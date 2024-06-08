package com.alerts;

import java.util.List;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

public class BloodSaturationStrategy implements AlertStrategy{

    @Override
    public Boolean checkAlert(Patient patient) {

        PatientRecord lastSat = null;
        PatientRecord prevSat = null;
        DataStorage dataStorage = DataStorage.getInstance();
        List<PatientRecord> records = dataStorage.getRecords(patient.getPatientId(), 0, Long.MAX_VALUE);

        for (PatientRecord record : records) {
            if (record.getRecordType().equals("Saturation")) {
                if (record.getMeasurementValue() < 92) {
                    return true;
                }
                else if (prevSat != null && lastSat != null && (record.getTimestamp() - prevSat.getTimestamp()) <= 10 * 60 * 1000){
                    if ((prevSat.getMeasurementValue() - record.getMeasurementValue()) >= 5) {
                        return true;
                    }
                }
            }
            
            prevSat = lastSat;
            lastSat = record;
            if (record.getRecordType().equals("Saturation")) {
                lastSat = record;
            }
        }
        return false;
    }
    
}
