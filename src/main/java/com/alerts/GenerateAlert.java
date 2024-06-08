package com.alerts;
import com.data_management.PatientRecord;

public interface GenerateAlert {
    void generateAlert(PatientRecord record, AlertGenerator alertGenerator);
}
