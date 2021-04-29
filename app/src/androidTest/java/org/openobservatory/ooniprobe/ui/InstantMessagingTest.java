package org.openobservatory.ooniprobe.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openobservatory.ooniprobe.DatabaseUtils;
import org.openobservatory.ooniprobe.FormattingUtils;
import org.openobservatory.ooniprobe.R;
import org.openobservatory.ooniprobe.ResultFactory;
import org.openobservatory.ooniprobe.activity.ResultDetailActivity;
import org.openobservatory.ooniprobe.model.database.Measurement;
import org.openobservatory.ooniprobe.model.database.Result;
import org.openobservatory.ooniprobe.test.suite.InstantMessagingSuite;

import tools.fastlane.screengrab.locale.LocaleTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

@RunWith(AndroidJUnit4.class)
public class InstantMessagingTest {

    @ClassRule
    public static final LocaleTestRule localeTestRule = new LocaleTestRule();

    public ActivityScenario<ResultDetailActivity> scenario;

    @Before
    public void setUp() {
        DatabaseUtils.resetDatabase();
    }

    public void launchActivity(int resultId) {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ResultDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", resultId);
        intent.putExtras(bundle);

        scenario = ActivityScenario.launch(intent);
    }

    @Test
    public void testHeaderData() {
        // Arrange
        Result testResult = ResultFactory.createAndSave(new InstantMessagingSuite());

        // Act / Assert
        launchActivity(testResult.id);

        // Page 1
        onView(withId(R.id.tested)).check(matches(withText(String.valueOf(testResult.countTotalMeasurements()))));
        onView(withId(R.id.blocked)).check(matches(withText(String.valueOf(testResult.countAnomalousMeasurements()))));
        onView(withId(R.id.available)).check(matches(withText(String.valueOf(testResult.countOkMeasurements()))));

        // Page 2
        onView(withId(R.id.pager)).perform(swipeLeft());
        onView(withId(R.id.startTime)).check(matches(withText(FormattingUtils.formatStartTime(testResult.start_time))));
        onView(withId(R.id.download)).check(matches(withText(testResult.getFormattedDataUsageDown())));
        onView(withId(R.id.upload)).check(matches(withText(testResult.getFormattedDataUsageUp())));
        onView(withId(R.id.runtime)).check(matches(withText(FormattingUtils.formatRunTime(testResult.getRuntime()))));

        // Page 3
        onView(withId(R.id.pager)).perform(swipeLeft());
        onView(withText(testResult.network.country_code)).check(matches(isDisplayed()));
        onView(withText(containsString(testResult.network.network_name))).check(matches(isDisplayed()));
        onView(withText(containsString(testResult.network.asn))).check(matches(isDisplayed()));
    }

    @Test
    public void testSuccessWhatsApp() {
        // Arrange
        Result testResult = ResultFactory.createAndSave(new InstantMessagingSuite());
        Measurement measurement = testResult.getMeasurement("whatsapp");

        // Act
        launchActivity(testResult.id);

        // Assert
        onView(withText("WhatsApp Test"))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.outcome)).check(matches(withText("Working")));
        onView(withId(R.id.application)).check(matches(withText("OK")));
        onView(withId(R.id.webApp)).check(matches(withText("OK")));
        onView(withId(R.id.registrations)).check(matches(withText("OK")));

        onView(withId(R.id.startTime)).check(matches(withText(FormattingUtils.formatStartTime(measurement.start_time))));
        onView(withId(R.id.runtime)).check(matches(withText(FormattingUtils.formatRunTime(measurement.runtime))));
        onView(withId(R.id.country)).check(matches(withText(testResult.network.country_code)));
        onView(withId(R.id.networkName)).check(matches(withText(testResult.network.network_name)));
        onView(withId(R.id.networkDetail)).check(matches(withText(containsString(testResult.network.asn))));
    }

    @Test
    public void testBlockedWhatsApp() {
        // Arrange
        Result testResult = ResultFactory.createAndSave(new InstantMessagingSuite(), 0, 4);
        Measurement measurement = testResult.getMeasurement("whatsapp");

        // Act
        launchActivity(testResult.id);

        // Assert
        onView(withText("WhatsApp Test"))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.outcome)).check(matches(withText("Likely blocked")));
        onView(withId(R.id.application)).check(matches(withText("Failed")));
        onView(withId(R.id.webApp)).check(matches(withText("Failed")));
        onView(withId(R.id.registrations)).check(matches(withText("Failed")));

        onView(withId(R.id.startTime)).check(matches(withText(FormattingUtils.formatStartTime(measurement.start_time))));
        onView(withId(R.id.runtime)).check(matches(withText(FormattingUtils.formatRunTime(measurement.runtime))));
        onView(withId(R.id.country)).check(matches(withText(testResult.network.country_code)));
        onView(withId(R.id.networkName)).check(matches(withText(testResult.network.network_name)));
        onView(withId(R.id.networkDetail)).check(matches(withText(containsString(testResult.network.asn))));
    }

}
