package com.Strategy;

public interface AlertStrategy {
    /**
     * Checks patient records and triggers alerts if conditions are met.
     *
     * @param patientId The ID of the patient
     * @param records List of patient records relevant to the strategy
     */
    void checkAlert(int patientId, java.util.List<com.data_management.PatientRecord> records);
}

