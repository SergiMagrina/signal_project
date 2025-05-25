package com.decorator;

public interface Alert {
    int getPatientId();
    String getCondition();
    long getTimestamp();
    void trigger();
}

