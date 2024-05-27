package data_management;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.data_management.DataReader;
import com.data_management.DataStorage;
import com.data_management.PatientRecord;
import com.data_management.dataReaderImplementation;

class DataStorageTest {

    @Test
    void testAddAndGetRecords() {
        // DataReader reader
        DataStorage storage = new DataStorage();
        DataReader myDataReader = new dataReaderImplementation("src\\test\\java\\data_management\\TestDataFiles\\DataStorageTestFiles");
        try {
            myDataReader.readData(storage);
        } catch (Exception e) {
            // if the reading does not work it should fail the test
            assertEquals(1, 0);
        }
        storage.addPatientData(1, 300.0, "WhiteBloodCells", 1714376789051L);

        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);
        assertEquals(3, records.size()); // Check if two records are retrieved
        assertEquals(100.0, records.get(0).getMeasurementValue()); // Validate first record
        assertEquals(300.0, records.get(2).getMeasurementValue()); // Validate third record
    }
}
