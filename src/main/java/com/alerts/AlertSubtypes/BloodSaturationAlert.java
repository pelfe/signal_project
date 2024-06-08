package com.alerts.AlertSubtypes;

import com.alerts.Alert;

public class BloodSaturationAlert extends Alert{

    public BloodSaturationAlert(String patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }
    
}
