package com.alerts.Factorys;

import com.alerts.Alert;
import com.alerts.AlertSubtypes.BloodPressureAlert;

public class BloodPressureFactory implements AlertFactory {
    public Alert createAlert(int patientId, String message, long timestamp) {
        return new BloodPressureAlert(Integer.toString(patientId), message, timestamp);
    }
}
