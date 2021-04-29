package org.openobservatory.ooniprobe.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openobservatory.ooniprobe.DatabaseUtils;
import org.openobservatory.ooniprobe.R;
import org.openobservatory.ooniprobe.ResultFactory;
import org.openobservatory.ooniprobe.activity.ResultDetailActivity;
import org.openobservatory.ooniprobe.model.database.Result;
import org.openobservatory.ooniprobe.test.suite.InstantMessagingSuite;

import java.text.DecimalFormat;
import java.util.Locale;

import tools.fastlane.screengrab.locale.LocaleTestRule;

import static androidx.test.espresso.Espresso.onView;
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

        String formattedStartTime = DateFormat.format(
                DateFormat.getBestDateTimePattern(Locale.getDefault(), "yMdHm"),
                testResult.start_time
        ).toString();

        String formattedRunTime = new DecimalFormat("#0.00").format(testResult.getRuntime());

        // Act / Assert
        launchActivity(testResult.id);

        // Page 1
        onView(withId(R.id.tested)).check(matches(withText(String.valueOf(testResult.countTotalMeasurements()))));
        onView(withId(R.id.blocked)).check(matches(withText(String.valueOf(testResult.countAnomalousMeasurements()))));
        onView(withId(R.id.available)).check(matches(withText(String.valueOf(testResult.countOkMeasurements()))));

        // Page 2
        onView(withId(R.id.pager)).perform(swipeLeft());
        onView(withId(R.id.startTime)).check(matches(withText(formattedStartTime)));
        onView(withId(R.id.download)).check(matches(withText(testResult.getFormattedDataUsageDown())));
        onView(withId(R.id.upload)).check(matches(withText(testResult.getFormattedDataUsageUp())));
        onView(withId(R.id.runtime)).check(matches(withText(formattedRunTime)));

        // Page 3
        onView(withId(R.id.pager)).perform(swipeLeft());
        onView(withText(testResult.network.country_code)).check(matches(isDisplayed()));
        onView(withText(containsString(testResult.network.network_name))).check(matches(isDisplayed()));
        onView(withText(containsString(testResult.network.asn))).check(matches(isDisplayed()));
    }

}
