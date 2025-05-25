package data_management;

import static org.junit.jupiter.api.Assertions.*;

import com.data_management.PatientRecord;
import com.data_management.Patient;
import com.data_management.DataStorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

class DataStorageTest {

    @BeforeEach
    void clearStorage() throws Exception {
        // Use reflection to clear the singleton between tests
        Field instanceField = DataStorage.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null); // reset the singleton instance
    }

    @Test
    void testAddAndGetRecords() {
        DataStorage storage = DataStorage.getInstance();
        long timestamp1 = 1714376789050L;
        long timestamp2 = 1714376789051L;

        storage.addPatientData(1, 98.6, "Temperature", timestamp1);
        storage.addPatientData(1, 99.1, "Temperature", timestamp2);

        List<PatientRecord> records = storage.getRecords(1, timestamp1, timestamp2);
        assertEquals(2, records.size(), "Should return 2 records");

        assertEquals(98.6, records.get(0).getMeasurementValue());
        assertEquals("Temperature", records.get(0).getRecordType());
    }

    @Test
    void testGetRecordsForNonExistentPatientReturnsEmptyList() {
        DataStorage storage = DataStorage.getInstance();
        List<PatientRecord> records = storage.getRecords(999, 0L, System.currentTimeMillis());
        assertNotNull(records);
        assertTrue(records.isEmpty(), "Expected an empty list for non-existent patient");
    }

    @Test
    void testSingletonBehavior() {
        DataStorage s1 = DataStorage.getInstance();
        DataStorage s2 = DataStorage.getInstance();
        assertSame(s1, s2, "DataStorage should be a singleton");
    }

    @Test
    void testGetAllPatients() {
        DataStorage storage = DataStorage.getInstance();
        storage.addPatientData(1, 80.0, "HeartRate", 1714376000000L);
        storage.addPatientData(2, 120.0, "BloodPressure", 1714376001000L);

        List<Patient> patients = storage.getAllPatients();
        assertEquals(2, patients.size(), "Should return 2 unique patients");
    }
}
