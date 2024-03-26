package server;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MySQLAccess {
    private final String USER = "cinema_db_user";
    private final String PASSWORD = "1234";
    private final String LINK = "jdbc:mysql://localhost:3306/cinema_db";

    Connection connection;

    public MySQLAccess() throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(LINK, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public String getAllClients() {
        StringBuilder result = new StringBuilder("[");
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM client")) {

            while (resultSet.next()) {
                result.append("client_id = ").append(resultSet.getInt("client_id")).append(", ");
                result.append("first_name = ").append(resultSet.getString("first_name")).append(", ");
                result.append("last_name = ").append(resultSet.getString("last_name")).append("\n");
            }
            result.append("]");
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception
            return "getAllClients error " + e.getMessage();
        }
    }

    public String getAllMovies() {
        StringBuilder result = new StringBuilder("[");
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM movie")) {

            while (resultSet.next()) {
                result.append("movie_id = ").append(resultSet.getInt("movie_id")).append(", ");
                result.append("title = ").append(resultSet.getString("title")).append(", ");
                result.append("length = ").append(resultSet.getInt("length")).append(", ");
                result.append("genre = ").append(resultSet.getString("genre")).append(", ");
                result.append("price = ").append(resultSet.getDouble("price")).append("\n");
            }
            result.append("]");
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception
            return "getAllMovies error " + e.getMessage();
        }
    }


    public String getAllShowings() {
        StringBuilder result = new StringBuilder("[");
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM showing")) {

            while (resultSet.next()) {
                result.append("showing_id = ").append(resultSet.getInt("showing_id")).append(", ");
                result.append("movie_id = ").append(resultSet.getInt("movie_id")).append(", ");
                result.append("hall_id = ").append(resultSet.getInt("hall_id")).append(", ");
                result.append("date = ").
                        append(
                                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                                        resultSet.getTimestamp("date")
                                )
                        ).append("\n");
            }
            result.append("]");
            return result.toString();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception
            return "getAllShowings error " + e.getMessage();
        }
    }
/*
    *//**
     * Returns string representing all made bookings as the values of booking attributes for each table row
     * @return
     *//*
    public String getAllBookings() {
        StringBuilder result = new StringBuilder("[");
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM booking")) {

            while (resultSet.next()) {
                result.append("booking_id = ").append(resultSet.getInt("booking_id")).append(", ");
                result.append("client_id = ").append(resultSet.getInt("client_id")).append(", ");
                result.append("showing_id = ").append(resultSet.getInt("showing_id")).append(", ");
                result.append("seat_id = ").append(resultSet.getInt("seat_id")).append("\n");
            }
            result.append("]");
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception
            return "getAllBookings error " + e.getMessage();
        }
    }*/

    /**
     * Returns string representing all made bookings as booking_id, client_id, showing_id, seat_id, seat's row and number
     * @return
     */
    public String getAllBookings() {
        StringBuilder result = new StringBuilder("[");
        // Update the SQL query to join the booking table with the seat table
        String query = "SELECT b.booking_id, b.client_id, b.showing_id, b.seat_id, s.row, s.number " +
                "FROM booking b " +
                "JOIN seat s ON b.seat_id = s.seat_id";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                result.append("booking_id = ").append(resultSet.getInt("booking_id")).append(", ");
                result.append("client_id = ").append(resultSet.getInt("client_id")).append(", ");
                result.append("showing_id = ").append(resultSet.getInt("showing_id")).append(", ");
                result.append("seat_id = ").append(resultSet.getInt("seat_id")).append(", ");
                result.append("seat_row = ").append(resultSet.getInt("row")).append(", ");
                result.append("seat_number = ").append(resultSet.getInt("number")).append("\n");
            }
            result.append("]");
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception
            return "getAllBookings error " + e.getMessage();
        }
    }

    public String getAllHalls() {
        StringBuilder result = new StringBuilder("[");
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM hall")) {

            while (resultSet.next()) {
                result.append("hall_id = ").append(resultSet.getInt("hall_id")).append(", ");
                result.append("num_of_rows = ").append(resultSet.getInt("num_of_rows")).append(", ");
                result.append("seats_per_row = ").append(resultSet.getInt("seats_per_row")).append(", ");
                result.append("name = ").append(resultSet.getString("name")).append("\n");
            }
            result.append("]");
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception
            return "getAllHalls error " + e.getMessage();
        }
    }

    public String addClient(String first_name, String last_name) {
        String temp = "INSERT INTO cinema_db.client (first_name, last_name) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(temp);) {
            preparedStatement.setString(1, first_name);
            preparedStatement.setString(2, last_name);
            preparedStatement.executeUpdate();
            return "Client added";
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception
            return "addClient error " + e.getMessage();
        }
    }

    public String addMovie(String title,
                           String length,
                           String genre,
                           String price) {

        String temp = "INSERT INTO cinema_db.movie (title, length, genre, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(temp);) {
            preparedStatement.setString(1, title);
            preparedStatement.setInt(2, Integer.parseInt(length));
            preparedStatement.setString(3, genre);
            preparedStatement.setDouble(4, Double.parseDouble(price));

            preparedStatement.executeUpdate();
            return "Movie added";
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception
            return "addMovie error " + e.getMessage();
        }
    }


    public String addShowing(String movie_id, String hall_id, String date) {
        String temp = "INSERT INTO cinema_db.showing (movie_id, hall_id, date) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(temp);) {
            preparedStatement.setInt(1, Integer.parseInt(movie_id));
            preparedStatement.setInt(2, Integer.parseInt(hall_id));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(date));
            preparedStatement.executeUpdate();
            return "Showing added";
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception
            return "addShowing error " + e.getMessage();
        }
    }

    public String addHall(String num_of_rows, String seats_per_row, String name) {
        String temp = "INSERT INTO cinema_db.hall (num_of_rows, seats_per_row, name) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(temp);) {
            preparedStatement.setInt(1, Integer.parseInt(num_of_rows));
            preparedStatement.setInt(2, Integer.parseInt(seats_per_row));
            preparedStatement.setString(3, name);
            preparedStatement.executeUpdate();
            return "Hall added";
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception
            return "addHall error " + e.getMessage();
        }
    }

    /*public String makeBooking(String client_id, String showing_id, String seat_id) {
        String temp = "INSERT INTO cinema_db.booking (client_id, showing_id, seat_id) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(temp);) {
            preparedStatement.setInt(1, Integer.parseInt(client_id));
            preparedStatement.setInt(2, Integer.parseInt(showing_id));
            preparedStatement.setInt(3, Integer.parseInt(seat_id));
            preparedStatement.executeUpdate();
            return "Booking added";
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception
            return "makeBooking error " + e.getMessage();
        }
    }*/

    /**
     * Make booking method to be invoked with seat row and number instead of seat id
     * @param client_id
     * @param showing_id
     * @param seat_row
     * @param seat_number
     * @return
     */
    public String makeBooking(String client_id, String showing_id, String seat_row, String seat_number) {
        // We are trying to find seat by seat row and number
        String findSeatIdQuery =
                "SELECT seat_id " +
                "FROM showing sh " +
                "JOIN seat s USING(hall_id)" +
                "WHERE  sh.showing_id = ? " +
                "AND    s.row = ? " +
                "AND    s.number = ?";
        int seat_id;

        try (PreparedStatement preparedStatement = connection.prepareStatement(findSeatIdQuery);) {
            preparedStatement.setInt(1, Integer.parseInt(showing_id));
            preparedStatement.setInt(2, Integer.parseInt(seat_row));
            preparedStatement.setInt(3, Integer.parseInt(seat_number));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                seat_id = resultSet.getInt("seat_id");
            } else {
                return "No seat for given showing_id, seat row and number found!";
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception
            return "Error finding seat: " + e.getMessage();
        }

        String insertStatement = "INSERT INTO cinema_db.booking (client_id, showing_id, seat_id) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStatement);) {
            preparedStatement.setInt(1, Integer.parseInt(client_id));
            preparedStatement.setInt(2, Integer.parseInt(showing_id));
            preparedStatement.setInt(3, seat_id);
            preparedStatement.executeUpdate();
            return "Booking added";
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception
            return "makeBooking error " + e.getMessage();
        }

    }


    public String cancelBooking(String booking_id) {
        String checkIfBookingExistsQuery = "SELECT COUNT(*) FROM booking WHERE booking_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(checkIfBookingExistsQuery);) {
            preparedStatement.setInt(1, Integer.parseInt(booking_id));
            ResultSet resultSet = preparedStatement.executeQuery();
            int numOfRowsWithGivenId = -1;
            if (resultSet.next()) {
                numOfRowsWithGivenId = resultSet.getInt(1);
            }
            if (numOfRowsWithGivenId == 0) {
                return "Booking with id = " + booking_id + " not found to cancel it";
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception
            return "cancelBooking error +" + e.getMessage();
        }

        String deleteStmt = "DELETE FROM cinema_db.booking WHERE booking_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteStmt);) {
            preparedStatement.setInt(1, Integer.parseInt(booking_id));
            preparedStatement.executeUpdate();
            return "Booking with id = " + booking_id + " has been canceled";
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception
            return "cancelBooking error: " + e.getMessage();
        }
    }

    public String showSeatingArrangement(String showingId) {
        StringBuilder result = new StringBuilder();
        try {
            // Fetch hall information
            int numRows, seatsPerRow;
            try (PreparedStatement hallStmt = connection.prepareStatement(
                    "SELECT h.num_of_rows, h.seats_per_row FROM hall h JOIN showing s ON h.hall_id = s.hall_id WHERE s.showing_id = ?")) {
                hallStmt.setInt(1, Integer.parseInt(showingId));
                ResultSet hallResult = hallStmt.executeQuery();
                if (!hallResult.next()) {
                    return "Showing not found or hall not found for showing ID: " + showingId;
                }
                numRows = hallResult.getInt("num_of_rows");
                seatsPerRow = hallResult.getInt("seats_per_row");
            }

            // Initialize seating matrix
            boolean[][] seats = new boolean[numRows][seatsPerRow];

            // Fetch taken seats
            try (PreparedStatement seatStmt = connection.prepareStatement(
                    "SELECT s.row, s.number FROM seat s JOIN booking b ON s.seat_id = b.seat_id WHERE b.showing_id = ?")) {
                seatStmt.setInt(1, Integer.parseInt(showingId));
                ResultSet seatResult = seatStmt.executeQuery();
                while (seatResult.next()) {
                    int row = seatResult.getInt("row");
                    int number = seatResult.getInt("number");
                    seats[row - 1][number - 1] = true; // Mark seat as taken
                }
            }

            // Add seat numbers as header
            result.append("    ");
            for (int i = 1; i <= seatsPerRow; i++) {
                result.append(String.format("%-4d", i));
            }
            result.append("\n");

            // Create graphical representation of seating arrangement
            for (int i = 0; i < numRows; i++) {
                result.append(String.format("%-4d", i + 1)); // Add row number
                for (int j = 0; j < seatsPerRow; j++) {
                    result.append(seats[i][j] ? "[X] " : "[ ] "); // [X] for taken, [ ] for available
                }
                result.append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error retrieving seating arrangement: " + e.getMessage();
        }
        return result.toString();
    }
}

