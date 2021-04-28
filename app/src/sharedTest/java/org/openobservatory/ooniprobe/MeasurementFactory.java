package org.openobservatory.ooniprobe;

import org.openobservatory.ooniprobe.model.database.Measurement;
import org.openobservatory.ooniprobe.model.database.Result;
import org.openobservatory.ooniprobe.model.database.Url;
import org.openobservatory.ooniprobe.test.test.AbstractTest;


import io.bloco.faker.Faker;

public class MeasurementFactory {

    private static final Faker faker = new Faker();

    public static Measurement build(
            AbstractTest testType,
            Result result,
            Url url,
            boolean hasFailed
    ) {

        Measurement temp = new Measurement();

        temp.id = faker.number.positive();
        temp.result = result;
        temp.is_done = true;
        temp.is_uploaded = true;
        temp.is_failed = hasFailed;
        temp.failure_msg = hasFailed ? "error" : null;
        temp.is_upload_failed = false;
        temp.is_rerun = false;
        temp.is_anomaly = false;
        temp.start_time = faker.date.forward();
        temp.test_name = testType.getName();
        temp.report_id = String.valueOf(result.id);
        temp.test_keys = getTestKeyFrom(testType, hasFailed);
        temp.runtime = faker.number.positive();
        temp.url = url;

        return temp;
    }

    private static String getTestKeyFrom(AbstractTest testType, boolean hasFailed) {
        if (hasFailed) {
            return getFailedTestKey(testType);
        }

        return getSuccessTestKeyFrom(testType);
    }

    private static String getSuccessTestKeyFrom(AbstractTest testType) {
        String result;
        switch (testType.getName()) {
            case "whatsapp":
                result = "{\"registration_server_status\":\"ok\",\"whatsapp_endpoints_status\":\"ok\",\"whatsapp_web_status\":\"ok\"}";
                break;

            default: result = "{}";
        }

        return result;
    }

    private static String getFailedTestKey(AbstractTest testType) {
        String result;
        switch (testType.getName()) {
            case "whatsapp":
                result = "{\"registration_server_status\":\"blocked\",\"whatsapp_endpoints_status\":\"blocked\",\"whatsapp_web_status\":\"blocked\"}";
                break;

            default: result = "{}";
        }

        return result;
    }

}
