package com.data_management;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;

public class WebSocketDataReader implements DataReader {
    private WebSocketClient client;
    private final String serverUri;

    public WebSocketDataReader(String serverUri) {
        this.serverUri = serverUri;
    }

    @Override
    public void startReading(DataStorage dataStorage) throws IOException {
        client = new WebSocketClient(URI.create(serverUri)) {
            @Override
            public void onOpen(ServerHandshake handshakeData) {
                System.out.println("WebSocket connection opened.");
            }

            @Override
            public void onMessage(String message) {
                try {
                    // Expected format: patientId,timestamp,label,value
                    String[] parts = message.split(",", 4);
                    if (parts.length < 4) throw new IllegalArgumentException("Invalid message format");

                    int patientId = Integer.parseInt(parts[0]);
                    long timestamp = Long.parseLong(parts[1]);
                    String label = parts[2];
                    double value = Double.parseDouble(parts[3]);

                    dataStorage.addPatientData(patientId, value, label, timestamp);
                } catch (Exception e) {
                    System.err.println("Error parsing message: \"" + message + "\"");
                    e.printStackTrace();
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("WebSocket closed: " + reason);
            }

            @Override
            public void onError(Exception ex) {
                System.err.println("WebSocket error:");
                ex.printStackTrace();
            }
        };

        client.connect();
    }

    @Override
    public void stopReading() {
        if (client != null && client.isOpen()) {
            client.close();
        }
    }
}
