package com.data_management;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class dataReaderImplementation implements DataReader {

    private String location;
    private int port;

    /**
    * initiates the dataReaderImplementation object
    * and sets the loaction it should read from
    * 
    * @param location the folder to read data from
    */
    public dataReaderImplementation(String location){
        this.location = location;
    }

    
    /** 
     * @return String
     */
    public String getLocation() {
        return this.location;
    }

    
    /** 
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }
    
    
    /** 
     * @param dataStorage
     * @throws IOException
     */
    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        final File folder = new File(location);
        String line;
        FileReader fr;
        BufferedReader br;

        for (final File fileEntry : folder.listFiles()){
            if (!fileEntry.isDirectory()) {
                try {
                    
                    fr = new FileReader(fileEntry);
                    br = new BufferedReader(fr);

                    while ((line = br.readLine()) != null) {
                        this.readDataLine(line, dataStorage);
                    }
                    } catch (IOException e) {
                        throw e;
                    }
            }
        }
    }

    private void readDataLine(String line, DataStorage storage){
        String[] parts = line.split(",");

        int id = Integer.parseInt(parts[0].split(":")[1].trim());
        long timestamp = Long.parseLong(parts[1].split(":")[1].trim(), 10);
        String recordType = parts[2].split(":")[1].trim();
        double measurementValue = 0;
        
        switch(recordType){
            case "Alert":
                // can not be handled since records only accept doubles as value
                return;
            case "Saturation":
                measurementValue = Double.parseDouble(parts[3].split(":")[1].trim().replaceAll("%", ""));
                break;
            default:
                measurementValue = Double.parseDouble(parts[3].split(":")[1].trim());
                break;
        }
        storage.addPatientData(id, measurementValue, recordType, timestamp);
        return;
        
        
    }   
    
}
