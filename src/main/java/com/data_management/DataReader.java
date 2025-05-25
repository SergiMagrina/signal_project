package com.data_management;

import java.io.IOException;

public interface DataReader {

    /**
     * Connects to the data source and starts receiving data continuously.
     * Incoming data should be stored or updated in the provided DataStorage.
     *
     * This method is expected to run asynchronously, continuously listening for data.
     *
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error connecting or reading the data
     */
    void startListening(DataStorage dataStorage) throws IOException;

    /**
     * Stops listening to the data source and cleans up resources.
     *
     * @throws IOException if there is an error during shutdown
     */
    void stopListening() throws IOException;
}
