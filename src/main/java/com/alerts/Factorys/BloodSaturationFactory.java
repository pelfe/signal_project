package com.alerts.Factorys;

import com.alerts.Alert;
import com.alerts.AlertSubtypes.BloodSaturationAlert;

public class BloodSaturationFactory implements AlertFactory {
    public Alert createAlert(int patientId, String message, long timestamp) {
        return new BloodSaturationAlert(Integer.toString(patientId), message, timestamp);
    }
}
