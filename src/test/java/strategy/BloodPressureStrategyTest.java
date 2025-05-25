package strategy;

import static org.junit.jupiter.api.Assertions.*;

import com.data_management.PatientRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.ArrayList;

import com.strategy.BloodPressureStrategy;

public class BloodPressureStrategyTest {

    private BloodPressureStrategy strategy;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        strategy = new BloodPressureStrategy();
        // Redirect System.out to capture printed alerts
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testNoAlertsWhenNoBloodPressureRecords() {
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(1, 120, "Heart Rate", 1000L)); // Non-bp record

        strategy.checkAlert(1, records);

        assertEquals("", outContent.toString().trim(), "No alerts should be printed");
    }

    @Test
    public void testNoAlertsWhenBloodPressureNormalOrHigh() {
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(1, 120, "Blood Pressure", 1000L)); // Normal/high BP

        strategy.checkAlert(1, records);

        assertEquals("", outContent.toString().trim(), "No alerts should be printed");
    }

    @Test
    public void testAlertTriggeredForLowBloodPressure() {
        int patientId = 1;
        long timestamp = 1000L;
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(patientId, 85, "Blood Pressure", timestamp)); // Low BP

        strategy.checkAlert(patientId, records);

        String output = outContent.toString().trim();
        assertTrue(output.contains("ALERT: Low Blood Pressure"), "Alert message should contain condition");
        assertTrue(output.contains("patient " + patientId), "Alert message should contain patient ID");
        assertTrue(output.contains(String.valueOf(timestamp)), "Alert message should contain timestamp");
    }

    @BeforeEach
    public void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
    }
}

