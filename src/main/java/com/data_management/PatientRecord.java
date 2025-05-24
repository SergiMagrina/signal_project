package com.data_management;

public class PatientRecord {
    private int patientId;
    private String recordType; // Example: "ECG", "Blood Pressure", "Blood Saturation"

    // For general measurement (heart rate, saturation, ECG peak)
    private double measurementValue;

    // For blood pressure only
    private double systolicValue;   // use 0 if not applicable
    private double diastolicValue;  // use 0 if not applicable

    private long timestamp;

    // Constructor for general record (non-BP)
    public PatientRecord(int patientId, double measurementValue, String recordType, long timestamp) {
        this.patientId = patientId;
        this.measurementValue = measurementValue;
        this.recordType = recordType;
        this.timestamp = timestamp;
        this.systolicValue = 0;
        this.diastolicValue = 0;
    }

    // Overloaded constructor for blood pressure records
    public PatientRecord(int patientId, double systolicValue, double diastolicValue, long timestamp) {
        this.patientId = patientId;
        this.recordType = "Blood Pressure";
        this.systolicValue = systolicValue;
        this.diastolicValue = diastolicValue;
        this.timestamp = timestamp;
        this.measurementValue = 0;
    }

    // Getters
    public int getPatientId() {
        return patientId;
    }

    public double getMeasurementValue() {
        return measurementValue;
    }

    public double getSystolicValue() {
        return systolicValue;
    }

    public double getDiastolicValue() {
        return diastolicValue;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getRecordType() {
        return recordType;
    }
}

