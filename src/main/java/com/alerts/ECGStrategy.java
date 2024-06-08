package com.alerts;

import java.util.List;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

public class ECGStrategy implements AlertStrategy{

    @Override
    public Boolean checkAlert(Patient patient) {

        final double BEAT_THRESHOLD = 1.5;
        double lastBeatInterval = 0;
        double prevBeatInterval = 0;
        DataStorage dataStorage = DataStorage.getInstance();
        List<PatientRecord> records = dataStorage.getRecords(patient.getPatientId(), 0, Long.MAX_VALUE);

        for (PatientRecord record : records) {
            if(!record.getRecordType().equals("heart_rate")) {
                continue;
            }

            double newBeatInterval = System.currentTimeMillis() - record.getTimestamp();
            // So if it's 1.5 bigger/smaller than last one, then it's irregular, that's arbitrary example.
            if (lastBeatInterval != 0 && Math.abs(newBeatInterval - lastBeatInterval) > lastBeatInterval * BEAT_THRESHOLD) {
                return true;
            }
            // Updating
            prevBeatInterval = lastBeatInterval;
            lastBeatInterval = newBeatInterval;
        
        }
        return false;
    }
    
}
