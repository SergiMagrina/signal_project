package com.cardio_generator.generators;

import java.util.Random;
import com.cardio_generator.outputs.OutputStrategy;

/**
 * The {@code BloodSaturationDataGenerator} class generates simulated blood oxygen saturation
 * (SpO2) data for a set of patients. Each patient has a saturation value that fluctuates slightly
 * over time to mimic real physiological readings.
 * <p>
 * The saturation values are initialized to values between 95% and 100% and constrained to remain
 * within a realistic range (90% to 100%).
 */
public class BloodSaturationDataGenerator implements PatientDataGenerator {

    /**
     * A shared {@link Random} instance used for generating random values.
     */
    private static final Random random = new Random();

    /**
     * An array storing the last generated saturation value for each patient.
     * Indexed by patient ID.
     */
    private int[] lastSaturationValues;

    /**
     * Constructs a {@code BloodSaturationDataGenerator} for a given number of patients.
     * Initializes each patient's saturation to a random value between 95% and 100%.
     *
     * @param patientCount the number of patients to simulate (IDs assumed to start at 1)
     */
    public BloodSaturationDataGenerator(int patientCount) {
        lastSaturationValues = new int[patientCount + 1];

        // Initialize with baseline saturation values
        for (int i = 1; i <= patientCount; i++) {
            lastSaturationValues[i] = 95 + random.nextInt(6); // Value between 95 and 100
        }
    }

    /**
     * Generates a new blood saturation value for the specified patient and sends it using the
     * provided {@link OutputStrategy}.
     * <p>
     * The new value is computed by applying a small variation (-1, 0, or +1) to the last value,
     * and then clamping the result to the range 90–100%.
     *
     * @param patientId      the ID of the patient for whom to generate data (must be 1 or higher)
     * @param outputStrategy the output strategy used to send the generated data
     * @throws IllegalArgumentException if {@code patientId} is out of bounds
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            int variation = random.nextInt(3) - 1; // -1, 0, or +1
            int newSaturationValue = lastSaturationValues[patientId] + variation;

            // Clamp to range [90, 100]
            newSaturationValue = Math.min(Math.max(newSaturationValue, 90), 100);

            // Update last value and send output
            lastSaturationValues[patientId] = newSaturationValue;
            outputStrategy.output(patientId, System.currentTimeMillis(), "Saturation",
                    newSaturationValue + "%");

        } catch (Exception e) {
            System.err.println("An error occurred while generating blood saturation data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
