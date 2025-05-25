package factory;

import static org.junit.jupiter.api.Assertions.*;

import com.alerts.Alert;
import org.junit.jupiter.api.Test;

import com.factory.BloodOxygenAlertFactory;
import com.factory.BloodOxygenAlert;

public class BloodOxygenAlertFactoryTest {

    @Test
    public void testCreateAlert() {
        BloodOxygenAlertFactory factory = new BloodOxygenAlertFactory();

        int patientId = 101;
        String condition = "Hypoxia";
        long timestamp = System.currentTimeMillis();

        Alert alert = factory.createAlert(patientId, condition, timestamp);

        assertNotNull(alert, "Alert should not be null");
        assertEquals(patientId, alert.getPatientId());
        assertEquals(condition, alert.getCondition());
        assertEquals(timestamp, alert.getTimestamp());

        // Optional: check the actual class of the returned object
        assertEquals(BloodOxygenAlert.class, alert.getClass());
    }
}
