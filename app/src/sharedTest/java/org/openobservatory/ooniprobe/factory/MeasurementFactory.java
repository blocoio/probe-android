package org.openobservatory.ooniprobe.factory;

import android.content.Context;

import org.apache.commons.io.FileUtils;
import org.openobservatory.ooniprobe.model.database.Measurement;
import org.openobservatory.ooniprobe.model.database.Result;
import org.openobservatory.ooniprobe.model.database.Url;
import org.openobservatory.ooniprobe.test.test.AbstractTest;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

import io.bloco.faker.Faker;

import static org.openobservatory.ooniprobe.factory.TestKeyFactory.getAccessibleStringFrom;
import static org.openobservatory.ooniprobe.factory.TestKeyFactory.getBlockedStringFrom;

public class MeasurementFactory {

    private static final Faker faker = new Faker();

    public static Measurement build(
            AbstractTest testType,
            Result result,
            Url url,
            boolean wasBlocked,
            boolean wasUploaded
    ) {

        Measurement temp = new Measurement();

        temp.id = faker.number.positive();
        temp.result = result;
        temp.is_done = true;
        temp.is_uploaded = wasUploaded;
        temp.is_failed = false;
        temp.is_upload_failed = false;
        temp.is_rerun = false;
        temp.is_anomaly = wasBlocked;
        temp.start_time = faker.date.forward();
        temp.test_name = testType.getName();
        temp.report_id = wasUploaded ? String.valueOf(result.id) : null;
        temp.test_keys = getTestKeyFrom(testType, wasBlocked);
        temp.runtime = faker.number.positive();
        temp.url = url;

        return temp;
    }

    public static Measurement buildWithName(String testName) {
        Measurement measurement = new Measurement();
        measurement.test_name = testName;
        measurement.start_time = new Date();
        return measurement;
    }

    private static String getTestKeyFrom(AbstractTest testType, boolean hasFailed) {
        if (hasFailed) {
            return getBlockedStringFrom(testType);
        }

        return getAccessibleStringFrom(testType);
    }

    public static void addEntryFiles(Context context, List<Measurement> measurements, Boolean markUploaded) {
        measurements.forEach(measurement -> {
            String entryName = String.format("%d_%d", faker.number.positive(), faker.number.positive());
            addEntryFile(context, entryName, measurement, markUploaded);
        });
    }

    public static boolean addEntryFile(Context context, String reportId, Measurement measurement, Boolean markUploaded) {
        try {
            //Simulating measurement done and uploaded
            measurement.report_id = reportId;
            measurement.is_done = true;
            measurement.is_uploaded = markUploaded;
            measurement.save();
            File entryFile = Measurement.getEntryFile(context, measurement.id, measurement.test_name);
            entryFile.getParentFile().mkdirs();
            FileUtils.writeStringToFile(
                    entryFile,
                    "test",
                    Charset.forName("UTF-8")
            );
        } catch (IOException e) {
            return false;
        }

        return true;
    }

}
