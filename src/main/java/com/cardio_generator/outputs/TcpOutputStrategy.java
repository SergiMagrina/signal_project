package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * The {@code TcpOutputStrategy} class implements the {@link OutputStrategy} interface
 * to send patient data over a TCP connection to a connected client.
 * <p>
 * This class starts a TCP server on the specified port and waits for a single client connection.
 * Once connected, it streams formatted patient data messages to the client.
 */
public class TcpOutputStrategy implements OutputStrategy {

    /**
     * The server socket used to listen for client connections.
     */
    private ServerSocket serverSocket;

    /**
     * The socket representing the connection to the client.
     */
    private Socket clientSocket;

    /**
     * A writer used to send messages to the connected client.
     */
    private PrintWriter out;

    /**
     * Constructs a {@code TcpOutputStrategy} that starts a TCP server on the specified port.
     * <p>
     * The server accepts a single client connection in a background thread. Once connected,
     * output data will be streamed to that client.
     *
     * @param port the TCP port number on which to start the server
     */
    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);

            // Accept client connection in a separate thread to avoid blocking
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    System.err.println("Error accepting client connection: " + e.getMessage());
                }
            });
        } catch (IOException e) {
            System.err.println("Error starting TCP server: " + e.getMessage());
        }
    }

    /**
     * Sends a formatted patient data message to the connected client.
     * <p>
     * If no client is connected yet, this method does nothing.
     *
     * @param patientId the unique ID of the patient
     * @param timestamp the timestamp of the data in milliseconds since UNIX epoch
     * @param label     the category or type of the data (e.g., "HeartRate")
     * @param data      the actual measurement or data string to send
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message);
        }
    }
}
