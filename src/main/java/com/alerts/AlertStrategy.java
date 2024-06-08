package com.alerts;

import com.data_management.Patient;

public interface AlertStrategy {
    
    public Boolean checkAlert(Patient Patient);
}
