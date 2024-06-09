package data_management;

import com.alerts.AlertGenerator;
import com.cardio_generator.outputs.ConsoleOutputStrategy;
import com.data_management.DataStorage;
import com.data_management.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AlertGeneratorTest {

    @BeforeEach
    public void setup() {
        DataStorage.wipeClean();
    }

    @Test
    public void SystolicSlowlyInc() {
        DataStorage dataStorage = DataStorage.getInstance();
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);

        dataStorage.addPatientData(1, 100, "systolic_bp", System.currentTimeMillis());
        dataStorage.addPatientData(1, 105, "systolic_bp", System.currentTimeMillis());
        dataStorage.addPatientData(1, 110, "systolic_bp", System.currentTimeMillis());
        Patient patient = dataStorage.getAllPatients().get(0);
        boolean alertTriggered = patient.getAlerts().contains("Trend change for systolic blood pressure");
        System.out.println("Alerts: " + patient.getAlerts());

        assertFalse(alertTriggered);
    }


    @Test
    public void SaturationNormal() {
        DataStorage dataStorage = DataStorage.getInstance();
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);

        dataStorage.addPatientData(1, 95, "Saturation", System.currentTimeMillis());
        Patient patient = dataStorage.getAllPatients().get(0);
        alertGenerator.evaluateData(patient);
        boolean alertTriggered = patient.getAlerts().contains("Low saturation");
        System.out.println("Alerts: " + patient.getAlerts());
        assertFalse(alertTriggered);
    }

    @Test
    public void SaturationDropsRapidly() {
        DataStorage dataStorage = DataStorage.getInstance();
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);

        long currentTime = System.currentTimeMillis();
        dataStorage.addPatientData(1, 100, "Saturation", currentTime);
        dataStorage.addPatientData(1, 94, "Saturation", currentTime);
        Patient patient = dataStorage.getAllPatients().get(0);
        alertGenerator.evaluateData(patient);
        boolean alertTriggered = patient.getAlerts().contains("Saturation decreasing rapidly");
        System.out.println("Alerts: " + patient.getAlerts());

        assertTrue(alertTriggered);
    }

}
