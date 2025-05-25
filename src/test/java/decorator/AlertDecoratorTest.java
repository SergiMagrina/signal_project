package decorator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.decorator.AlertDecorator;
import com.decorator.Alert;

public class AlertDecoratorTest {

    @Test
    void testDelegatedMethods() {
        Alert alert = new Alert() {
            @Override public int getPatientId() { return 1; }
            @Override public String getCondition() { return "TestCondition"; }
            @Override public long getTimestamp() { return 123456L; }
            @Override public void trigger() { triggered = true; }

            boolean triggered = false;
        };

        AlertDecorator decorator = new AlertDecorator(alert) {}; // anonymous subclass

        assertEquals(1, decorator.getPatientId());
        assertEquals("TestCondition", decorator.getCondition());
        assertEquals(123456L, decorator.getTimestamp());
    }

    @Test
    void testTriggerDelegation() {
        class TestAlert implements Alert {
            boolean triggered = false;

            @Override public int getPatientId() { return 2; }
            @Override public String getCondition() { return "AlertCondition"; }
            @Override public long getTimestamp() { return 789L; }
            @Override public void trigger() { triggered = true; }
        }

        TestAlert testAlert = new TestAlert();
        AlertDecorator decorator = new AlertDecorator(testAlert) {};

        assertFalse(testAlert.triggered);
        decorator.trigger();
        assertTrue(testAlert.triggered);
    }
}

