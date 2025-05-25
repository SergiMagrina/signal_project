package outputs;

import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import com.cardio_generator.outputs.FileOutputStrategy;

class FileOutputStrategyTest {

    private static final String TEST_DIR = "test_output";
    private FileOutputStrategy fileOutputStrategy;

    @BeforeEach
    void setUp() throws IOException {
        fileOutputStrategy = new FileOutputStrategy(TEST_DIR);
        Files.createDirectories(Paths.get(TEST_DIR));
    }

    @AfterEach
    void tearDown() throws IOException {
        // Delete all files in the test directory after each test
        Files.walk(Paths.get(TEST_DIR))
                .map(Path::toFile)
                .forEach(file -> {
                    if (!file.delete()) {
                        System.err.println("Failed to delete " + file.getPath());
                    }
                });
        Files.deleteIfExists(Paths.get(TEST_DIR));
    }

    @Test
    void testOutputCreatesAndAppendsToFile() throws IOException {
        String label = "HeartRate";
        int patientId = 123;
        long timestamp = 1650000000000L;
        String data = "72bpm";

        fileOutputStrategy.output(patientId, timestamp, label, data);

        Path filePath = Paths.get(TEST_DIR, label + ".txt");
        assertTrue(Files.exists(filePath));

        List<String> lines = Files.readAllLines(filePath);
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).contains("Patient ID: 123"));
        assertTrue(lines.get(0).contains("Label: HeartRate"));
        assertTrue(lines.get(0).contains("Data: 72bpm"));
    }

    @Test
    void testOutputMultipleLabelsCreateSeparateFiles() throws IOException {
        fileOutputStrategy.output(1, 1000L, "ECG", "0.98");
        fileOutputStrategy.output(2, 2000L, "BloodPressure", "120/80");

        Path ecgPath = Paths.get(TEST_DIR, "ECG.txt");
        Path bpPath = Paths.get(TEST_DIR, "BloodPressure.txt");

        assertTrue(Files.exists(ecgPath));
        assertTrue(Files.exists(bpPath));

        assertEquals(1, Files.readAllLines(ecgPath).size());
        assertEquals(1, Files.readAllLines(bpPath).size());
    }

    @Test
    void testOutputAppendsToExistingFile() throws IOException {
        String label = "OxygenSaturation";
        fileOutputStrategy.output(1, 1000L, label, "98%");
        fileOutputStrategy.output(2, 2000L, label, "99%");

        Path filePath = Paths.get(TEST_DIR, label + ".txt");
        List<String> lines = Files.readAllLines(filePath);

        assertEquals(2, lines.size());
        assertTrue(lines.get(1).contains("Patient ID: 2"));
        assertTrue(lines.get(1).contains("Data: 99%"));
    }
}
