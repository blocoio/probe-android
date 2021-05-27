package org.openobservatory.ooniprobe.test;

import org.junit.Test;
import org.openobservatory.engine.OONISession;
import org.openobservatory.engine.OONIURLInfo;
import org.openobservatory.engine.OONIURLListResult;
import org.openobservatory.ooniprobe.RobolectricAbstractTest;
import org.openobservatory.ooniprobe.common.service.RunTestService;
import org.openobservatory.ooniprobe.engine.TestEngineInterface;
import org.openobservatory.ooniprobe.factory.ResultFactory;
import org.openobservatory.ooniprobe.model.database.Result;
import org.openobservatory.ooniprobe.test.suite.AbstractSuite;
import org.openobservatory.ooniprobe.test.suite.WebsitesSuite;
import org.openobservatory.ooniprobe.test.test.AbstractTest;
import org.openobservatory.ooniprobe.test.test.WebConnectivity;
import org.openobservatory.ooniprobe.utils.DatabaseUtils;

import java.util.ArrayList;
import java.util.Arrays;

import io.bloco.faker.Faker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestAsyncTaskTest extends RobolectricAbstractTest {

    RunTestService runServiceMock = mock(RunTestService.class);


    @Test public void storesTestSuitInDb() {
        // Arrange
        ArrayList<AbstractSuite> suiteList = new ArrayList<>();
        AbstractSuite mockedSuite = mock(WebsitesSuite.class);
        suiteList.add(mockedSuite);
        TestAsyncTask task = new TestAsyncTask(a, suiteList, runServiceMock);
        Result testResult = ResultFactory.build(new WebsitesSuite(), true);

        when(mockedSuite.getTestList(any())).thenReturn(new AbstractTest[0]);

        when(mockedSuite.getResult()).thenReturn(testResult);

        // Act
        task.execute();
        idleTaskUntilFinished(task);

        Result databaseResult = DatabaseUtils.findResult(testResult.id);

        // Assert
        assertNotNull(databaseResult);
        assertEquals(databaseResult.id, testResult.id);
        assertFalse(databaseResult.is_viewed);
    }

    @Test public void downloadsWebSuitUrls() throws Exception {
        // Arrange
        Faker faker = new Faker();
        OONISession ooniSessionMock = mock(OONISession.class);
        TestEngineInterface mockedEngine = new TestEngineInterface(ooniSessionMock);
        EngineProvider.engineInterface = mockedEngine;

        ArrayList<AbstractSuite> suiteList = new ArrayList<>();
        AbstractSuite mockedSuite = mock(WebsitesSuite.class);
        suiteList.add(mockedSuite);
        TestAsyncTask task = new TestAsyncTask(a, suiteList, runServiceMock);
        Result testResult = ResultFactory.build(new WebsitesSuite(), true);

        WebConnectivity test = new WebConnectivity();
        test.setInputs(null);

        when(mockedSuite.getTestList(any())).thenReturn(new WebConnectivity[]{test});
        when(mockedSuite.getResult()).thenReturn(testResult);

        OONIURLListResult listResult = mock(OONIURLListResult.class);
        when(ooniSessionMock.fetchURLList(any(),any())).thenReturn(listResult);

        String url1 = faker.internet.url();
        OONIURLInfo firstUrl = mock(OONIURLInfo.class);
        when(firstUrl.getUrl()).thenReturn(url1);
        when(firstUrl.getCategoryCode()).thenReturn("");
        when(firstUrl.getCountryCode()).thenReturn(faker.address.countryCode());

        String url2 = faker.internet.url();
        OONIURLInfo secondUrl = mock(OONIURLInfo.class);
        when(secondUrl.getUrl()).thenReturn(url2);
        when(secondUrl.getCategoryCode()).thenReturn("");
        when(secondUrl.getCountryCode()).thenReturn(faker.address.countryCode());

        String url3 = faker.internet.url();
        OONIURLInfo thirdUrl = mock(OONIURLInfo.class);
        when(thirdUrl.getUrl()).thenReturn(url3);
        when(thirdUrl.getCategoryCode()).thenReturn("");
        when(thirdUrl.getCountryCode()).thenReturn(faker.address.countryCode());

        when(listResult.getUrls()).thenReturn(new ArrayList<>(Arrays.asList(firstUrl, secondUrl, thirdUrl)));

        // Act
        mockedEngine.isTaskDone = true;

        task.execute();
        idleTaskUntilFinished(task);

        // Assert
        assertEquals(testResult.id, testResult.id);
        assertEquals(test.getInputs().size(), 3);
        assertEquals(test.getInputs().get(0), url1);
        assertEquals(test.getInputs().get(1), url2);
        assertEquals(test.getInputs().get(2), url3);
    }
}