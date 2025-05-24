package com.data_management;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileDataReader implements DataReader {

    private final String outputDir;

    public FileDataReader(String outputDir) {
        this.outputDir = outputDir;
    }

    @Override
    public void readData(DataStorage storage) {
        File dir = new File(outputDir);
        if (!dir.exists() || !dir.isDirectory()) {
            System.err.println("Invalid directory: " + outputDir);
            return;
        }

        File[] files = dir.listFiles();
        if (files == null) {
            System.err.println("No files found in directory: " + outputDir);
            return;
        }

        for (File file : files) {
            if (file.isFile()) {
                parseFile(file, storage);
            }
        }
    }

    private void parseFile(File file, DataStorage storage) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Expected CSV format: patientId,measurementValue,recordType,timestamp
                String[] parts = line.split(",");
                if (parts.length != 4) {
                    System.err.println("Skipping malformed line: " + line);
                    continue;
                }
                try {
                    int patientId = Integer.parseInt(parts[0].trim());
                    double measurementValue = Double.parseDouble(parts[1].trim());
                    String recordType = parts[2].trim();
                    long timestamp = Long.parseLong(parts[3].trim());

                    storage.addPatientData(patientId, measurementValue, recordType, timestamp);

                } catch (NumberFormatException e) {
                    System.err.println("Skipping line due to parse error: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + file.getName() + ": " + e.getMessage());
        }
    }
}

