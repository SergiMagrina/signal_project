package com.Decorator;

public interface Alert {
    int getPatientId();
    String getCondition();
    long getTimestamp();
    void trigger();
}

