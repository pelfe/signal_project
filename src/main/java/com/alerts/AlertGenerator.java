package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;
    private PatientRecord lastSys = null;
    private PatientRecord prevSys = null;
    private PatientRecord lastDias = null;
    private PatientRecord prevDias = null;
    private PatientRecord lastSat = null;
    private PatientRecord prevSat = null;
    private double lastBeatInterval = 0;
    private double prevBeatInterval = 0;

    //Arbitrary threshold for beat interval
    private static final double BEAT_THRESHOLD = 1.5;



    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {

        List<PatientRecord> records = dataStorage.getRecords(patient.getPatientId(), 0, Long.MAX_VALUE);
        for (PatientRecord record : records) {
            //Step 1:
            systolicDiabolic(record);
            //Step 2:
            saturation(record);
            //Step 3:
            //So basically updating whichever record is the last one of the type and then updating that as the record so we can move more
            //into downstream IE you have to compare both values, but without this structure, you can't do that.
            if (record.getRecordType().equals("systolic_bp")) {
                lastSys = record;
            } else if (record.getRecordType().equals("Saturation")) {
                lastSat = record;
            }
            HypetensiveHypoxemia(record);
            //Step 4:
            if(record.getRecordType().equals("heart_rate")) {
                IrregularBeatPeriod(record);
            }

            //Step 5:
            triggeredByButtonOrbyStaff(record.getPatientId());


        }
    }




    public void systolicDiabolic(PatientRecord record) {

        //Step 1:
        //Checks whether the systolic and diastolic blood pressure is above or below the normal
        if (record.getRecordType().equals("systolic_bp")) {
            prevSys = lastSys;
            lastSys = record;
            double diff1 = record.getMeasurementValue() - lastSys.getMeasurementValue();
            double diff2 = lastSys.getMeasurementValue() - prevSys.getMeasurementValue();
            if ((diff1 > 10 && diff2 > 10) || (diff1 < -10 && diff2 < -10)) {
                triggerAlert(new Alert(Integer.toString(record.getPatientId()), "Trend change for systolic blood pressure", record.getTimestamp()));
            }
            if (record.getMeasurementValue() > 180) {
                triggerAlert(new Alert(Integer.toString(record.getPatientId()), "Systolic Blood pressure is high", record.getTimestamp()));
            } 
            else if (record.getMeasurementValue() < 90) {
                triggerAlert(new Alert(Integer.toString(record.getPatientId()), "Systolic blood pressure is low", record.getTimestamp()));
            }
        }

        else if (record.getRecordType().equals("diastolic_bp")) {
            prevDias = lastDias;
            lastDias = record;
            double diff1 = record.getMeasurementValue() - lastDias.getMeasurementValue();
            double diff2 = lastDias.getMeasurementValue() - prevDias.getMeasurementValue();
            if ((diff1 > 10 && diff2 > 10) || (diff1 < -10 && diff2 < -10)) {
                triggerAlert(new Alert(Integer.toString(record.getPatientId()), "Trend change for diastolic blood pressure", record.getTimestamp()));
            }
            if (record.getMeasurementValue() > 120) {
                triggerAlert(new Alert(Integer.toString(record.getPatientId()), "High blood pressure detected!", record.getTimestamp()));
            }
            else if (record.getMeasurementValue() < 60) {
                triggerAlert(new Alert(Integer.toString(record.getPatientId()), "Low blood pressure detected!", record.getTimestamp()));
            }
        }
    }

    //Checks whether saturation is below 92% or if it decreases rapidly
    public void saturation(PatientRecord record) {
        //Step 2:
        if (record.getRecordType().equals("Saturation")) {
            if (record.getMeasurementValue() < 92) {
                triggerAlert(new Alert(Integer.toString(record.getPatientId()), "Low saturation", record.getTimestamp()));
            } 
            else if (prevSat != null && lastSat != null && (record.getTimestamp() - prevSat.getTimestamp()) <= 10 * 60 * 1000){
                if ((prevSat.getMeasurementValue() - record.getMeasurementValue()) >= 5) {
                    triggerAlert(new Alert(Integer.toString(record.getPatientId()), "Saturation decreasing rapidly", record.getTimestamp()));
                }
            }
        }
        prevSat = lastSat;
        lastSat = record;
        }

        //Checks whether the patient has a hypotensive hypoxic condition.
    public void HypetensiveHypoxemia(PatientRecord record) {
        //Step 3:
        if (lastSys != null && lastSat != null) {
            if (lastSys.getMeasurementValue() < 90 && lastSat.getMeasurementValue() < 92) {
                triggerAlert(new Alert(Integer.toString(lastSys.getPatientId()), "Hypotensive hypoxeia alert!", lastSys.getTimestamp()));
            }
        }
    }




    //Alert by nonsystematic feedback from the patient or from nearby nurse
    public void triggeredByButtonOrbyStaff(int patientId) {
        triggerAlert(new Alert(Integer.toString(patientId), "Patient alerts!", System.currentTimeMillis()));
    }


    //Most ambigious one, but it's about the irregular beat period
    //So some assumptions must be made, big jump is irregular, but how big?
    //So we just want an interval in which

    public void IrregularBeatPeriod(PatientRecord record) {
        // Calculate the new beat interval
        double newBeatInterval = System.currentTimeMillis() - record.getTimestamp();

        // So if it's 1.5 bigger/smaller than last one, then it's irregular, that's arbitrary example.
        if (lastBeatInterval != 0 && Math.abs(newBeatInterval - lastBeatInterval) > lastBeatInterval * BEAT_THRESHOLD) {
            triggerAlert(new Alert(Integer.toString(record.getPatientId()), "Irregular beats", record.getTimestamp()));
        }
        // Updating
        prevBeatInterval = lastBeatInterval;
        lastBeatInterval = newBeatInterval;
    }


    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        
        // Implementation might involve logging the alert or notifying staff
    }
}
