package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;


/**
 * Accepts client connections to send data to clients.
 *
 * Usage: set your port and then use output
 * to send data to client side.
 *
 */
public class TcpOutputStrategy implements OutputStrategy {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    /*
    * Start TPC server at provided port, then listens to client connections at
    * a new threat. When client connects it uses PrintWriter to send them data.
    * Send them print statement of their own IP after this stream has started.
    *
    *
    * @param port is the port that server can use to detect client connections.
    * @throws IOException if server can't be started at the provided
    * port or if can't accept the client.
    */
    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);

            // Accept clients in a new thread to not block the main thread
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
    * This method sends the output to client.
    * Format of the data send to client is: "patientId: %d, timestamp: %d, label: %s, data: %s"
    *
    * @param patientId is the ID of each patient.
    * @param timestamp is the time when data was send.
    * @param label is just the name for the output.
    * @param data is the content of the output.
    * */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message);
        }
    }
}
