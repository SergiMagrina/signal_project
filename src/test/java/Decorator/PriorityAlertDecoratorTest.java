
package com.Decorator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class PriorityAlertDecoratorTest {

    @Test
    public void testPriorityConditionFormatting() {
        Alert base = new BasicAlert(3, "Low Oxygen", 3000L);
        Alert priorityAlert = new PriorityAlertDecorator(base, "HIGH");

        assertEquals("[HIGH] Low Oxygen", priorityAlert.getCondition());
    }

    @Test
    public void testPriorityTriggerOutput() {
        Alert base = new BasicAlert(3, "Low Oxygen", 3000L);
        Alert priorityAlert = new PriorityAlertDecorator(base, "CRITICAL");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        priorityAlert.trigger();

        String output = out.toString();
        assertTrue(output.contains("Priority Level: CRITICAL"));
        assertTrue(output.contains("Basic Alert triggered for patient 3"));

        System.setOut(System.out);
    }
}
