package com.alerts.Decorators;

import com.alerts.Alert;

public class AlertDecorator extends Alert{
    private AlertDecorator myDecorator; 

    public AlertDecorator(String patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
        
    }
    

}
