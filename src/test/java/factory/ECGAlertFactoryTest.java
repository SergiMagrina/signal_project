package factory;

import static org.junit.jupiter.api.Assertions.*;

import com.alerts.Alert;
import org.junit.jupiter.api.Test;

import com.factory.ECGAlert;
import com.factory.ECGAlertFactory;

public class ECGAlertFactoryTest {

    @Test
    public void testCreateAlert() {
        ECGAlertFactory factory = new ECGAlertFactory();

        int patientId = 101;
        String condition = "Arrhythmia";
        long timestamp = System.currentTimeMillis();

        Alert alert = factory.createAlert(patientId, condition, timestamp);

        assertNotNull(alert, "Created alert should not be null");
        assertEquals(patientId, alert.getPatientId(), "Patient ID should match");
        assertEquals(condition, alert.getCondition(), "Condition should match");
        assertEquals(timestamp, alert.getTimestamp(), "Timestamp should match");

        // Optional: check that the returned alert is an instance of ECGAlert
        assertTrue(alert instanceof ECGAlert, "Alert should be instance of ECGAlert");
    }
}

