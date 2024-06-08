package com.alerts.Factorys;

import com.alerts.Alert;
import com.alerts.AlertSubtypes.StaffAlert;

public class staffAlertFactory implements AlertFactory {
    public Alert createAlert(int patientId, String message, long timestamp) {
        return new StaffAlert(Integer.toString(patientId), message, timestamp);
    }
}
