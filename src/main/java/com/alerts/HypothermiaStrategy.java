package com.alerts;

import java.util.List;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

public class HypothermiaStrategy implements AlertStrategy {

    @Override
    public Boolean checkAlert(Patient patient) {
        PatientRecord lastSys = null;
        PatientRecord lastSat = null;
        DataStorage dataStorage = DataStorage.getInstance();

        List<PatientRecord> records = dataStorage.getRecords(patient.getPatientId(), 0, Long.MAX_VALUE);
        for (PatientRecord record : records) {
            
            if (record.getRecordType().equals("systolic_bp")) {
                lastSys = record;
            } else if (record.getRecordType().equals("Saturation")) {
                lastSat = record;
            }

            if (lastSys != null && lastSat != null) {
                if (lastSys.getMeasurementValue() < 90 && lastSat.getMeasurementValue() < 92) {
                    return true;
                }
            }
        }
        return false;  
    }
}
