package com.alerts;

import com.alerts.Factorys.AlertFactory;
import com.alerts.Factorys.BloodPressureFactory;
import com.alerts.Factorys.BloodSaturationFactory;
import com.alerts.Factorys.ECGFactory;
import com.alerts.Factorys.HypothermiaFactory;
import com.alerts.Factorys.ECGFactory;
import com.alerts.Factorys.staffAlertFactory;
import com.alerts.Strategys.AlertStrategy;
import com.alerts.Strategys.BloodPressureStrategy;
import com.alerts.Strategys.BloodSaturationStrategy;
import com.alerts.Strategys.ECGStrategy;
import com.alerts.Strategys.HypothermiaStrategy;
import com.alerts.Strategys.StaffStrategy;
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
public class AlertGenerator2 {



    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator2() {
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


        AlertStrategy currentStrategy = new BloodPressureStrategy();
        AlertFactory currentFactory  =new BloodPressureFactory();
        if(currentStrategy.checkAlert(patient)){
            currentFactory.createAlert(patient.getPatientId(), "Blood Pressure Problem", System.currentTimeMillis());
        }

        currentStrategy = new BloodSaturationStrategy();
        currentFactory  =new BloodSaturationFactory();
        if(currentStrategy.checkAlert(patient)){
            currentFactory.createAlert(patient.getPatientId(), "Blood Saturation Problem", System.currentTimeMillis());
        }        

        currentStrategy = new ECGStrategy();
        currentFactory  =new ECGFactory();
        if(currentStrategy.checkAlert(patient)){
            currentFactory.createAlert(patient.getPatientId(), "Heartbeat Problem", System.currentTimeMillis());
        }

        currentStrategy = new HypothermiaStrategy();
        currentFactory  =new HypothermiaFactory();
        if(currentStrategy.checkAlert(patient)){
            currentFactory.createAlert(patient.getPatientId(), "Patient has Hypothermia", System.currentTimeMillis());
        }

        currentStrategy = new StaffStrategy();
        currentFactory  =new staffAlertFactory();
        if(currentStrategy.checkAlert(patient)){
            currentFactory.createAlert(patient.getPatientId(), "Manual Staff Alert", System.currentTimeMillis());
        }
    }
}
