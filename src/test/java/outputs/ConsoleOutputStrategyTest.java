package outputs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.cardio_generator.outputs.ConsoleOutputStrategy;

public class ConsoleOutputStrategyTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void testConsoleOutputFormat() {
        ConsoleOutputStrategy strategy = new ConsoleOutputStrategy();

        int patientId = 1;
        long timestamp = 1234567890L;
        String label = "TestLabel";
        String data = "TestData";

        strategy.output(patientId, timestamp, label, data);

        String output = outContent.toString();

        // Basic format check
        assertTrue(output.contains("Patient ID: 1"));
        assertTrue(output.contains("Timestamp: 1234567890"));
        assertTrue(output.contains("Label: TestLabel"));
        assertTrue(output.contains("Data: TestData"));
    }
}


