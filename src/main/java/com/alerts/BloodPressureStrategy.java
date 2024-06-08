package com.alerts;

import java.util.List;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

public class BloodPressureStrategy implements AlertStrategy{

    @Override
    public Boolean checkAlert(Patient patient) {
        PatientRecord lastSys = null;
        PatientRecord prevSys = null;
        PatientRecord lastDias = null;
        PatientRecord prevDias = null;

        DataStorage dataStorage = DataStorage.getInstance();
        List<PatientRecord> records = dataStorage.getRecords(patient.getPatientId(), 0, Long.MAX_VALUE);
        for (PatientRecord record : records) {
            if (record.getRecordType().equals("systolic_bp")) {
                prevSys = lastSys;
                lastSys = record;
                if(systolicCheck(record, lastSys, prevSys)){
                    return true;
                }
            }

            else if (record.getRecordType().equals("diastolic_bp")) {
                prevDias = lastDias;
                lastDias = record;
                if(diastolicCheck(record, lastDias, prevDias)){
                    return true;
                }
            }
            
            if (record.getRecordType().equals("systolic_bp")) {
                lastSys = record;
            } 
        }
        return false;
    }
    
    public Boolean systolicCheck(PatientRecord record, PatientRecord lastSys, PatientRecord prevSys){
        double diff1 = record.getMeasurementValue() - lastSys.getMeasurementValue();
        double diff2 = lastSys.getMeasurementValue() - prevSys.getMeasurementValue();
        if ((diff1 > 10 && diff2 > 10) || (diff1 < -10 && diff2 < -10)) {
            return true;
        }
        if (record.getMeasurementValue() > 180) {
            return true;
        } 
        else if (record.getMeasurementValue() < 90) {
            return true;
        }
        return false;
    }

    public Boolean diastolicCheck(PatientRecord record, PatientRecord lastDias, PatientRecord prevDias){
        double diff1 = record.getMeasurementValue() - lastDias.getMeasurementValue();
        double diff2 = lastDias.getMeasurementValue() - prevDias.getMeasurementValue();
        if ((diff1 > 10 && diff2 > 10) || (diff1 < -10 && diff2 < -10)) {
            return true;
        }
        if (record.getMeasurementValue() > 120) {
            return true;
        }
        else if (record.getMeasurementValue() < 60) {
            return true;
        }
        return false;
    }

}
