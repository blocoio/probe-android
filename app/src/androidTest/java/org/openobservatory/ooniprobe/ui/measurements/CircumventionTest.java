package org.openobservatory.ooniprobe.ui.measurements;

import android.content.Intent;
import android.os.Bundle;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.openobservatory.ooniprobe.R;
import org.openobservatory.ooniprobe.ResultFactory;
import org.openobservatory.ooniprobe.activity.ResultDetailActivity;
import org.openobservatory.ooniprobe.model.database.Measurement;
import org.openobservatory.ooniprobe.model.database.Result;
import org.openobservatory.ooniprobe.test.suite.CircumventionSuite;
import org.openobservatory.ooniprobe.utils.DatabaseUtils;
import org.openobservatory.ooniprobe.utils.FormattingUtils;

import tools.fastlane.screengrab.locale.LocaleTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class CircumventionTest extends MeasurementScreen {

    @ClassRule
    public static final LocaleTestRule localeTestRule = new LocaleTestRule();

    public ActivityScenario<ResultDetailActivity> scenario;

    @Before
    public void setUp() {
        DatabaseUtils.resetDatabase();
    }

    private void launchActivity(int resultId) {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ResultDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", resultId);
        intent.putExtras(bundle);

        scenario = ActivityScenario.launch(intent);
    }

    @Test
    public void testHeaderData() {
        // Arrange
        Result testResult = ResultFactory.createAndSave(new CircumventionSuite(), 3, 0);

        // Act
        launchActivity(testResult.id);

        // Assert
        assertMeasurementHeader(testResult);
    }

    @Test
    public void testSuccessPsiphon() {
        // Arrange
        Result testResult = ResultFactory.createAndSave(new CircumventionSuite(), 3, 0);
        Measurement measurement = testResult.getMeasurement("psiphon");
        String formattedBootstrap = FormattingUtils.formatBootstrap(measurement.getTestKeys().bootstrap_time);

        // Act
        launchActivity(testResult.id);
        onView(withText("Psiphon Test")).check(matches(isDisplayed())).perform(click());

        // Assert
        assertMeasurementOutcome(true);
        onView(withId(R.id.bootstrap)).check(matches(withText(formattedBootstrap)));
        assertMeasurementRuntimeAndNetwork(measurement, testResult.network);
    }

    @Test
    public void testBlockedPsiphon() {
        // Arrange
        Result testResult = ResultFactory.createAndSave(new CircumventionSuite(), 0, 3);
        Measurement measurement = testResult.getMeasurement("psiphon");

        // Act
        launchActivity(testResult.id);
        onView(withText("Psiphon Test")).check(matches(isDisplayed())).perform(click());

        // Assert
        assertMeasurementOutcome(false);
        onView(withId(R.id.bootstrap)).check(matches(withText(TEST_RESULTS_NOT_AVAILABLE)));
        assertMeasurementRuntimeAndNetwork(measurement, testResult.network);
    }

    @Test
    public void testSuccessTor() {
        // Arrange
        Result testResult = ResultFactory.createAndSave(new CircumventionSuite(), 3, 0);
        Measurement measurement = testResult.getMeasurement("tor");

        String formattedBridges = FormattingUtils.getFormattedBridges(measurement);
        String formattedAuthorities = FormattingUtils.getFormattedAuthorities(measurement);

        // Act
        launchActivity(testResult.id);
        onView(withText("Tor Test")).check(matches(isDisplayed())).perform(click());

        // Assert
        assertMeasurementOutcome(true);
        onView(withId(R.id.bridges)).check(matches(withText(formattedBridges)));
        onView(withId(R.id.authorities)).check(matches(withText(formattedAuthorities)));
        assertMeasurementRuntimeAndNetwork(measurement, testResult.network);
    }

    @Test
    public void testBlockedTor() {
        // Arrange
        Result testResult = ResultFactory.createAndSave(new CircumventionSuite(), 0, 3);
        Measurement measurement = testResult.getMeasurement("tor");

        // Act
        launchActivity(testResult.id);
        onView(withText("Tor Test")).check(matches(isDisplayed())).perform(click());

        // Assert
        assertMeasurementOutcome(false);
        onView(withId(R.id.bridges)).check(matches(withText(TEST_RESULTS_NOT_AVAILABLE)));
        onView(withId(R.id.authorities)).check(matches(withText(TEST_RESULTS_NOT_AVAILABLE)));
        assertMeasurementRuntimeAndNetwork(measurement, testResult.network);
    }

    @Test
    public void testSuccessfulRiseUpVPN() {
        // Arrange
        Result testResult = ResultFactory.createAndSave(new CircumventionSuite(), 3, 0);
        Measurement measurement = testResult.getMeasurement("riseupvpn");

        // Act
        launchActivity(testResult.id);
        onView(withText("RiseupVPN Test")).check(matches(isDisplayed())).perform(click());

        // Assert
        assertMeasurementOutcome(true);
        onView(withId(R.id.bootstrap_value)).check(matches(withText(TEST_RESULTS_AVAILABLE)));
        onView(withId(R.id.openvpn_value)).check(matches(withText(TEST_RESULTS_AVAILABLE)));
        onView(withId(R.id.bridges_value)).check(matches(withText(TEST_RESULTS_AVAILABLE)));
        assertMeasurementRuntimeAndNetwork(measurement, testResult.network);
    }

    @Test
    public void testBlockedRiseUpVPN() {
        // Arrange
        Result testResult = ResultFactory.createAndSave(new CircumventionSuite(), 0, 3);
        Measurement measurement = testResult.getMeasurement("riseupvpn");

        // Act
        launchActivity(testResult.id);
        onView(withText("RiseupVPN Test")).check(matches(isDisplayed())).perform(click());

        // Assert
        assertMeasurementOutcome(false);
        onView(withId(R.id.bootstrap_value)).check(matches(withText("Blocked")));
        onView(withId(R.id.openvpn_value)).check(matches(withText("1 blocked")));
        onView(withId(R.id.bridges_value)).check(matches(withText("1 blocked")));
        assertMeasurementRuntimeAndNetwork(measurement, testResult.network);
    }

}
