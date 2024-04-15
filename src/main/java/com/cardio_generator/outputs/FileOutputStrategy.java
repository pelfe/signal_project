package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

// class name not written in upper camel case
public class FileOutputStrategy implements OutputStrategy {

    // Non constant Field names are written in lower camel case
    private String baseDirectory;

    // constants are all uppercase letters
    public final ConcurrentHashMap<String, String> FILE_MAP = new ConcurrentHashMap<>();

    //Method names are written in lower camel case, but class names in UpperCamelCase, so how to deal with
    // initializing methods?
    public FileOutputStrategy(String baseDirectory) {

        this.baseDirectory = baseDirectory;
    }

    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        // local variable must be lower camle case, line must have less than 100 column points
        String filePath = FILE_MAP.computeIfAbsent(label, k -> 
            Paths.get(baseDirectory, label + ".txt").toString());

        // Write the data to the file
        //line must have less than 100 column points
        try (PrintWriter out = new PrintWriter(
            Files.newBufferedWriter(Paths.get(filePath),
            StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            //line must have less than 100 column points
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", 
                patientId, timestamp, label, data);
        } catch (Exception e) {
            // caught exceptions should only be ignored if they are excpected
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
            return;
        }
    }
}