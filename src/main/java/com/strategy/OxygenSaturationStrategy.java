package com.strategy;

import java.util.List;
import com.data_management.PatientRecord;
import com.alerts.Alert;
import java.util.stream.Collectors;

public class OxygenSaturationStrategy implements AlertStrategy {

    @Override
    public void checkAlert(int patientId, List<PatientRecord> records) {
        // Filter oxygen saturation records
        List<PatientRecord> satRecords = records.stream()
                .filter(r -> "Blood Saturation".equals(r.getRecordType()))
                .collect(Collectors.toList());

        for (PatientRecord record : satRecords) {
            if (record.getMeasurementValue() < 92) {
                Alert alert = new Alert(patientId, "Low Oxygen Saturation", record.getTimestamp());
                triggerAlert(alert);
            }
        }
    }

    private void triggerAlert(Alert alert) {
        System.out.println("ALERT: " + alert.getCondition() + " for patient " + alert.getPatientId() + " at " + alert.getTimestamp());
    }
}

