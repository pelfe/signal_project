package com.alerts;

public interface AlertFactory {
    Alert makeAlert(int patientId, String message, long timestamp);
}
