package data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.data_management.PatientRecord;

public class PatientRecordTest {

    @Test
    public void testGeneralRecordConstructor() {
        int patientId = 101;
        double measurementValue = 98.6;
        String recordType = "ECG";
        long timestamp = 123456789L;

        PatientRecord record = new PatientRecord(patientId, measurementValue, recordType, timestamp);

        assertEquals(patientId, record.getPatientId());
        assertEquals(measurementValue, record.getMeasurementValue());
        assertEquals(recordType, record.getRecordType());
        assertEquals(timestamp, record.getTimestamp());

        // Blood pressure fields should be zero for general records
        assertEquals(0.0, record.getSystolicValue());
        assertEquals(0.0, record.getDiastolicValue());
    }

    @Test
    public void testBloodPressureRecordConstructor() {
        int patientId = 202;
        double systolic = 120;
        double diastolic = 80;
        long timestamp = 987654321L;

        PatientRecord record = new PatientRecord(patientId, systolic, diastolic, timestamp);

        assertEquals(patientId, record.getPatientId());
        assertEquals("Blood Pressure", record.getRecordType());
        assertEquals(timestamp, record.getTimestamp());

        // Blood pressure values should be set correctly
        assertEquals(systolic, record.getSystolicValue());
        assertEquals(diastolic, record.getDiastolicValue());

        // Measurement value should be zero for blood pressure records
        assertEquals(0.0, record.getMeasurementValue());
    }
}

