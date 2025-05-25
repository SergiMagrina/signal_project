package strategy;

import com.data_management.PatientRecord;
import com.alerts.Alert;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.strategy.HeartRateStrategy;

import static org.junit.jupiter.api.Assertions.*;

public class HeartRateStrategyTest {

    private HeartRateStrategy strategy;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        strategy = new HeartRateStrategy();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testNoAlertsWhenNoHeartRateRecords() {
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(1, 80, "Blood Pressure", 1000L));  // Non-heart rate record

        strategy.checkAlert(1, records);

        assertEquals("", outContent.toString().trim(), "No alerts should be printed");
    }

    @Test
    public void testNoAlertsWhenHeartRateNormal() {
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(1, 75, "Heart Rate", 1000L));  // Normal heart rate

        strategy.checkAlert(1, records);

        assertEquals("", outContent.toString().trim(), "No alerts should be printed");
    }

    @Test
    public void testAlertTriggeredForLowHeartRate() {
        int patientId = 1;
        long timestamp = 1000L;
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(patientId, 45, "Heart Rate", timestamp));  // Low heart rate

        strategy.checkAlert(patientId, records);

        String output = outContent.toString().trim();
        assertTrue(output.contains("ALERT: Abnormal Heart Rate"), "Alert message should contain condition");
        assertTrue(output.contains("patient " + patientId), "Alert message should contain patient ID");
        assertTrue(output.contains(String.valueOf(timestamp)), "Alert message should contain timestamp");
    }

    @Test
    public void testAlertTriggeredForHighHeartRate() {
        int patientId = 2;
        long timestamp = 2000L;
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(patientId, 110, "Heart Rate", timestamp));  // High heart rate

        strategy.checkAlert(patientId, records);

        String output = outContent.toString().trim();
        assertTrue(output.contains("ALERT: Abnormal Heart Rate"), "Alert message should contain condition");
        assertTrue(output.contains("patient " + patientId), "Alert message should contain patient ID");
        assertTrue(output.contains(String.valueOf(timestamp)), "Alert message should contain timestamp");
    }
}

