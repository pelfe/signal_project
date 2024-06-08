package com.alerts.Factorys;

import com.alerts.Alert;

public interface AlertFactory {
    Alert createAlert(int patientId, String message, long timestamp);
}
