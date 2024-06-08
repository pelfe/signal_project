package com.alerts.Factorys;

import com.alerts.Alert;
import com.alerts.AlertSubtypes.HypothermiaAlert;

public class HypothermiaFactory implements AlertFactory {
    public Alert createAlert(int patientId, String message, long timestamp) {
        return new HypothermiaAlert(Integer.toString(patientId), message, timestamp);
    }
}
