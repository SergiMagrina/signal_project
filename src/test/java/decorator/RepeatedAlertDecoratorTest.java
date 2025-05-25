package decorator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicInteger;

import com.decorator.Alert;
import com.decorator.RepeatedAlertDecorator;

public class RepeatedAlertDecoratorTest {

    @Test
    void testTriggerRepeatsCorrectNumberOfTimes() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);

        Alert alert = new Alert() {
            @Override public int getPatientId() { return 123; }
            @Override public String getCondition() { return "Alert!"; }
            @Override public long getTimestamp() { return 9999L; }
            @Override public void trigger() { counter.incrementAndGet(); }
        };

        int repeatCount = 3;
        long interval = 100; // 100 ms

        RepeatedAlertDecorator decorator = new RepeatedAlertDecorator(alert, repeatCount, interval);
        decorator.trigger();

        // Wait enough time for all repeats (repeatCount * interval + buffer)
        Thread.sleep(repeatCount * interval + 200);

        assertEquals(repeatCount, counter.get(), "Trigger should be called repeatCount times");
    }

    @Test
    void testDelegatedMethods() {
        Alert alert = new Alert() {
            @Override public int getPatientId() { return 5; }
            @Override public String getCondition() { return "OK"; }
            @Override public long getTimestamp() { return 1234L; }
            @Override public void trigger() {}
        };

        RepeatedAlertDecorator decorator = new RepeatedAlertDecorator(alert, 1, 1000);

        assertEquals(5, decorator.getPatientId());
        assertEquals("OK", decorator.getCondition());
        assertEquals(1234L, decorator.getTimestamp());
    }
}
