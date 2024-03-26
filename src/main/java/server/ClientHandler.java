package server;

import root.RequestType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        System.out.println("ClientHandler created with unique socket: " + clientSocket.toString());
    }

    @Override
    public void run() {
        try (
                BufferedReader clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter clientWriter = new PrintWriter(clientSocket.getOutputStream(), true);
        ) {
            System.out.println("Running ClientHandler created with unique socket: " + clientSocket.toString());
            String request;
            RequestHandler requestHandler = new RequestHandler();
            while ((request = clientReader.readLine()) != null) {

                System.out.println("ClientHandler from socket " + clientSocket.toString() + " received request from client: " + request);
                String response = requestHandler.handle(request);
                System.out.println("Response to be sent to socket " + clientSocket.toString() + ":\n" + response);
                clientWriter.println(response);
            }
            System.out.println("ClientHandler with unique socket: " + clientSocket.toString() + " is about to die");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
