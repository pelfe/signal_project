package com.alerts;

public class HypothermiaFactory implements AlertFactory {
    public Alert makeAlert(int patientId, String message, long timestamp) {
        return new Alert(Integer.toString(patientId), message, timestamp);
    }
}
