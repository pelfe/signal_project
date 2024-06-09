package com.alerts;

import com.alerts.Factorys.AlertFactory;
import com.alerts.Factorys.BloodPressureFactory;
import com.alerts.Factorys.BloodSaturationFactory;
import com.alerts.Factorys.ECGFactory;
import com.alerts.Factorys.HypothermiaFactory;
import com.alerts.Factorys.staffAlertFactory;
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
    private PatientRecord prevPrevSys = null;
    private PatientRecord lastDias = null;
    private PatientRecord prevDias = null;
    private PatientRecord lastSat = null;
    private PatientRecord prevSat = null;
    private double lastBeatInterval = 0;

    // Arbitrary threshold for beat interval
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
            // Step 1:
            systolicDiabolic(record, patient);
            // Step 2:
            saturation(record, patient);
            // Step 3:
            hypotensiveHypoxemia(record, patient);
            // Step 4:
            if (record.getRecordType().equals("heart_rate")) {
                irregularBeatPeriod(record, patient);
            }
            // Step 5:
            triggeredByButtonOrbyStaff(record.getPatientId(), patient);
        }
    }

    public void systolicDiabolic(PatientRecord record, Patient patient) {
        AlertFactory alertFactory = new BloodPressureFactory();
        // Step 1:
        // Checks whether the systolic and diastolic blood pressure is above or below the normal
        if (record.getRecordType().equals("systolic_bp")) {
            prevPrevSys = prevSys;
            prevSys = lastSys;
            lastSys = record;
            if (prevPrevSys != null && prevSys != null) {
                double diff1 = lastSys.getMeasurementValue() - prevSys.getMeasurementValue();
                double diff2 = prevSys.getMeasurementValue() - prevPrevSys.getMeasurementValue();
                if (Math.abs(diff1) > 10 && Math.abs(diff2) > 10 &&
                        ((diff1 > 0 && diff2 > 0) || (diff1 < 0 && diff2 < 0))) {
                    String alert = "Trend change for systolic blood pressure";
                    String alertMessage = "ID: " + record.getPatientId() + " Alert: " + alert + " at: " + record.getTimestamp();
                    patient.addAlert(alertMessage);
                    triggerAlert(alertFactory.createAlert(record.getPatientId(), alert, record.getTimestamp()), patient);
                }
            }

            if (record.getMeasurementValue() > 180) {
                String alert = "Systolic Blood pressure is high";
                patient.addAlert(alert);
                triggerAlert(alertFactory.createAlert(record.getPatientId(), alert, record.getTimestamp()), patient);
            } else if (record.getMeasurementValue() < 90) {
                String alert = "Systolic blood pressure is low";
                patient.addAlert(alert);
                triggerAlert(alertFactory.createAlert(record.getPatientId(), alert, record.getTimestamp()), patient);
            }
        } else if (record.getRecordType().equals("diastolic_bp")) {
            prevDias = lastDias;
            lastDias = record;
            double diff1 = record.getMeasurementValue() - lastDias.getMeasurementValue();
            double diff2 = lastDias.getMeasurementValue() - prevDias.getMeasurementValue();
            if ((diff1 > 10 && diff2 > 10) || (diff1 < -10 && diff2 < -10)) {
                String alert = "Trend change for diastolic blood pressure";
                patient.addAlert(alert);
                triggerAlert(alertFactory.createAlert(record.getPatientId(), alert, record.getTimestamp()), patient);
            }
            if (record.getMeasurementValue() > 120) {
                String alert = "High blood pressure detected!";
                patient.addAlert(alert);
                triggerAlert(alertFactory.createAlert(record.getPatientId(), alert, record.getTimestamp()), patient);
            } else if (record.getMeasurementValue() < 60) {
                String alert = "Low blood pressure detected!";
                patient.addAlert(alert);
                triggerAlert(alertFactory.createAlert(record.getPatientId(), alert, record.getTimestamp()), patient);
            }
        }
    }

    /**
     * Checks whether saturation is below 92% or if it decreases rapidly
     */
    private void saturation(PatientRecord record, Patient patient) {
        AlertFactory alertFactory = new BloodSaturationFactory();
        if (record.getRecordType().equals("Saturation")) {
            System.out.println("Evaluating saturation for patient: " + record.getPatientId() + " with value: " + record.getMeasurementValue());

            if (record.getMeasurementValue() < 92) {
                String alert = "ID: " + record.getPatientId() + " Alert: Low saturation at: " + record.getTimestamp();
                patient.addAlert(alert);
                triggerAlert(alertFactory.createAlert(record.getPatientId(), alert, record.getTimestamp()), patient);
            }

            if (prevSat != null && (prevSat.getMeasurementValue() - record.getMeasurementValue() >= 5)) {
                String alert = "Saturation decreasing rapidly";
                patient.addAlert(alert);
                triggerAlert(alertFactory.createAlert(record.getPatientId(), alert, record.getTimestamp()), patient);
            }

            prevSat = record;
        }
    }

    /**
     * Checks whether the patient has a hypotensive hypoxic condition.
     */
    private void hypotensiveHypoxemia(PatientRecord record, Patient patient) {
        AlertFactory alertFactory = new HypothermiaFactory();
        // Step 3:
        if (lastSys != null && lastSat != null) {
            if (lastSys.getMeasurementValue() < 90 && lastSat.getMeasurementValue() < 92) {
                String alert = "Hypotensive hypoxemia alert!";
                patient.addAlert(alert);
                triggerAlert(alertFactory.createAlert(record.getPatientId(), alert, lastSys.getTimestamp()), patient);
            }
        }
    }

    /**
     * Alert by nonsystematic feedback from the patient or from nearby nurse
     */
    public void triggeredByButtonOrbyStaff(int patientId, Patient patient) {
        AlertFactory alertFactory = new staffAlertFactory();
        String alert = "Patient alerts!";
        patient.addAlert(alert);
        triggerAlert(alertFactory.createAlert(patientId, alert, System.currentTimeMillis()), patient);
    }

    /**
     * Most ambiguous one, but it's about the irregular beat period
     * So some assumptions must be made, big jump is irregular, but how big?
     * So we just want an interval in which
     */
    public void irregularBeatPeriod(PatientRecord record, Patient patient) {
        AlertFactory alertFactory = new ECGFactory();
        // Calculate the new beat interval
        double newBeatInterval = System.currentTimeMillis() - record.getTimestamp();

        // So if it's 1.5 times bigger/smaller than last one, then it's irregular, that's arbitrary example.
        if (lastBeatInterval != 0 && Math.abs(newBeatInterval - lastBeatInterval) > lastBeatInterval * BEAT_THRESHOLD) {
            String alert = "Irregular beats";
            patient.addAlert(alert);
            triggerAlert(alertFactory.createAlert(record.getPatientId(), alert, record.getTimestamp()), patient);
        }
        // Updating
        lastBeatInterval = newBeatInterval;
    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert   the alert object containing details about the alert condition
     * @param patient the patient to whom the alert belongs
     */
    private void triggerAlert(Alert alert, Patient patient) {
        String alertMessage = "ID: " + alert.getPatientId() + " Alert Type: " + alert.getCondition() + " at: " + alert.getTimestamp();
        patient.addAlert(alertMessage);
        System.out.println(alertMessage);
    }
}
