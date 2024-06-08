package com.alerts.AlertSubtypes;

import com.alerts.Alert;

public class HypothermiaAlert extends Alert{

    public HypothermiaAlert(String patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }
    
}
