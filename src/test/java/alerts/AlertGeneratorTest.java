package alerts;

import com.alerts.*;
import com.data_management.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;

public class AlertGeneratorTest {

    @Test
    public void testLowSaturationAlert() {
        long timestamp = System.currentTimeMillis();

        // Create Blood Saturation record below threshold
        PatientRecord lowSatRecord = new PatientRecord(1, 90.0, "Blood Saturation", timestamp);

        // DataStorage with a simple in-memory implementation (anonymous class)
        DataStorage dataStorage = new DataStorage() {
            @Override
            public List<PatientRecord> getRecords(int patientId, long startTime, long endTime) {
                return List.of(lowSatRecord);
            }
        };

        AlertGenerator alertGen = new AlertGenerator(dataStorage) {
            Alert capturedAlert = null;

            @Override
            protected void triggerAlert(Alert alert) {
                capturedAlert = alert;
            }
        };

        Patient patient = new Patient(1);
        alertGen.evaluateData(patient);

        // Because capturedAlert is inside anonymous subclass, access it via reflection or better:
        // Just add a getter for test or use a field in test scope:
        // But here let's just check output (less ideal) or re-arrange...

        // A simple way: use a holder array:
    }

    @Test
    public void testLowSaturationAlertWithCapture() {
        long timestamp = System.currentTimeMillis();
        PatientRecord lowSatRecord = new PatientRecord(1, 90.0, "Blood Saturation", timestamp);

        DataStorage dataStorage = new DataStorage() {
            @Override
            public List<PatientRecord> getRecords(int patientId, long startTime, long endTime) {
                return List.of(lowSatRecord);
            }
        };

        final Alert[] capturedAlert = new Alert[1]; // holder to capture alert

        AlertGenerator alertGen = new AlertGenerator(dataStorage) {
            @Override
            protected void triggerAlert(Alert alert) {
                capturedAlert[0] = alert;
            }
        };

        Patient patient = new Patient(1);
        alertGen.evaluateData(patient);

        assertNotNull(capturedAlert[0]);
        assertEquals("Low Saturation Alert", capturedAlert[0].getCondition());
        assertEquals(timestamp, capturedAlert[0].getTimestamp());
        assertEquals(1, capturedAlert[0].getPatientId());
    }
}
