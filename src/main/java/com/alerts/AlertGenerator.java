package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;
import java.util.List;
import com.data_management.DataStorage;
import java.util.stream.Collectors;

public class AlertGenerator {

    private final DataStorage dataStorage;

    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    public void evaluateData(Patient patient) {
        List<PatientRecord> records = dataStorage.getRecords(patient.getPatientId(), 0, Long.MAX_VALUE);

        checkBloodSaturationAlerts(records, patient);
        checkCombinedAlerts(records, patient);
        checkECGAlerts(records, patient);
        // You can add blood pressure alerts similarly, omitted here for brevity.
    }

    private void checkBloodSaturationAlerts(List<PatientRecord> records, Patient patient) {
        List<PatientRecord> saturationRecords = records.stream()
                .filter(r -> "Blood Saturation".equals(r.getRecordType()))
                .collect(Collectors.toList());

        for (int i = 0; i < saturationRecords.size(); i++) {
            PatientRecord current = saturationRecords.get(i);
            double currentSat = current.getMeasurementValue();

            if (currentSat < 92) {
                Alert alert = new Alert(patient.getPatientId(), "Low Saturation Alert", current.getTimestamp());
                triggerAlert(alert);
            }

            long currentTime = current.getTimestamp();
            for (int j = i - 1; j >= 0; j--) {
                PatientRecord earlier = saturationRecords.get(j);
                long earlierTime = earlier.getTimestamp();
                if (currentTime - earlierTime <= 10 * 60 * 1000) {
                    if (earlier.getMeasurementValue() - currentSat >= 5) {
                        Alert alert = new Alert(patient.getPatientId(), "Rapid Drop Alert", current.getTimestamp());
                        triggerAlert(alert);
                        break;
                    }
                } else {
                    break;
                }
            }
        }
    }

    private void checkCombinedAlerts(List<PatientRecord> records, Patient patient) {
        PatientRecord latestBP = records.stream()
                .filter(r -> "Blood Pressure".equals(r.getRecordType()))
                .reduce((first, second) -> second)
                .orElse(null);

        PatientRecord latestSat = records.stream()
                .filter(r -> "Blood Saturation".equals(r.getRecordType()))
                .reduce((first, second) -> second)
                .orElse(null);

        if (latestBP != null && latestSat != null) {
            // Assuming your PatientRecord has a method getSystolicValue() for BP
            if (latestBP.getMeasurementValue() < 90 && latestSat.getMeasurementValue() < 92) {
                Alert alert = new Alert(patient.getPatientId(), "Hypotensive Hypoxemia Alert", Math.max(latestBP.getTimestamp(), latestSat.getTimestamp()));
                triggerAlert(alert);
            }
        }
    }

    private void checkECGAlerts(List<PatientRecord> records, Patient patient) {
        List<PatientRecord> ecgRecords = records.stream()
                .filter(r -> "ECG".equals(r.getRecordType()))
                .collect(Collectors.toList());

        if (ecgRecords.size() < 5) return;

        for (int i = 4; i < ecgRecords.size(); i++) {
            double sum = 0;
            for (int j = i - 4; j <= i; j++) {
                sum += ecgRecords.get(j).getMeasurementValue();
            }
            double average = sum / 5;
            PatientRecord current = ecgRecords.get(i);

            if (current.getMeasurementValue() > average * 1.5) {
                Alert alert = new Alert(patient.getPatientId(), "ECG Abnormal Peak Alert", current.getTimestamp());
                triggerAlert(alert);
            }
        }
    }

    protected void triggerAlert(Alert alert) {
        // Example action, e.g., print, log, or notify
        System.out.println("ALERT: " + alert.getCondition() + " for patient " + alert.getPatientId() + " at " + alert.getTimestamp());
    }

}
