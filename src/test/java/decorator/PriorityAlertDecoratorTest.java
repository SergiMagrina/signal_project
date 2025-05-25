package decorator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import com.decorator.Alert;
import com.decorator.PriorityAlertDecorator;

public class PriorityAlertDecoratorTest {

    @Test
    void testTriggerPrintsPriorityAndDelegates() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(output));

        final boolean[] triggered = {false};

        Alert alert = new Alert() {
            @Override public int getPatientId() { return 1; }
            @Override public String getCondition() { return "Normal"; }
            @Override public long getTimestamp() { return 1000L; }
            @Override public void trigger() { triggered[0] = true; }
        };

        PriorityAlertDecorator decorator = new PriorityAlertDecorator(alert, "HIGH");
        decorator.trigger();

        System.setOut(originalOut);

        String consoleOutput = output.toString().trim();
        assertTrue(consoleOutput.contains("Priority Level: HIGH"), "Should print priority level");
        assertTrue(triggered[0], "Original trigger() should be called");
    }

    @Test
    void testGetConditionIncludesPriority() {
        Alert alert = new Alert() {
            @Override public int getPatientId() { return 2; }
            @Override public String getCondition() { return "Critical"; }
            @Override public long getTimestamp() { return 2000L; }
            @Override public void trigger() {}
        };

        PriorityAlertDecorator decorator = new PriorityAlertDecorator(alert, "URGENT");
        assertEquals("[URGENT] Critical", decorator.getCondition());
    }

    @Test
    void testDelegatedMethods() {
        Alert alert = new Alert() {
            @Override public int getPatientId() { return 42; }
            @Override public String getCondition() { return "Stable"; }
            @Override public long getTimestamp() { return 999L; }
            @Override public void trigger() {}
        };

        PriorityAlertDecorator decorator = new PriorityAlertDecorator(alert, "LOW");

        assertEquals(42, decorator.getPatientId());
        assertEquals(999L, decorator.getTimestamp());
    }
}

