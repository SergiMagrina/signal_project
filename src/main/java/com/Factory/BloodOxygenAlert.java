package com.Factory;

import com.alerts.Alert;

public class BloodOxygenAlert extends Alert {
    public BloodOxygenAlert(int patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }
}