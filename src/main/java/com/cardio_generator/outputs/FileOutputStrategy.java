package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@code FileOutputStrategy} class implements the {@link OutputStrategy} interface
 * to handle output by writing formatted patient data to individual text files based on labels.
 * <p>
 * Each label (e.g., "HeartRate", "BloodPressure") is associated with a file in the specified base
 * directory. Records are appended to these files in a thread-safe manner using a {@link ConcurrentHashMap}.
 */
public class FileOutputStrategy implements OutputStrategy {

    /**
     * The base directory where all output files will be stored.
     */
    private String baseDirectory;

    /**
     * A thread-safe map to store and reuse file paths based on labels.
     * Keys are labels (e.g., "HeartRate"), values are full file paths.
     */
    private final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();

    /**
     * Constructs a {@code FileOutputStrategy} with a given base directory.
     *
     * @param baseDirectory the directory where output files will be stored.
     *                      If it doesn't exist, it will be created.
     */
    public FileOutputStrategy(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    /**
     * Outputs a patient data entry to a file associated with the provided label.
     * <p>
     * If the file doesn't exist, it will be created. If it exists, the method appends
     * the new data to the file.
     *
     * @param patientId the unique ID of the patient
     * @param timestamp the timestamp of the data in milliseconds since UNIX epoch
     * @param label     the category or type of the data (e.g., "HeartRate")
     * @param data      the actual measurement or data string to write
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Ensure the base directory exists
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }

        // Compute or retrieve the file path for the given label
        String filePath = fileMap.computeIfAbsent(label,
                k -> Paths.get(baseDirectory, label + ".txt").toString());

        // Write formatted data to the appropriate file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n",
                    patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}
