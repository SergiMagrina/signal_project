package generators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import com.cardio_generator.outputs.OutputStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cardio_generator.generators.BloodSaturationDataGenerator;

public class BloodSaturationDataGeneratorTest {

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

    BloodSaturationDataGenerator generator;
    TestOutputStrategy outputStrategy;

    @BeforeEach
    void setup() {
        generator = new BloodSaturationDataGenerator(3);
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
        assertEquals("Saturation", call.dataType);
        assertTrue(call.value.endsWith("%"));
    }

    @Test
    void testOutputValueInRange() {
        // Run multiple times to cover variations
        for (int i = 0; i < 50; i++) {
            outputStrategy.calls.clear();
            generator.generate(1, outputStrategy);
            OutputCall call = outputStrategy.calls.get(0);

            String valStr = call.value.replace("%", "");
            int value = Integer.parseInt(valStr);

            assertTrue(value >= 90 && value <= 100, "Value out of range: " + value);
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

