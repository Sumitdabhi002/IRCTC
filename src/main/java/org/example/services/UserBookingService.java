package org.example.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entity.Train;
import org.example.entity.User;
import org.example.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class UserBookingService {
    private User user;
    private List<User> userList;

    private static final String USERS_PATH = "C:\\Users\\SUMIT\\Desktop\\java project\\IRCTC\\src\\main\\java\\org\\example\\localdb\\users.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();


    public UserBookingService() throws IOException {
        loadUserListFromFile();
    }

    public UserBookingService(User user) throws IOException {
        this.user = user;
        loadUserListFromFile();
    }


    private void loadUserListFromFile() throws IOException {
        File file = new File(USERS_PATH);
        if (file.exists() && file.length() > 0) {
            userList = objectMapper.readValue(file, new TypeReference<List<User>>() {});
        } else {
            userList = new ArrayList<>();
        }
    }

    private void saveUserListToFile() throws IOException {
        objectMapper.writeValue(new File(USERS_PATH), userList);
    }

    public Boolean loginUser() {
        return userList.stream()
                .anyMatch(u -> u.getName().equalsIgnoreCase(user.getName())
                        && UserServiceUtil.checkPassword(user.getPassword(), u.getHashPassword()));
    }

    public boolean signup(User newUser) {
        try {

            boolean exists = userList.stream()
                    .anyMatch(u -> u.getName().equalsIgnoreCase(newUser.getName()));

            if (exists) {
                System.out.println("User already exists!");
                return false;
            }

            userList.add(newUser);
            saveUserListToFile();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    public void fetchBooking() {
        userList.stream()
                .filter(u -> u.getName().equals(user.getName())
                        && UserServiceUtil.checkPassword(user.getPassword(), u.getHashPassword()))
                .findFirst()
                .ifPresent(User::printTickets);
    }

    public Boolean ticketCancellation(String ticketId) {
        if (ticketId == null || ticketId.isEmpty()) {
            System.out.println("Ticket Id cannot be null or empty");
            return false;
        }

        boolean removed = user.getTicketBooked().removeIf(ticket -> ticket.getTicketId().equals(ticketId));

        if (removed) {
            System.out.println("Ticket with Id: " + ticketId + " is canceled");
            try {
                saveUserListToFile(); // persist cancellation
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            System.out.println("There is no Ticket Id: " + ticketId);
            return false;
        }
    }


    public List<Train> getTrains(String source, String destination) {
        try {
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source, destination);
        } catch (IOException ex) {
            return new ArrayList<>();
        }
    }

    public List<List<Integer>> fetchSeats(Train train) {
        return train.getSeats();
    }

    public Boolean bookSeat(Train train, int row, int col) {
        try {
            TrainService trainService = new TrainService();
            List<List<Integer>> seats = train.getSeats();

            if (row >= 0 && row < seats.size() && col >= 0 && col < seats.get(row).size()) {
                if (seats.get(row).get(col) == 0) {
                    seats.get(row).set(col, 1);
                    train.setSeats(seats);
                    return true;
                } else {
                    System.out.println("Seat already booked!");
                    return false;
                }
            } else {
                System.out.println("Invalid row or seat number!");
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }
}
