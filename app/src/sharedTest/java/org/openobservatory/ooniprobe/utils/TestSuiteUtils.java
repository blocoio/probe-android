package org.openobservatory.ooniprobe.utils;

import org.openobservatory.ooniprobe.test.test.AbstractTest;

import java.util.List;

public class TestSuiteUtils {

    /**
     * Saves a result in the DB and returns it with the given number of measurements, and
     * all related model objects in the DB.
     *
     * @param accessibleMeasurements number of successful measurements
     * @param accessibleTestTypes    list with successful measurements
     * @param blockedMeasurements    number of failed measurements
     * @param blockedTestTypes       list with failed measurements
     * @throws IllegalArgumentException for excess number of measurements
     */
    public static void populateMeasurements(
            List<AbstractTest> measurementsPool,
            int accessibleMeasurements, List<AbstractTest> accessibleTestTypes,
            int blockedMeasurements, List<AbstractTest> blockedTestTypes
    ) {

        if (accessibleMeasurements + blockedMeasurements > measurementsPool.size()) {
            throw new IllegalArgumentException("Test suite only has "
                    + measurementsPool.size()
                    + " possibilities of measurement, can't run "
                    + (accessibleMeasurements + blockedMeasurements)
            );
        }

        int typeCurrentIndex = 0;
        int maxMeasurements = Math.max(accessibleMeasurements, blockedMeasurements);

        if (maxMeasurements == accessibleMeasurements) {
            for (int i = 0; i < accessibleMeasurements; i++) {
                accessibleTestTypes.add(measurementsPool.get(typeCurrentIndex++));
            }

            for (int i = 0; i < blockedMeasurements; i++) {
                blockedTestTypes.add(measurementsPool.get(typeCurrentIndex++));
            }
        } else {
            for (int i = 0; i < blockedMeasurements; i++) {
                blockedTestTypes.add(measurementsPool.get(typeCurrentIndex++));
            }

            for (int i = 0; i < accessibleMeasurements; i++) {
                accessibleTestTypes.add(measurementsPool.get(typeCurrentIndex++));
            }
        }
    }

}
