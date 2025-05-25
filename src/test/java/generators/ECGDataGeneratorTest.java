package generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import com.cardio_generator.outputs.OutputStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cardio_generator.generators.ECGDataGenerator;

public class ECGDataGeneratorTest {

    static class OutputCall {
        int patientId;
        String dataType;
        String value;

        OutputCall(int patientId, long timestamp, String dataType, String value) {
            this.patientId = patientId;
            this.dataType = dataType;
            this.value = value;
        }
    }

    static class TestOutputStrategy implements OutputStrategy {
        List<OutputCall> calls = new ArrayList<>();

        @Override
        public void output(int patientId, long timestamp, String dataType, String value) {
            calls.add(new OutputCall(patientId, timestamp, dataType, value));
        }
    }

    ECGDataGenerator generator;
    TestOutputStrategy outputStrategy;

    @BeforeEach
    void setup() {
        generator = new ECGDataGenerator(3);
        outputStrategy = new TestOutputStrategy();
    }

    @Test
    void testGenerateProducesOneOutput() {
        generator.generate(1, outputStrategy);
        assertEquals(1, outputStrategy.calls.size());
    }

    @Test
    void testOutputPatientIdAndDataType() {
        generator.generate(2, outputStrategy);
        OutputCall call = outputStrategy.calls.get(0);

        assertEquals(2, call.patientId);
        assertEquals("ECG", call.dataType);
    }

    @Test
    void testOutputValueIsParsableDouble() {
        generator.generate(1, outputStrategy);
        OutputCall call = outputStrategy.calls.get(0);

        assertDoesNotThrow(() -> Double.parseDouble(call.value));
    }

    @Test
    void testOutputValueChangesAcrossCalls() throws InterruptedException {
        generator.generate(1, outputStrategy);
        double firstValue = Double.parseDouble(outputStrategy.calls.get(0).value);

        // Wait a short time so time-dependent value changes
        Thread.sleep(10);

        outputStrategy.calls.clear();
        generator.generate(1, outputStrategy);
        double secondValue = Double.parseDouble(outputStrategy.calls.get(0).value);

        // There's randomness and time component, so values should differ in general
        assertNotEquals(firstValue, secondValue);
    }

    @Test
    void testGenerateWithInvalidPatientIdDoesNotThrow() {
        assertDoesNotThrow(() -> {
            generator.generate(0, outputStrategy);
            generator.generate(-1, outputStrategy);
            generator.generate(10, outputStrategy);
        });
    }
}
