package com.cardio_generator.outputs;

/**
 * Strategy interface for outputting patient health data
 */
public interface OutputStrategy {

    /**
     * Outputs data for a specific patient
     * @param patientId The ID of the patient
     * @param timestamp The timestamp of the data
     * @param label The label describing the type of data
     * @param data The data to be output
     */
    void output(int patientId, long timestamp, String label, String data);
}
