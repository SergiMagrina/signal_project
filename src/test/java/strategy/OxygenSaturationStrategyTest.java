package strategy;

import com.data_management.PatientRecord;
import com.alerts.Alert;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.strategy.OxygenSaturationStrategy;

import static org.junit.jupiter.api.Assertions.*;

public class OxygenSaturationStrategyTest {

    private OxygenSaturationStrategy strategy;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        strategy = new OxygenSaturationStrategy();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testNoAlertsWhenNoBloodSaturationRecords() {
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(1, 100, "Heart Rate", 1000L)); // Non-saturation record

        strategy.checkAlert(1, records);

        assertEquals("", outContent.toString().trim(), "No alerts should be printed");
    }

    @Test
    public void testNoAlertsWhenBloodSaturationNormal() {
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(1, 95, "Blood Saturation", 1000L)); // Normal saturation (>= 92)

        strategy.checkAlert(1, records);

        assertEquals("", outContent.toString().trim(), "No alerts should be printed");
    }

    @Test
    public void testAlertTriggeredForLowOxygenSaturation() {
        int patientId = 1;
        long timestamp = 1000L;
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(patientId, 88, "Blood Saturation", timestamp)); // Low saturation (< 92)

        strategy.checkAlert(patientId, records);

        String output = outContent.toString().trim();
        assertTrue(output.contains("ALERT: Low Oxygen Saturation"), "Alert message should contain condition");
        assertTrue(output.contains("patient " + patientId), "Alert message should contain patient ID");
        assertTrue(output.contains(String.valueOf(timestamp)), "Alert message should contain timestamp");
    }
}

