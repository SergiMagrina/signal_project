package com.Factory;

import com.alerts.Alert;

public abstract class AlertFactory {

    public abstract Alert createAlert(int patientId, String condition, long timestamp);
}

