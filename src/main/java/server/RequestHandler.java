package server;

import root.RequestType;

import java.sql.SQLException;
import java.util.ArrayList;


public class RequestHandler {
    MySQLAccess mySQLAccess;
    public RequestHandler() {
        System.out.println("RequestHandler created");
        try {
            this.mySQLAccess = new MySQLAccess();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String handle(String message) {
        ArrayList<String> tokens = new ArrayList<>();
        System.out.println("Request to handle by requestHandler: " + message);
        tokenize(tokens, message);
        System.out.println("Tokenized request = " + tokens);

        try {
            switch (RequestType.valueOf(tokens.get(0))) {
                case SHOW_CLIENTS:
                    //System.out.println("SHOW_CLIENTS RequestHandler.handle(message = " + message + ")");
                    return mySQLAccess.getAllClients();
                case SHOW_MOVIES:
                    return mySQLAccess.getAllMovies();
                case SHOW_SHOWINGS:
                    //System.out.println("SHOW_SHOWINGS RequestHandler.handle(message = " + message + ")");
                    return mySQLAccess.getAllShowings();
                case SHOW_BOOKINGS:
                    //System.out.println("SHOW_BOOKINGS RequestHandler.handle(message = " + message + ")");
                    return mySQLAccess.getAllBookings();
                case SHOW_HALLS:
                    //System.out.println("SHOW_HALLS RequestHandler.handle(message = " + message + ")");
                    return mySQLAccess.getAllHalls();
                case ADD_CLIENT:
                    //System.out.println("ADD_CLIENT RequestHandler.handle(message = " + message + ")");
                    return mySQLAccess.addClient(tokens.get(1), tokens.get(2));
                case ADD_MOVIE:
                    return mySQLAccess.addMovie(tokens.get(1), tokens.get(2), tokens.get(3), tokens.get(4));
                case ADD_SHOWING:
                    //System.out.println("ADD_SHOWING RequestHandler.handle(message = " + message + ")");
                    return mySQLAccess.addShowing(tokens.get(1), tokens.get(2), tokens.get(3));
                case ADD_HALL:
                    //System.out.println("ADD_HALL RequestHandler.handle(message = " + message + ")");
                    return mySQLAccess.addHall(tokens.get(1), tokens.get(2), tokens.get(3));
                case MAKE_BOOKING:
                    //System.out.println("MAKE_BOOKING RequestHandler.handle(message = " + message + ")");
                    return mySQLAccess.makeBooking(tokens.get(1), tokens.get(2), tokens.get(3), tokens.get(4));
                case CANCEL_BOOKING:
                    //System.out.println("CANCEL_BOOKING RequestHandler.handle(message = " + message + ")");
                    return mySQLAccess.cancelBooking(tokens.get(1));
                case SHOW_SEATING_ARRANGEMENT:
                    return mySQLAccess.showSeatingArrangement(tokens.get(1));
                default:
                    System.out.println("Default RequestHandler.handle for request" + message);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static void tokenize(ArrayList<String> list, String message) {
        System.out.println("Request to tokenize: " + message);
        int i;
        for (i = 0; i < message.length(); i++) {
            if(message.charAt(i) == '|') {
                break;
            }
        }
        list.addLast(message.substring(0, i));
        if(i == message.length()) return;
        tokenize(list, message.substring(i + 1));
    }
}
