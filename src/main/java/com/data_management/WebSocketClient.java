package com.data_management;

import java.io.IOException;
import java.net.URI;

public class WebSocketClient implements WebSocketDataReader {
    private DataStorage dataStorage;
    private MyWebSocketClient client;
    private String uri;

    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        this.dataStorage = dataStorage;
    }

    @Override
    public void connect(String uri) {
        try {
            this.uri = uri;
            client = new MyWebSocketClient(new URI(uri), dataStorage);
            client.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        if (client == null) {
            System.out.println("There isn't any connection to the server.");
        } else {
            try {
                client.disconnect();
            } catch (Exception e) {
                System.out.println("Couldn't disconnect from the server.");
                e.printStackTrace();
            }
        }
    }
}
