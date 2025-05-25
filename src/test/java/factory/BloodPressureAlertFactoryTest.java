package factory;

import static org.junit.jupiter.api.Assertions.*;

import com.alerts.Alert;
import org.junit.jupiter.api.Test;

import com.factory.BloodPressureAlertFactory;
import com.factory.BloodPressureAlert;

public class BloodPressureAlertFactoryTest {

    @Test
    public void testCreateAlert() {
        BloodPressureAlertFactory factory = new BloodPressureAlertFactory();

        int patientId = 456;
        String condition = "Hypertension";
        long timestamp = System.currentTimeMillis();

        Alert alert = factory.createAlert(patientId, condition, timestamp);

        assertNotNull(alert, "Created alert should not be null");
        assertEquals(patientId, alert.getPatientId(), "Patient ID should match");
        assertEquals(condition, alert.getCondition(), "Condition should match");
        assertEquals(timestamp, alert.getTimestamp(), "Timestamp should match");
        assertTrue(alert instanceof BloodPressureAlert, "Alert should be an instance of BloodPressureAlert");
    }
}
