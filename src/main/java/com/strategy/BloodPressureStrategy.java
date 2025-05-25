package com.strategy;

import java.util.List;
import com.data_management.PatientRecord;
import com.alerts.Alert;
import com.factory.BloodPressureAlert;
import java.util.stream.Collectors;

public class BloodPressureStrategy implements AlertStrategy {

    @Override
    public void checkAlert(int patientId, List<PatientRecord> records) {
        // Filter blood pressure records
        List<PatientRecord> bpRecords = records.stream()
                .filter(r -> "Blood Pressure".equals(r.getRecordType()))
                .collect(Collectors.toList());

        for (PatientRecord record : bpRecords) {
            if (record.getMeasurementValue() < 90) {
                Alert alert = new BloodPressureAlert(patientId, "Low Blood Pressure", record.getTimestamp());
                triggerAlert(alert);
            }
        }
    }

    private void triggerAlert(Alert alert) {
        System.out.println("ALERT: " + alert.getCondition() + " for patient " + alert.getPatientId() + " at " + alert.getTimestamp());
    }
}

