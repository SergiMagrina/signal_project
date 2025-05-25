package factory;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.factory.BloodPressureAlert;

public class BloodPressureAlertTest {

    @Test
    public void testBloodPressureAlertConstructor() {
        int patientId = 123;
        String condition = "High Blood Pressure";
        long timestamp = System.currentTimeMillis();

        BloodPressureAlert alert = new BloodPressureAlert(patientId, condition, timestamp);

        assertNotNull(alert, "Alert object should not be null");
        assertEquals(patientId, alert.getPatientId(), "Patient ID should match");
        assertEquals(condition, alert.getCondition(), "Condition should match");
        assertEquals(timestamp, alert.getTimestamp(), "Timestamp should match");
    }
}
