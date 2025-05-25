
package java.Decorator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.Decorator.Alert;



public class AlertTest {

    @Test
    public void testBasicAlertProperties() {
        Alert alert = new BasicAlert(1, "TestCondition", 1000L);
        assertEquals(1, alert.getPatientId());
        assertEquals("TestCondition", alert.getCondition());
        assertEquals(1000L, alert.getTimestamp());
    }
}
