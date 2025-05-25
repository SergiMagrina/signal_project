package com.cardio_generator.generators;

import java.util.Random;
import com.cardio_generator.outputs.OutputStrategy;

/**
 * The {@code AlertGenerator} class simulates the triggering and resolution of alerts
 * (e.g., emergency button presses) for a set of patients.
 * <p>
 * Each patient can be in one of two alert states:
 * <ul>
 *   <li><b>true</b> – an alert is currently active</li>
 *   <li><b>false</b> – no active alert (resolved)</li>
 * </ul>
 * The generator uses a random model to simulate alerts being triggered or resolved over time.
 */
public class AlertGenerator implements PatientDataGenerator {

    /**
     * Random number generator used for simulating stochastic behavior.
     */
    public static Random randomGenerator = new Random();

    /**
     * Tracks the current alert state of each patient.
     * <p>
     * {@code true} indicates an active alert; {@code false} indicates no alert.
     * Indexed by patient ID.
     */
    private boolean[] alertStates;

    /**
     * Constructs an {@code AlertGenerator} for a given number of patients.
     * Initializes all patients with alert state set to resolved (false).
     *
     * @param patientCount the number of patients to monitor (IDs assumed to start at 1)
     */
    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1];
    }

    /**
     * Simulates the alert status for a specific patient and outputs the result using the
     * provided {@link OutputStrategy}.
     * <p>
     * - If an alert is currently active, there is a 90% chance it resolves in the current step.
     * - If no alert is active, there is a small chance (based on a Poisson process with λ = 0.1)
     *   that a new alert is triggered.
     *
     * @param patientId      the ID of the patient to generate data for (must be 1 or higher)
     * @param outputStrategy the output strategy to send the alert data
     * @throws IllegalArgumentException if {@code patientId} is out of bounds
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (alertStates[patientId]) {
                // Attempt to resolve alert with 90% probability
                if (randomGenerator.nextDouble() < 0.9) {
                    alertStates[patientId] = false;
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                // Use Poisson process to determine alert triggering
                double lambda = 0.1; // Average number of alerts per time period
                double p = -Math.expm1(-lambda); // P(at least one event in time interval)
                boolean alertTriggered = randomGenerator.nextDouble() < p;

                if (alertTriggered) {
                    alertStates[patientId] = true;
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
