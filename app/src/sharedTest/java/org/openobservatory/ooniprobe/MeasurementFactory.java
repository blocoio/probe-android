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
            boolean wasBlocked
    ) {

        Measurement temp = new Measurement();

        temp.id = faker.number.positive();
        temp.result = result;
        temp.is_done = true;
        temp.is_uploaded = true;
        temp.is_failed = false;
        temp.is_upload_failed = false;
        temp.is_rerun = false;
        temp.is_anomaly = wasBlocked;
        temp.start_time = faker.date.forward();
        temp.test_name = testType.getName();
        temp.report_id = String.valueOf(result.id);
        temp.test_keys = getTestKeyFrom(testType, wasBlocked);
        temp.runtime = faker.number.positive();
        temp.url = url;

        return temp;
    }

    private static String getTestKeyFrom(AbstractTest testType, boolean hasFailed) {
        if (hasFailed) {
            return getBlockedTestKeyFrom(testType);
        }

        return getAccessibleTestKeyFrom(testType);
    }

    private static String getAccessibleTestKeyFrom(AbstractTest testType) {
        String result;
        switch (testType.getName()) {
            case "whatsapp":
                result = "{\"registration_server_status\":\"ok\",\"whatsapp_endpoints_status\":\"ok\",\"whatsapp_web_status\":\"ok\"}";
                break;
            case "telegram":
                result = "{\"telegram_http_blocking\":\"false\",\"telegram_tcp_blocking\":\"false\",\"telegram_web_status\":\"ok\"}";
                break;
            case "facebook_messenger":
                result = "{\"facebook_tcp_blocking\":\"false\",\"facebook_dns_blocking\":\"ok\"}";
                break;
            case "signal":
                result = "{\"signal_backend_status\":\"ok\",\"signal_backend_failure\":\"ok\"}";
                break;

            default: result = "{}";
        }

        return result;
    }

    private static String getBlockedTestKeyFrom(AbstractTest testType) {
        String result;
        switch (testType.getName()) {
            case "whatsapp":
                result = "{\"registration_server_status\":\"blocked\",\"whatsapp_endpoints_status\":\"blocked\",\"whatsapp_web_status\":\"blocked\"}";
                break;
            case "telegram":
                result = "{\"telegram_http_blocking\":\"true\",\"telegram_tcp_blocking\":\"true\",\"telegram_web_status\":\"blocked\"}";
                break;
            case "facebook_messenger":
                result = "{\"facebook_tcp_blocking\":\"true\",\"facebook_dns_blocking\":\"true\"}";
                break;
            case "signal":
                result = "{\"signal_backend_status\":\"blocked\",\"signal_backend_failure\":\"blocked\"}";
                break;

            default: result = "{}";
        }

        return result;
    }

}
