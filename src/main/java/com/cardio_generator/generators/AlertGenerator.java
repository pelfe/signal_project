package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;


/*
* This class manages alert states for patients.
*
* Usage: Given patientCount,
* handle the alert stages for the patients.
*
*/
public class AlertGenerator implements PatientDataGenerator {

    
    public static final Random randomGenerator = new Random();

    //Non-constants should be in camelCase changed naming.
    private boolean[] alertStates; // false = resolved, true = pressed

    //Method names are written on lower camel case. But it's a constructor, so it must
    //have same name as the class it's from.

    /*
    * In the constructor, initialise alertStates using patientCount.
    *
    * @param patientCount as parameter that tells you the count of patients.
    */
    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1];
    }

    /*
    * This method given patientId and outputStrategy changes patients alert stages.
    * Checks whether patientId is in the boolean list.
    * When it is, then there's 90% of resolving the problem.
    * Then it will set alert to false for patient and output this patient and current time with resolved.
    *
    * If no alert, then calculate chance for alert during the period.
    * Generate new alert if p is bigger than randomGenerator.nextDouble()
    * outputs time and that alert has been triggered.
    *
    *
    *
    * @param patientId is Id of patient.
    * @param outputStategy is the outputStrategy.
    * @throws Expection if there's an error generating alert data. IE
    * patientId is not in alertStates boolean array.
    */

    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (alertStates[patientId]) {
                if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve
                    alertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                //All variables should be in lower case camel. Changed naming.
                double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
                double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
                boolean alertTriggered = randomGenerator.nextDouble() < p;

                if (alertTriggered) {
                    //Had to change from AlertStates to alertStates,
                    // to match the variable name change.
                    alertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
