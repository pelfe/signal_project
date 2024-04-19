package com.cardio_generator.outputs;

/**
 * This interface defines the basic information flow for every Output strategy
 * 
 * usage:
 * This interface should be implemented by any class responsible for patient related data output
 */
public interface OutputStrategy {
    /**
     * 
     * 
     * @param patientId the patientId of the patient whoms information will be put out
     * @param timestamp the current time
     * @param label what kind of data is being put out
     * @param data The data being put out
     */
    void output(int patientId, long timestamp, String label, String data);
}
