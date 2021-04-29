package org.openobservatory.ooniprobe;

import org.openobservatory.ooniprobe.model.database.Measurement;
import org.openobservatory.ooniprobe.model.database.Network;
import org.openobservatory.ooniprobe.model.database.Result;
import org.openobservatory.ooniprobe.model.database.Url;
import org.openobservatory.ooniprobe.test.suite.AbstractSuite;
import org.openobservatory.ooniprobe.test.suite.CircumventionSuite;
import org.openobservatory.ooniprobe.test.suite.InstantMessagingSuite;
import org.openobservatory.ooniprobe.test.test.AbstractTest;
import org.openobservatory.ooniprobe.test.test.FacebookMessenger;
import org.openobservatory.ooniprobe.test.test.Psiphon;
import org.openobservatory.ooniprobe.test.test.RiseupVPN;
import org.openobservatory.ooniprobe.test.test.Signal;
import org.openobservatory.ooniprobe.test.test.Telegram;
import org.openobservatory.ooniprobe.test.test.Tor;
import org.openobservatory.ooniprobe.test.test.Whatsapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.bloco.faker.Faker;

import static org.openobservatory.ooniprobe.utils.TestSuiteUtils.populateMeasurements;

public class ResultFactory {

    private static final Faker faker = new Faker();

    private static final int DEFAULT_SUCCESS_MEASUREMENTS = 4;
    private static final int DEFAULT_FAILED_MEASUREMENTS = 0;

    public static Result build(AbstractSuite suite) {
        Result temp = new Result();
        temp.id = faker.number.positive();
        temp.test_group_name = suite.getName();
        temp.data_usage_down = faker.number.positive();
        temp.data_usage_up = faker.number.positive();
        temp.start_time = faker.date.forward();
        temp.is_done = true;

        temp.failure_msg = null;
        return temp;
    }

    /**
     * Saves a result in the DB and returns it with the 4 measurements, and all related model
     * objects in the DB.
     *
     * @param suite type of result (ex: Websites, Instant Messaging, Circumvention, Performance)
     * @return result with
     */
    public static Result createAndSave(AbstractSuite suite) {
        return createAndSave(suite, DEFAULT_SUCCESS_MEASUREMENTS, DEFAULT_FAILED_MEASUREMENTS);
    }

    /**
     * Saves a result in the DB and returns it with the given number of measurements, and
     * all related model objects in the DB.
     *
     * @param suite                  type of result (ex: Websites, Instant Messaging, Circumvention, Performance)
     * @param accessibleMeasurements number of accessible measurements
     * @param blockedMeasurements    number of blocked measurements
     * @return result with
     * @throws IllegalArgumentException for excess number of measurements
     */
    public static Result createAndSave(
            AbstractSuite suite,
            int accessibleMeasurements,
            int blockedMeasurements
    ) throws IllegalArgumentException {

        List<AbstractTest> accessibleTestTypes = new ArrayList<>();
        List<AbstractTest> blockedTestTypes = new ArrayList<>();

        if (suite instanceof InstantMessagingSuite || suite instanceof CircumventionSuite) {
            List<AbstractTest> measurementTestTypes = null;

            if (suite instanceof InstantMessagingSuite) {
                measurementTestTypes = Arrays.asList(
                        new FacebookMessenger(),
                        new Telegram(),
                        new Whatsapp(),
                        new Signal()
                );
            }

            if (suite instanceof CircumventionSuite) {
                measurementTestTypes = Arrays.asList(
                        new Psiphon(),
                        new Tor(),
                        new RiseupVPN()
                );
            }

            populateMeasurements(
                    measurementTestTypes,
                    accessibleMeasurements,
                    accessibleTestTypes,
                    blockedMeasurements,
                    blockedTestTypes
            );
        }

        return createAndSave(suite, accessibleTestTypes, blockedTestTypes);
    }

    private static Result createAndSave(
            AbstractSuite suite,
            List<AbstractTest> successTestTypes,
            List<AbstractTest> failedTestTypes
    ) {
        Result tempResult = ResultFactory.build(suite);

        Url tempUrl = UrlFactory.build();
        tempUrl.save();

        Network tempNetwork = NetworkFactory.build();
        tempResult.network = tempNetwork;
        tempNetwork.save();

        successTestTypes.forEach(type -> {
            Measurement temp = MeasurementFactory.build(type, tempResult, tempUrl, false);
            temp.save();
        });

        failedTestTypes.forEach(type -> {
            Measurement temp = MeasurementFactory.build(type, tempResult, tempUrl, true);
            temp.save();
        });

        tempResult.getMeasurements();
        tempResult.save();

        return tempResult;
    }

}
