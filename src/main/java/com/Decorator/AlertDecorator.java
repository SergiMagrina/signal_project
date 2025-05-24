package com.Decorator;

public abstract class AlertDecorator implements Alert {
    protected Alert wrappedAlert;

    public AlertDecorator(Alert alert) {
        this.wrappedAlert = alert;
    }

    @Override
    public int getPatientId() {
        return wrappedAlert.getPatientId();
    }

    @Override
    public String getCondition() {
        return wrappedAlert.getCondition();
    }

    @Override
    public long getTimestamp() {
        return wrappedAlert.getTimestamp();
    }

    @Override
    public void trigger() {
        wrappedAlert.trigger();
    }
}

