package alerts;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.alerts.Alert;

public class AlertTest {

    @Test
    public void testAlertConstructorAndGetters() {
        int patientId = 123;
        String condition = "High Blood Pressure";
        long timestamp = 1651234567890L;

        Alert alert = new Alert(patientId, condition, timestamp);

        assertEquals(patientId, alert.getPatientId());
        assertEquals(condition, alert.getCondition());
        assertEquals(timestamp, alert.getTimestamp());
    }
}

