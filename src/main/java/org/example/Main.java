package org.example;

import org.example.entity.Train;
import org.example.entity.User;
import org.example.services.UserBookingService;
import org.example.util.UserServiceUtil;

import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        System.out.println("Running Train Booking System");
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        UserBookingService userBookingService;
        try {
            userBookingService = new UserBookingService();
        } catch (IOException ex) {
            System.out.println("There is something wrong");
            return;
        }

        Train trainSelectedForBooking = null;

        while (option != 7) {
            System.out.println("\nChoose option");
            System.out.println("1. Sign up");
            System.out.println("2. Login");
            System.out.println("3. Fetch Bookings");
            System.out.println("4. Search Trains");
            System.out.println("5. Book a Seat");
            System.out.println("6. Cancel my Booking");
            System.out.println("7. Exit the App");

            option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.println("Enter the username to signup");
                    String nameToSignUp = scanner.next();
                    System.out.println("Enter the password to signup");
                    String passwordToSignUp = scanner.next();
                    User userToSignup = new User(
                            nameToSignUp,
                            passwordToSignUp,
                            UserServiceUtil.hashPassword(passwordToSignUp),
                            new ArrayList<>(),
                            UUID.randomUUID().toString()
                    );
                    userBookingService.signup(userToSignup);
                    break;

                case 2:
                    System.out.println("Enter the username to Login");
                    String nameToLogin = scanner.next();
                    System.out.println("Enter the password to login");
                    String passwordToLogin = scanner.next();
                    User userToLogin = new User(
                            nameToLogin,
                            passwordToLogin,
                            UserServiceUtil.hashPassword(passwordToLogin),
                            new ArrayList<>(),
                            UUID.randomUUID().toString()
                    );
                    try {
                        userBookingService = new UserBookingService(userToLogin);
                    } catch (IOException ex) {
                        return;
                    }
                    break;

                case 3:
                    System.out.println("Fetching your bookings...");
                    userBookingService.fetchBooking();
                    break;

                case 4:
                    System.out.println("Type your source station:");
                    String source = scanner.next();
                    System.out.println("Type your destination station:");
                    String dest = scanner.next();

                    List<Train> trains = userBookingService.getTrains(source, dest);
                    int index = 1;
                    for (Train t : trains) {
                        System.out.println(index + ". Train id : " + t.getTrainId());
                        for (Map.Entry<String, String> entry : t.getStationTimes().entrySet()) {
                            System.out.println("   Station: " + entry.getKey() + "  Time: " + entry.getValue());
                        }
                        index++;
                    }

                    System.out.println("Select a train by typing 1,2,3...");
                    int selectedIndex = scanner.nextInt() - 1; // subtract 1 for correct index
                    trainSelectedForBooking = trains.get(selectedIndex);
                    break;

                case 5:
                    if (trainSelectedForBooking == null) {
                        System.out.println("⚠️ Please search and select a train first (Option 4)");
                        break;
                    }
                    System.out.println("Available seats:");
                    List<List<Integer>> seats = userBookingService.fetchSeats(trainSelectedForBooking);
                    for (List<Integer> row : seats) {
                        for (Integer val : row) {
                            System.out.print(val + " ");
                        }
                        System.out.println();
                    }

                    System.out.println("Select the seat by typing row and column");
                    System.out.print("Enter the row: ");
                    int row = scanner.nextInt();
                    System.out.print("Enter the column: ");
                    int col = scanner.nextInt();

                    System.out.println("Booking your seat....");
                    Boolean booked = userBookingService.bookSeat(trainSelectedForBooking, row, col); // <-- FIXED
                    if (booked) {
                        System.out.println("Booked! Enjoy your journey");
                    } else {
                        System.out.println(" Can't book this seat");
                    }
                    break;

                case 6:
                    System.out.println("Cancel booking feature not implemented yet.");
                    break;

                case 7:
                    System.out.println("Exiting the app. Goodbye!");
                    return;

                default:
                    System.out.println("Invalid option, try again.");
                    break;
            }
        }
    }
}
