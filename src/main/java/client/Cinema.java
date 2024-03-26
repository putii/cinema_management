package client;

import root.RequestType;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

/**
 * Driver class for Cinema Management System
 */
public class Cinema
{
	Socket socket;
	PrintWriter writer;
	Cinema(Socket socket) {
		System.out.println("Cinema created for socket: " + socket.toString() + "\n");
		this.socket = socket;
		try {
			this.writer = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	public void readWhatToDo() {
		int option;
		Scanner select = new Scanner(System.in);
		do {
			pressEnterToContinue();
			displayMenu();
			option = select.nextInt();
			switch (option) {
				case 1:
					showClients();
					break;
				case 2:
					showMovies();
					break;
				case 3:
					showShowings();
					break;
				case 4:
					showBookings();
					break;
				case 5:
					showHalls();
					break;
				case 6:
					addClient();
					break;
				case 7:
					addMovie();
					break;
				case 8:
					addShowing();
					break;
				case 9:
					addHall();
					break;
				case 10:
					makeBooking();
					break;
				case 11:
					cancelBooking();
					break;
				case 12:
					showSeatingArrangement();
					break;
				case 13:
					System.out.println("Bye, bye!");
					return; // autoclose of socket in ClientEndpoint
				default:
					System.out.println("Invalid option. Please try again.");
			}
		} while (true);
	}




	private static void displayMenu() {
		System.out.println("-------------------------------");
		System.out.println("Cinema Management System");
		System.out.println("-------------------------------");
		System.out.println("1. Show Clients");
		System.out.println("2. Show Movies");
		System.out.println("3. Show Showings");
		System.out.println("4. Show Bookings");
		System.out.println("5. Show Halls");
		System.out.println("6. Add Client");
		System.out.println("7. Add Movie");
		System.out.println("8. Add Showing");
		System.out.println("9. Add Hall");
		System.out.println("10. Make Booking");
		System.out.println("11. Cancel Booking");
		System.out.println("12. Show Seating Arrangement");
		System.out.println("13. Exit");
		System.out.println("-------------------------------");
		System.out.print("Enter Option: \n");
	}



	private void showClients() {
		System.out.println("Cinema.ShowClients() called");
		String request = String.valueOf(RequestType.SHOW_CLIENTS);
		sendRequest(request);
	}

	private void showMovies() {
		System.out.println("Cinema.ShowMovies() called");
		String request = String.valueOf(RequestType.SHOW_MOVIES);
		sendRequest(request);
	}


	private void showShowings() {
		System.out.println("Cinema.showShowings() called");
		String request = String.valueOf(RequestType.SHOW_SHOWINGS);
		sendRequest(request);
	}
	private void showBookings() {
		System.out.println("Cinema.showBookings() called");
		String request = String.valueOf(RequestType.SHOW_BOOKINGS);
		sendRequest(request);
	}

	private void showHalls() {
		System.out.println("Cinema.showHalls() called");
		String request = String.valueOf(RequestType.SHOW_HALLS);
		sendRequest(request);
	}

	private void addClient() {
		System.out.println("Cinema.addClient() called");
		System.out.println("Add Client:");
		Scanner input = new Scanner(System.in);
		System.out.print("first_name: ");
		String first_name = input.nextLine();
		System.out.print("last_name: ");
		String last_name = input.nextLine();

		String request = RequestType.ADD_CLIENT + "|" + first_name + "|" + last_name;
		sendRequest(request);
	}

	private void addMovie() {
		System.out.println("Cinema.addMovie() called");
		System.out.println("Add Movie:");
		Scanner input = new Scanner(System.in);

		System.out.print("title: ");
		String title = input.nextLine();
		System.out.print("length (in minutes): ");
		int length = input.nextInt();  // Używam nextInt() dla wartości liczbowych
		input.nextLine();  // Czyszczenie bufora skanera po odczytaniu liczby
		System.out.print("genre: ");
		String genre = input.nextLine();
		System.out.print("price: ");
		double price = input.nextDouble();
		input.nextLine();  // Ponowne czyszczenie bufora

		String request = RequestType.ADD_MOVIE + "|" + title + "|" + length + "|" + genre + "|" + price;
		sendRequest(request);
	}

	private void addShowing() {
		System.out.println("Cinema.addShowing() called");
		System.out.println("Add Show:");
		System.out.print("Show Name: ");
		Scanner input = new Scanner(System.in);
		System.out.print("movie_id: ");
		String movieId = input.nextLine();
		System.out.print("hall_id: ");
		String hall_id = input.nextLine();
		System.out.print("date in format yyyy-MM-dd HH:mm:ss :");
		String date = input.nextLine();

		String request = RequestType.ADD_SHOWING + "|" + movieId + "|" + hall_id + "|" + date;
		sendRequest(request);
	}

	private void addHall() {
		System.out.println("Add Hall:");

		System.out.print("num_of_rows: ");
		Scanner input = new Scanner(System.in);
		String num_of_rows = input.nextLine();
		System.out.print("seats_per_row: ");
		String seats_per_row = input.nextLine();
		System.out.print("name: ");
		String name = input.nextLine();

		String request = RequestType.ADD_HALL + "|" + num_of_rows + "|" + seats_per_row + "|" + name;
		sendRequest(request);
	}

	private void makeBooking() {
		System.out.println("Make booking:");

		System.out.print("client_id: ");
		Scanner input = new Scanner(System.in);
		String client_id = input.nextLine();
		System.out.println("showing_id: ");
		String showing_id = input.nextLine();
		System.out.println("seat_row: ");
		String seat_row = input.nextLine();
		System.out.println("seat_number: ");
		String seat_number = input.nextLine();

		String request = RequestType.MAKE_BOOKING + "|" + client_id + "|" + showing_id + "|" + seat_row + "|" + seat_number;
		sendRequest(request);
	}

	private void cancelBooking() {
		System.out.println("Cancel booking:");

		System.out.print("booking_id: ");
		Scanner input = new Scanner(System.in);
		String booking_id = input.nextLine();

		String request = RequestType.CANCEL_BOOKING + "|" + booking_id;
		sendRequest(request);
	}

	private void showSeatingArrangement() {
		System.out.print("Show seating arrangement: ");

		System.out.println("showing_id: ");
		Scanner input = new Scanner(System.in);
		String showing_id = input.nextLine();

		String request = RequestType.SHOW_SEATING_ARRANGEMENT + "|" + showing_id;
		sendRequest(request);
	}

	private void pressEnterToContinue()
	{
		System.out.println("Press Enter key to continue...");
		try {
			System.in.read();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendRequest(String request) {
		writer.println(request);
		System.out.println("Request: " + request + " sent to server");
	}
}

