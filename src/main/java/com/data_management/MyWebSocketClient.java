package com.data_management;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class MyWebSocketClient extends WebSocketClient {
    private DataStorage dataStorage;

    public MyWebSocketClient(URI serverUri, DataStorage dataStorage) {
        super(serverUri);
        this.dataStorage = dataStorage;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Opened server connection");
    }

    @Override
    public void onMessage(String message) {
        String[] parts = message.split(",");
        if(parts.length != 4) return; // Invalid format for message
        try {
            int id = Integer.parseInt(parts[0].split(":")[1].trim());
            long timestamp = Long.parseLong(parts[1].split(":")[1].trim());
            String recordType = parts[2].split(":")[1].trim();
            double measurementValue = Double.parseDouble(parts[3].split(":")[1].trim());
            dataStorage.addPatientData(id, measurementValue, recordType, timestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed");
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("Problem: " + ex.getMessage());
    }

    public void connect() {
        try {
            super.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            super.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

