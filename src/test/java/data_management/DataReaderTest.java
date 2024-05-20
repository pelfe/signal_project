package data_management;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.data_management.DataReader;
import com.data_management.DataReaderImplementation;
import com.data_management.DataStorage;
import com.data_management.PatientRecord;

public class DataReaderTest {
    @Test
    void testDataReader() {
        massTest();
        edgeCaseTest();
        
    }

    /**
     * tests the data reader on a mass of output data, suceeds if the data reader throws no error of any kind
     * 
     */
    void massTest(){
        DataStorage storage = new DataStorage();
        DataReader myDataReader = new DataReaderImplementation("src\\test\\java\\data_management\\TestDataFiles\\DataReaderMassTestFiles");
        try {
            myDataReader.readData(storage);
        } catch (Exception e) {
            // if the reading does not work it should fail the test
            assertEquals(1, 0);
        }
    }

    void edgeCaseTest(){
        DataStorage storage = new DataStorage();
        DataReader myDataReader = new DataReaderImplementation("src\\test\\java\\data_management\\TestDataFiles\\DataReaderEdgeCaseTestFiles");
        try {
            myDataReader.readData(storage);
        } catch (Exception e) {
            // if the reading does not work it should fail the test
            assertEquals(1, 0);
        }
    }
}
