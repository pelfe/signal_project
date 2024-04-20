package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;


/*
* This provides an interface to have
* generate method for all the classes in generators
* folder.
*
*/
public interface PatientDataGenerator {
    /*
    * This method generates all the data about patients.
    *
    * @param patiendId is the Id of patient.
    * @param outputStrategy is outputStrategy
    */
    void generate(int patientId, OutputStrategy outputStrategy);
}
