
package com.Decorator;

public class BasicAlert implements Alert {
    private final int patientId;
    private final String condition;
    private final long timestamp;

    public BasicAlert(int patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
    }

    @Override
    public int getPatientId() {
        return patientId;
    }

    @Override
    public String getCondition() {
        return condition;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public void trigger() {
        System.out.println("Basic Alert triggered for patient " + patientId);
    }
}
