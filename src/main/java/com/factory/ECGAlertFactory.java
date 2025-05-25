package com.factory;

import com.alerts.Alert;

public class ECGAlertFactory extends AlertFactory {
    @Override
    public Alert createAlert(int patientId, String condition, long timestamp) {
        return new ECGAlert(patientId, condition, timestamp);
    }
}
