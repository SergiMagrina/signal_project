package java.data_management;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class WebSocketDataReaderTest {
    private     DataStorage dataStorage;
    private WebSocketDataReader reader;

    @BeforeEach
    void setUp() {
        dataStorage = DataStorage.getInstance();
        dataStorage.clear();  // Ensure test isolation
        reader = new WebSocketDataReader("ws://dummy");
    }

    @Test
    void testValidMessageParsing() {
        String message = "101,1716612023,HR,78.5";

        // Simulate internal parsing (assuming refactoring allows testing this directly or inject WebSocketClient)
        reader.parseAndStoreMessage(message, dataStorage);

        Patient patient = dataStorage.getPatient(101);
        assertNotNull(patient);
        assertEquals(78.5, patient.getLatestValue("HR"), 0.01);
    }

    @Test
    void testMalformedMessage() {
        String badMessage = "INVALID,DATA";

        // Should not throw, should log error
        assertDoesNotThrow(() -> reader.parseAndStoreMessage(badMessage, dataStorage));
        assertNull(dataStorage.getPatient(0));  // No valid patient created
    }

    @Test
    void testPartialMessage() {
        String partialMessage = "102,1716612023,HR";  // Missing value

        assertDoesNotThrow(() -> reader.parseAndStoreMessage(partialMessage, dataStorage));
        assertNull(dataStorage.getPatient(102));
    }
}
