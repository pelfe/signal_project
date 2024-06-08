package data_management;

import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public class AlertGeneratorTest {
    private DataStorage dataStorage;
    private AlertGenerator alertGenerator;
    @BeforeEach
    public void setup() {
        dataStorage = DataStorage.getInstance();
        alertGenerator = new AlertGenerator(dataStorage);
    }


    //Step 1 tests!
    @Test
    public void CriticalThresholdTests() {
        //Systolic extreme case tests, IE partitioning tests
        dataStorage.addPatientData(1, 222, "systolic_bp", System.currentTimeMillis());
        dataStorage.addPatientData(1, 42, "systolic_bp", System.currentTimeMillis());

        //Diastolic extreme case tests
        dataStorage.addPatientData(1, 130, "diastolic_bp", System.currentTimeMillis());
        dataStorage.addPatientData(1, 42, "diastolic_bp", System.currentTimeMillis());
    }

    @Test
    public void IncreasingTrendTest() {
        //So when three consecutive systolic blood pressure readings increase by more than 10 mmHg each, an alert is triggered.
        dataStorage.addPatientData(1, 100, "systolic_bp", System.currentTimeMillis());
        dataStorage.addPatientData(1, 120, "systolic_bp", System.currentTimeMillis());
        dataStorage.addPatientData(1, 140, "systolic_bp", System.currentTimeMillis());
    }

    @Test
    public void DecreasingTrendTest() {
        //So the same, but when decreasing, so absolutely different, but then again also the same.
        dataStorage.addPatientData(1, 140, "systolic_bp", System.currentTimeMillis());
        dataStorage.addPatientData(1, 120, "systolic_bp", System.currentTimeMillis());
        dataStorage.addPatientData(1, 100, "systolic_bp", System.currentTimeMillis());
    }

    @Test
    public void LowSatTest() {
        // Below 92% saturation
        dataStorage.addPatientData(1, 80, "Saturation", System.currentTimeMillis());

    }

    @Test
    public void DropTest() {
        // Rapid drop in saturation
        long currentTime = System.currentTimeMillis();
        dataStorage.addPatientData(1, 100, "Saturation", System.currentTimeMillis());
        dataStorage.addPatientData(1, 94, "Saturation", System.currentTimeMillis() + 5 * 60 * 1000); // 5mins later
    }

    @Test
    public void HypetensiveHypoxemiaTest() {
        // Systolic blood pressure is below 90?
        dataStorage.addPatientData(1, 82, "systolic_bp", System.currentTimeMillis());

        // What if it's below 92%?
        dataStorage.addPatientData(1, 42, "Saturation", System.currentTimeMillis());
    }

    @Test
    public void EGFRTest() {
        // Heart rate below 50
        dataStorage.addPatientData(1,  42, "heart_rate", System.currentTimeMillis());
        // Above 100
        dataStorage.addPatientData(1,  120, "heart_rate", System.currentTimeMillis());
    }

    @Test
    public  void testIrregularBeat() {
        // Irregular values, should give and not give alers basically
        dataStorage.addPatientData(1, 50, "heart_rate", System.currentTimeMillis());
        dataStorage.addPatientData(1, 300, "heart_rate", System.currentTimeMillis() + 1);
        
    }



}
