
package java.Decorator;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;
import com.Decorator.Alert;

public class RepeatedAlertDecoratorTest {

    @Test
    public void testRepeatedTriggering() throws InterruptedException {
        Alert base = new BasicAlert(4, "Irregular ECG", 4000L);
        Alert repeated = new RepeatedAlertDecorator(base, 3, 100);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        repeated.trigger();
        Thread.sleep(350); // wait for 3 triggers

        String result = out.toString();
        int occurrences = result.split("Repeated alert for patient 4", -1).length - 1;

        assertTrue(result.contains("Basic Alert triggered for patient 4"));
        assertTrue(occurrences >= 2);

        System.setOut(System.out);
    }
}
