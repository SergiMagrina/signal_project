package generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import com.cardio_generator.outputs.OutputStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.cardio_generator.generators.BloodLevelsDataGenerator;

public class BloodLevelsDataGeneratorTest {

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

    BloodLevelsDataGenerator generator;
    TestOutputStrategy outputStrategy;

    @BeforeEach
    void setup() {
        generator = new BloodLevelsDataGenerator(3);
        outputStrategy = new TestOutputStrategy();
    }

    @Test
    void testGenerateOutputsThreeMeasurements() {
        generator.generate(1, outputStrategy);

        // Should produce exactly 3 output calls per generate()
        assertEquals(3, outputStrategy.calls.size());

        // Check data types
        boolean hasCholesterol = outputStrategy.calls.stream().anyMatch(c -> "Cholesterol".equals(c.dataType));
        boolean hasWhiteCells = outputStrategy.calls.stream().anyMatch(c -> "WhiteBloodCells".equals(c.dataType));
        boolean hasRedCells = outputStrategy.calls.stream().anyMatch(c -> "RedBloodCells".equals(c.dataType));

        assertTrue(hasCholesterol);
        assertTrue(hasWhiteCells);
        assertTrue(hasRedCells);
    }

    @Test
    void testGenerateForDifferentPatients() {
        generator.generate(1, outputStrategy);
        generator.generate(2, outputStrategy);

        // For two calls, total output calls should be 6 (3 per patient)
        assertEquals(6, outputStrategy.calls.size());

        // Check that patient IDs are correct
        boolean allPatientsValid = outputStrategy.calls.stream()
                .allMatch(c -> c.patientId == 1 || c.patientId == 2);

        assertTrue(allPatientsValid);
    }

    @Test
    void testGeneratedValuesWithinExpectedRange() {
        generator.generate(1, outputStrategy);

        for (OutputCall call : outputStrategy.calls) {
            double value = Double.parseDouble(call.value);

            switch (call.dataType) {
                case "Cholesterol":
                    // Baseline between 150 and 200 + variation of +-5 (half of +-10)
                    assertTrue(value >= 145 && value <= 205, "Cholesterol out of expected range: " + value);
                    break;
                case "WhiteBloodCells":
                    // Baseline between 4 and 10 + variation of +-0.5 (half of +-1)
                    assertTrue(value >= 3.5 && value <= 10.5, "WhiteBloodCells out of expected range: " + value);
                    break;
                case "RedBloodCells":
                    // Baseline between 4.5 and 6 + variation of +-0.1 (half of +-0.2)
                    assertTrue(value >= 4.4 && value <= 6.1, "RedBloodCells out of expected range: " + value);
                    break;
                default:
                    fail("Unexpected data type: " + call.dataType);
            }
        }
    }

    @Test
    void testGenerateWithInvalidPatientIdDoesNotThrow() {
        assertDoesNotThrow(() -> {
            // Patient id 0 or negative is invalid but no explicit check, just see no exceptions
            generator.generate(0, outputStrategy);
            generator.generate(-1, outputStrategy);
            // Also test patient ID beyond initial count
            generator.generate(10, outputStrategy);
        });
    }
}

