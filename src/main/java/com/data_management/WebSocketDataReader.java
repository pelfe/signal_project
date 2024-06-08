package com.data_management;

import java.io.IOException;

public interface WebSocketDataReader {
    /**
     * Reads data from a source and it then stores it into a storage.
     *
     * @param dataStorage place to store data
     * @throws IOException when it can't read the data
     */

    void readData(DataStorage dataStorage) throws IOException;
    void connect() throws IOException;
    void disconnect() throws IOException;

    void onMessage(String message);
}
