package generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import com.cardio_generator.outputs.OutputStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cardio_generator.generators.BloodPressureDataGenerator;

public class BloodPressureDataGeneratorTest {

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

    BloodPressureDataGenerator generator;
    TestOutputStrategy outputStrategy;

    @BeforeEach
    void setup() {
        generator = new BloodPressureDataGenerator(3);
        outputStrategy = new TestOutputStrategy();
    }

    @Test
    void testGenerateOutputsSystolicAndDiastolic() {
        generator.generate(1, outputStrategy);

        // Exactly 2 output calls per generate
        assertEquals(2, outputStrategy.calls.size());

        boolean hasSystolic = outputStrategy.calls.stream()
                .anyMatch(c -> "SystolicPressure".equals(c.dataType));
        boolean hasDiastolic = outputStrategy.calls.stream()
                .anyMatch(c -> "DiastolicPressure".equals(c.dataType));

        assertTrue(hasSystolic);
        assertTrue(hasDiastolic);
    }

    @Test
    void testGenerateMultiplePatients() {
        generator.generate(1, outputStrategy);
        generator.generate(2, outputStrategy);

        // 4 output calls total (2 per patient)
        assertEquals(4, outputStrategy.calls.size());

        boolean validPatients = outputStrategy.calls.stream()
                .allMatch(c -> c.patientId == 1 || c.patientId == 2);

        assertTrue(validPatients);
    }

    @Test
    void testValuesWithinExpectedRange() {
        generator.generate(1, outputStrategy);

        for (OutputCall call : outputStrategy.calls) {
            int value = (int) Double.parseDouble(call.value);

            if ("SystolicPressure".equals(call.dataType)) {
                assertTrue(value >= 90 && value <= 180,
                        "Systolic value out of range: " + value);
            } else if ("DiastolicPressure".equals(call.dataType)) {
                assertTrue(value >= 60 && value <= 120,
                        "Diastolic value out of range: " + value);
            } else {
                fail("Unexpected data type: " + call.dataType);
            }
        }
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
