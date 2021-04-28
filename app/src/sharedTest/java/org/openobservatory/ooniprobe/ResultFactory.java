package org.openobservatory.ooniprobe;

import org.openobservatory.ooniprobe.model.database.Measurement;
import org.openobservatory.ooniprobe.model.database.Network;
import org.openobservatory.ooniprobe.model.database.Result;
import org.openobservatory.ooniprobe.model.database.Url;
import org.openobservatory.ooniprobe.test.suite.AbstractSuite;
import org.openobservatory.ooniprobe.test.suite.InstantMessagingSuite;
import org.openobservatory.ooniprobe.test.test.AbstractTest;
import org.openobservatory.ooniprobe.test.test.FacebookMessenger;
import org.openobservatory.ooniprobe.test.test.Signal;
import org.openobservatory.ooniprobe.test.test.Telegram;
import org.openobservatory.ooniprobe.test.test.Whatsapp;

import java.util.ArrayList;
import java.util.List;

import io.bloco.faker.Faker;

public class ResultFactory {

    private static final Faker faker = new Faker();

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

    public static Result createAndSave(AbstractSuite suite) {

        List<AbstractTest> testTypes = new ArrayList<>();

        if (suite instanceof InstantMessagingSuite) {
            testTypes.add(new FacebookMessenger());
            testTypes.add(new Telegram());
            testTypes.add(new Whatsapp());
            testTypes.add(new Signal());
        }

        return createAndSave(suite, testTypes);
    }

    private static Result createAndSave(AbstractSuite suite, List<AbstractTest> testTypes) {
        Result tempResult = ResultFactory.build(suite);

        Url tempUrl = UrlFactory.build();
        tempUrl.save();

        Network tempNetwork = NetworkFactory.build();
        tempResult.network = tempNetwork;
        tempNetwork.save();

        testTypes.forEach(type -> {
            Measurement temp = MeasurementFactory.build(type, tempResult, tempUrl, false);
            temp.save();
        });


        tempResult.getMeasurements();
        tempResult.save();

        return tempResult;
    }

    public enum GroupName {
        INSTANT_MESSAGING;
    }

}
