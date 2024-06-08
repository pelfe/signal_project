package com.alerts.AlertSubtypes;

import com.alerts.Alert;

public class StaffAlert extends Alert{

    public StaffAlert(String patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }
    
}
