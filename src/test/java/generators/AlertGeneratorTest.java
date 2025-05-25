package generators;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import com.cardio_generator.outputs.OutputStrategy;
import java.util.Random;
import com.cardio_generator.generators.AlertGenerator;

class AlertGeneratorTest {

    private AlertGenerator alertGenerator;
    private TestOutputStrategy testOutput;

    // Helper output strategy that captures the last output call
    static class TestOutputStrategy implements OutputStrategy {
        int patientId;
        long timestamp;
        String type;
        String status;

        @Override
        public void output(int patientId, long timestamp, String type, String status) {
            this.patientId = patientId;
            this.timestamp = timestamp;
            this.type = type;
            this.status = status;
        }
    }

    // Custom Random that returns fixed doubles for deterministic testing
    static class FixedRandom extends Random {
        private final double[] values;
        private int index = 0;

        FixedRandom(double... values) {
            this.values = values;
        }

        @Override
        public double nextDouble() {
            double val = values[index % values.length];
            index++;
            return val;
        }
    }

    @BeforeEach
    void setUp() {
        alertGenerator = new AlertGenerator(3);
        testOutput = new TestOutputStrategy();
    }

    @Test
    void testAlertResolves_whenAlertIsActiveAndRandomLessThanPoint9() {
        // Set alert active for patient 1
        setAlertState(alertGenerator, 1, true);

        // Replace randomGenerator with one that returns 0.5 (<0.9) => alert resolves
        AlertGenerator.randomGenerator = new FixedRandom(0.5);

        alertGenerator.generate(1, testOutput);

        assertFalse(getAlertState(alertGenerator, 1), "Alert should be resolved (false)");
        assertEquals(1, testOutput.patientId);
        assertEquals("Alert", testOutput.type);
        assertEquals("resolved", testOutput.status);
    }

    @Test
    void testAlertDoesNotResolve_whenAlertIsActiveAndRandomGreaterThanPoint9() {
        // Set alert active for patient 2
        setAlertState(alertGenerator, 2, true);

        // Replace randomGenerator with one that returns 0.95 (>0.9) => alert does not resolve
        AlertGenerator.randomGenerator = new FixedRandom(0.95);

        alertGenerator.generate(2, testOutput);

        assertTrue(getAlertState(alertGenerator, 2), "Alert should still be active (true)");
        assertNull(testOutput.type, "No output should be produced because alert did not resolve");
    }

    @Test
    void testAlertTriggered_whenNoAlertAndRandomLessThanThreshold() {
        // alertStates default false, patient 1 no alert

        // p = 1 - e^(-0.1) ~ 0.095 -> use 0.05 < 0.095 to trigger alert
        AlertGenerator.randomGenerator = new FixedRandom(0.05);

        alertGenerator.generate(1, testOutput);

        assertTrue(getAlertState(alertGenerator, 1), "Alert should be triggered (true)");
        assertEquals("Alert", testOutput.type);
        assertEquals("triggered", testOutput.status);
    }

    @Test
    void testAlertNotTriggered_whenNoAlertAndRandomGreaterThanThreshold() {
        // alertStates default false, patient 2 no alert

        // Use 0.2 > 0.095 (threshold), alert should not trigger
        AlertGenerator.randomGenerator = new FixedRandom(0.2);

        alertGenerator.generate(2, testOutput);

        assertFalse(getAlertState(alertGenerator, 2), "Alert should NOT be triggered (false)");
        assertNull(testOutput.type, "No output should be produced because alert not triggered");
    }

    // --- Helper methods to access private alertStates array ---

    private void setAlertState(AlertGenerator generator, int patientId, boolean state) {
        try {
            var field = AlertGenerator.class.getDeclaredField("alertStates");
            field.setAccessible(true);
            boolean[] alertStates = (boolean[]) field.get(generator);
            alertStates[patientId] = state;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean getAlertState(AlertGenerator generator, int patientId) {
        try {
            var field = AlertGenerator.class.getDeclaredField("alertStates");
            field.setAccessible(true);
            boolean[] alertStates = (boolean[]) field.get(generator);
            return alertStates[patientId];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

