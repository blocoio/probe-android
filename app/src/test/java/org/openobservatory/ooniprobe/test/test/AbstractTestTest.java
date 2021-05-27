package org.openobservatory.ooniprobe.test.test;

import com.google.gson.Gson;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.junit.Test;
import org.openobservatory.engine.OONISession;
import org.openobservatory.ooniprobe.R;
import org.openobservatory.ooniprobe.RobolectricAbstractTest;
import org.openobservatory.ooniprobe.common.PreferenceManager;
import org.openobservatory.ooniprobe.engine.TestEngineInterface;
import org.openobservatory.ooniprobe.factory.EventResultFactory;
import org.openobservatory.ooniprobe.factory.JsonResultFactory;
import org.openobservatory.ooniprobe.factory.ResultFactory;
import org.openobservatory.ooniprobe.factory.UrlFactory;
import org.openobservatory.ooniprobe.model.database.Measurement;
import org.openobservatory.ooniprobe.model.database.Result;
import org.openobservatory.ooniprobe.model.database.Result_Table;
import org.openobservatory.ooniprobe.model.database.Url;
import org.openobservatory.ooniprobe.model.jsonresult.JsonResult;
import org.openobservatory.ooniprobe.model.settings.Settings;
import org.openobservatory.ooniprobe.test.EngineProvider;
import org.openobservatory.ooniprobe.test.suite.WebsitesSuite;

import java.io.IOException;

import io.bloco.faker.Faker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.openobservatory.ooniprobe.test.test.AbstractTest.TestCallback;

public class AbstractTestTest extends RobolectricAbstractTest {

    Faker faker = new Faker();

    PreferenceManager mockPreferenceManager = mock(PreferenceManager.class);
    TestCallback mockedCallback = mock(TestCallback.class);
    OONISession mockedSession = mock(OONISession.class);
    Settings mockedSettings = mock(Settings.class);
    Gson gson = new Gson();

    TestEngineInterface testEngine = new TestEngineInterface(mockedSession);

    @Override
    public void setUp() {
        super.setUp();
        EngineProvider.engineInterface = testEngine;

        try {
            when(mockedSettings.toExperimentSettings(any(), any())).thenReturn(null);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testStartFinish() {
        // Arrange
        AbstractTest test = new WebConnectivity();
        Result result = ResultFactory.createAndSave(new WebsitesSuite());

        // Act
        testEngine.sendNextEvent(EventResultFactory.buildStarted());
        testEngine.sendNextEvent(EventResultFactory.buildEnded());

        test.run(c, mockPreferenceManager, gson, mockedSettings, result, 1, mockedCallback);

        // Assert
        verify(mockedCallback, times(1)).onStart(c.getString(R.string.Test_WebConnectivity_Fullname));
        verify(mockedCallback, times(1)).onProgress(100);
    }

    @Test
    public void testMeasurementStart() {
        // Arrange
        AbstractTest test = new WebConnectivity();
        Result result = ResultFactory.build(new WebsitesSuite(), true, false);
        result.save();

        Url ulr = UrlFactory.createAndSave();

        JsonResult jsonResult = JsonResultFactory.build(test, true);

        String measurementName = test.getName();
        String measurementUrl = ulr.url;
        String reportId = "1000_00001";
        String jsonResultString = gson.toJson(jsonResult);
        int measurementId = 1;
        double measurementDownload = 10;
        double measurementUpload = 15;

        // Act
        testEngine.sendNextEvent(EventResultFactory.buildStarted());
        testEngine.sendNextEvent(EventResultFactory.buildCreateReport(reportId));
        testEngine.sendNextEvent(EventResultFactory.buildMeasurementStart(measurementId, measurementUrl));
        testEngine.sendNextEvent(EventResultFactory.buildMeasurementEntry(measurementId, jsonResultString));
        testEngine.sendNextEvent(EventResultFactory.buildMeasurementUpload(measurementId, false));
        testEngine.sendNextEvent(EventResultFactory.buildMeasurementDone(measurementId));
        testEngine.sendNextEvent(EventResultFactory.buildDataUsage(measurementDownload, measurementUpload));
        testEngine.sendNextEvent(EventResultFactory.buildEnded());

        test.run(c, mockPreferenceManager, gson, mockedSettings, result, 1, mockedCallback);

        // Assert
        verify(mockedCallback, times(1)).onStart(c.getString(R.string.Test_WebConnectivity_Fullname));
        verify(mockedCallback, times(1)).onProgress(100);

        Result updatedResult = SQLite.select().from(Result.class).where(Result_Table.id.eq(result.id)).querySingle();
        assertNotNull(updatedResult);

        Measurement measurement = updatedResult.getMeasurements().get(0);
        assertNotNull(measurement);

        assertTrue(updatedResult.is_done);
        assertEquals(updatedResult.data_usage_down, measurementDownload, 0);
        assertEquals(updatedResult.data_usage_up, measurementUpload, 0);
        assertEquals(updatedResult.start_time, jsonResult.test_start_time);

        assertEquals(measurement.test_name, measurementName);
        assertEquals(measurement.url.url, measurementUrl);
        assertEquals(measurement.start_time, jsonResult.measurement_start_time);
        assertEquals(measurement.runtime, jsonResult.test_runtime, 0);
        assertEquals(measurement.report_id, reportId);
        assertTrue(measurement.is_uploaded);
        assertFalse(measurement.is_anomaly);
    }
}