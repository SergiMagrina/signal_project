package factory;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.factory.ECGAlert;

public class ECGAlertTest {

    @Test
    public void testECGAlertCreation() {
        int patientId = 123;
        String condition = "Irregular heartbeat";
        long timestamp = System.currentTimeMillis();

        ECGAlert alert = new ECGAlert(patientId, condition, timestamp);

        assertNotNull(alert, "Alert should not be null");
        assertEquals(patientId, alert.getPatientId(), "Patient ID should match");
        assertEquals(condition, alert.getCondition(), "Condition should match");
        assertEquals(timestamp, alert.getTimestamp(), "Timestamp should match");
    }
}

