package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientEndpoint {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Replace with the server's IP address or hostname
        final int portNumber = 12345; // Make sure it matches the server's port

        try (Socket socket = new Socket(serverAddress, portNumber);
             //PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {

            // Create a thread to continuously listen for messages from the server
            Thread serverListenerThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = serverReader.readLine()) != null) {
                        System.out.println("SR: " + message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            serverListenerThread.start();

            Cinema cinema = new Cinema(socket);
            cinema.readWhatToDo();
            closeSocket(socket); // we are closing the socket manually to force readLine() of BufferedReader
            // to throw exception as it is listening on socket we are closing
            serverListenerThread.join(); // we are waiting for listener thread to die because of thrown exception
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    static private void closeSocket(Socket socket) {
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}