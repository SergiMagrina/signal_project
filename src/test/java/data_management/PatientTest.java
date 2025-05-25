package data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import com.data_management.Patient;
import com.data_management.PatientRecord;

public class PatientTest {

    private Patient patient;

    @BeforeEach
    public void setup() {
        patient = new Patient(123);
    }

    @Test
    public void testConstructor() {
        assertEquals(123, patient.getPatientId());
        assertTrue(patient.getAllRecords().isEmpty(), "New patient should have no records");
    }

    @Test
    public void testAddRecord() {
        patient.addRecord(98.6, "Temperature", 1000L);
        List<PatientRecord> records = patient.getAllRecords();
        assertEquals(1, records.size());

        PatientRecord record = records.get(0);
        assertEquals(123, record.getPatientId());
        assertEquals(98.6, record.getMeasurementValue());
        assertEquals("Temperature", record.getRecordType());
        assertEquals(1000L, record.getTimestamp());
    }

    @Test
    public void testGetRecordsWithinTimeRange() {
        // Add records with different timestamps
        patient.addRecord(80, "HeartRate", 1000L);
        patient.addRecord(85, "HeartRate", 2000L);
        patient.addRecord(90, "HeartRate", 3000L);

        // Get records between 1500 and 3500
        List<PatientRecord> filtered = patient.getRecords(1500L, 3500L);
        assertEquals(2, filtered.size());
        for (PatientRecord rec : filtered) {
            assertTrue(rec.getTimestamp() >= 1500L && rec.getTimestamp() <= 3500L);
        }
    }

    @Test
    public void testGetRecordsNoMatch() {
        patient.addRecord(75, "BloodPressure", 1000L);

        List<PatientRecord> filtered = patient.getRecords(2000L, 3000L);
        assertTrue(filtered.isEmpty(), "No records should match the time range");
    }
}

