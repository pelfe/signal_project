package com.alerts.Factorys;

import com.alerts.Alert;
import com.alerts.AlertSubtypes.ECGAlert;

public class ECGFactory implements AlertFactory {
    public Alert createAlert(int patientId, String message, long timestamp) {
        return new ECGAlert(Integer.toString(patientId), message, timestamp);
    }
}
