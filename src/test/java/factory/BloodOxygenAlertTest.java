package factory;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.factory.BloodOxygenAlert;

public class BloodOxygenAlertTest {

    @Test
    public void testConstructorAndGetters() {
        int patientId = 123;
        String condition = "Low Oxygen";
        long timestamp = System.currentTimeMillis();

        BloodOxygenAlert alert = new BloodOxygenAlert(patientId, condition, timestamp);

        assertEquals(patientId, alert.getPatientId());
        assertEquals(condition, alert.getCondition());
        assertEquals(timestamp, alert.getTimestamp());
    }
}

