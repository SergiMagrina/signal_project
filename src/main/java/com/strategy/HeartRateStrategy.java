package com.strategy;

import java.util.List;
import com.data_management.PatientRecord;
import com.alerts.Alert;
import java.util.stream.Collectors;

public class HeartRateStrategy implements AlertStrategy {

    @Override
    public void checkAlert(int patientId, List<PatientRecord> records) {
        // Filter heart rate records
        List<PatientRecord> hrRecords = records.stream()
                .filter(r -> "Heart Rate".equals(r.getRecordType()))
                .collect(Collectors.toList());

        for (PatientRecord record : hrRecords) {
            double hr = record.getMeasurementValue();
            if (hr < 50 || hr > 100) {  // example thresholds
                Alert alert = new Alert(patientId, "Abnormal Heart Rate", record.getTimestamp());
                triggerAlert(alert);
            }
        }
    }

    private void triggerAlert(Alert alert) {
        System.out.println("ALERT: " + alert.getCondition() + " for patient " + alert.getPatientId() + " at " + alert.getTimestamp());
    }
}

