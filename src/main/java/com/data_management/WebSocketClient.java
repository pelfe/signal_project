package com.data_management;

import java.io.IOException;

public class WebSocketClient implements WebSocketDataReader {
    private DataStorage dataStorage;
    private WebSocketClient client;

    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        this.dataStorage = dataStorage;
    }

    @Override
    public void connect() {
        try {
            client = new WebSocketClient();
            client.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        try {
            client.disconnect();
        } catch (Exception e) {
            System.out.println("Coulnd't disconnect from the server.");
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Message: " + message);
    }
}
