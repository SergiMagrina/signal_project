package outputs;

import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

import com.cardio_generator.outputs.TcpOutputStrategy;

class TCPOutputStrategyTest {

    private static final int TEST_PORT = 5555;
    private TcpOutputStrategy tcpOutputStrategy;
    private Socket clientSocket;
    private BufferedReader reader;

    @BeforeEach
    void setUp() throws Exception {
        tcpOutputStrategy = new TcpOutputStrategy(TEST_PORT);

        // Allow some time for the server to start
        TimeUnit.MILLISECONDS.sleep(100);

        // Simulate client connecting to the TCP server
        clientSocket = new Socket("localhost", TEST_PORT);
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        // Give time for the server to register the client
        TimeUnit.MILLISECONDS.sleep(100);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (clientSocket != null && !clientSocket.isClosed()) {
            clientSocket.close();
        }
    }

    @Test
    void testOutputDoesNothingIfNoClientConnected() throws Exception {
        // Simulate a strategy that is not connected to a client
        TcpOutputStrategy disconnectedStrategy = new TcpOutputStrategy(TEST_PORT + 1);

        // Allow server to start
        TimeUnit.MILLISECONDS.sleep(100);

        // It should not throw any exceptions
        assertDoesNotThrow(() ->
                disconnectedStrategy.output(1, 1000L, "Test", "None")
        );
    }
}

