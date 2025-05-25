package data_management;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import com.data_management.FileDataReader;
import com.data_management.PatientRecord;
import com.data_management.DataStorage;
import com.data_management.Patient;

import static org.junit.jupiter.api.Assertions.*;

class FileDataReaderTest {

    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        tempDir = Files.createTempDirectory("test_output_dir");
        DataStorage.getInstance().clear(); // 💡 Clear singleton
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.walk(tempDir)
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    void testReadData_ValidFile() throws IOException {
        String content = "1,72.5,HeartRate,1700000000000\n" +
                "2,88.1,BloodPressure,1700000001000";
        Path file = Files.write(tempDir.resolve("data.csv"), content.getBytes());

        DataStorage storage = DataStorage.getInstance();
        FileDataReader reader = new FileDataReader(tempDir.toString());

        reader.readData(storage);

        List<PatientRecord> records1 = storage.getRecords(1, 0, Long.MAX_VALUE);
        List<PatientRecord> records2 = storage.getRecords(2, 0, Long.MAX_VALUE);

        assertEquals(1, records1.size());
        assertEquals(1, records2.size());

        assertEquals("HeartRate", records1.get(0).getRecordType());
        assertEquals("BloodPressure", records2.get(0).getRecordType());
    }

    @Test
    void testReadData_InvalidFileFormat() throws IOException {
        String content = "1,72.5,HeartRate\n" +  // Missing timestamp
                "not,a,number,line\n";   // Fully malformed
        Files.write(tempDir.resolve("bad_data.csv"), content.getBytes());

        DataStorage storage = DataStorage.getInstance();
        FileDataReader reader = new FileDataReader(tempDir.toString());

        reader.readData(storage);

        List<Patient> patients = storage.getAllPatients();
        assertTrue(patients.isEmpty(), "No valid records should be parsed");
    }

    @Test
    void testReadData_EmptyDirectory() {
        FileDataReader reader = new FileDataReader(tempDir.toString());
        DataStorage storage = DataStorage.getInstance();

        reader.readData(storage);

        assertTrue(storage.getAllPatients().isEmpty(), "No data should be added from empty directory");
    }

    @Test
    void testReadData_InvalidDirectory() {
        FileDataReader reader = new FileDataReader("non_existent_directory");
        DataStorage storage = DataStorage.getInstance();

        reader.readData(storage);

        assertTrue(storage.getAllPatients().isEmpty(), "Invalid directory should not add any data");
    }
}

