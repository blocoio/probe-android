package org.openobservatory.ooniprobe;

import org.openobservatory.ooniprobe.test.test.AbstractTest;
import org.openobservatory.ooniprobe.test.test.FacebookMessenger;
import org.openobservatory.ooniprobe.test.test.Signal;
import org.openobservatory.ooniprobe.test.test.Telegram;
import org.openobservatory.ooniprobe.test.test.Whatsapp;

import java.util.Arrays;
import java.util.List;

public class InstantMessagingTestSuiteUtils {

    /**
     * Saves a result in the DB and returns it with the given number of measurements, and
     * all related model objects in the DB.
     *
     * @param accessibleMeasurements number of successful measurements
     * @param accessibleTestTypes list with successful measurements
     * @param blockedMeasurements  number of failed measurements
     * @param blockedTestTypes list with failed measurements
     * @throws IllegalArgumentException for excess number of measurements
     */
    public static void populateInstantMessagingMeasurements(
            int accessibleMeasurements, List<AbstractTest> accessibleTestTypes,
            int blockedMeasurements, List<AbstractTest> blockedTestTypes
    ) {
        List<AbstractTest> instantMessagingTestTypes = Arrays.asList(
                new FacebookMessenger(),
                new Telegram(),
                new Whatsapp(),
                new Signal()
        );

        // There's only 4 types of measurements for InstantMessagingSuite
        if (accessibleMeasurements + blockedMeasurements > 4) {
            throw new IllegalArgumentException(
                    "InstantMessagingSuite only has 4 possibilities of measurement, can't run"
                            + (accessibleMeasurements + blockedMeasurements)
            );
        }

        int typeCurrentIndex = 0;
        int maxMeasurements = Math.max(accessibleMeasurements, blockedMeasurements);

        if (maxMeasurements == accessibleMeasurements) {
            for (int i = 0; i < accessibleMeasurements; i++) {
                accessibleTestTypes.add(instantMessagingTestTypes.get(typeCurrentIndex++));
            }

            for (int i = 0; i < blockedMeasurements; i++) {
                blockedTestTypes.add(instantMessagingTestTypes.get(typeCurrentIndex++));
            }
        } else {
            for (int i = 0; i < blockedMeasurements; i++) {
                blockedTestTypes.add(instantMessagingTestTypes.get(typeCurrentIndex++));
            }

            for (int i = 0; i < accessibleMeasurements; i++) {
                accessibleTestTypes.add(instantMessagingTestTypes.get(typeCurrentIndex++));
            }
        }
    }

}
