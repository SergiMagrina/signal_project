package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Interface for generating patient health data
 */
public interface PatientDataGenerator {

    /**
     * Generates data for a specific patient and outputs it using the given strategy
     * @param patientId The ID of the patient
     * @param outputStrategy The strategy used to output the data
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
