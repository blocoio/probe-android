package org.openobservatory.ooniprobe.ui.measurements;

import org.openobservatory.ooniprobe.AbstractTest;
import org.openobservatory.ooniprobe.R;
import org.openobservatory.ooniprobe.model.database.Measurement;
import org.openobservatory.ooniprobe.model.database.Network;
import org.openobservatory.ooniprobe.model.database.Result;
import org.openobservatory.ooniprobe.utils.FormattingUtils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

public class MeasurementScreen extends AbstractTest {

    protected final String SUCCESSFUL_MEASUREMENT
            = getResourceString(R.string.Modal_OK);

    protected final String BLOCKED_MEASUREMENT
            = getResourceString(R.string.TestResults_Overview_MiddleBoxes_Failed);

    protected final String TEST_RESULTS_NOT_AVAILABLE
            = getResourceString(R.string.Dashboard_Overview_LastRun_Never);

    protected final String TEST_RESULTS_AVAILABLE = SUCCESSFUL_MEASUREMENT;

    protected final String SUCCESSFUL_OUTCOME
            = getResourceString(R.string.TestResults_Summary_Circumvention_Hero_Reachable_Singular);

    protected final String BLOCKED_OUTCOME
            = getResourceString(R.string.TestResults_Details_Websites_LikelyBlocked_Hero_Title);

    void assertMeasurementHeader(Result result) {
        // Page 1
        onView(withId(R.id.tested)).check(matches(withText(String.valueOf(result.countTotalMeasurements()))));
        onView(withId(R.id.blocked)).check(matches(withText(String.valueOf(result.countAnomalousMeasurements()))));
        onView(withId(R.id.available)).check(matches(withText(String.valueOf(result.countOkMeasurements()))));

        // Page 2
        onView(withId(R.id.pager)).perform(swipeLeft());
        onView(withId(R.id.startTime)).check(matches(withText(FormattingUtils.formatStartTime(result.start_time))));
        onView(withId(R.id.download)).check(matches(withText(result.getFormattedDataUsageDown())));
        onView(withId(R.id.upload)).check(matches(withText(result.getFormattedDataUsageUp())));
        onView(withId(R.id.runtime)).check(matches(withText(FormattingUtils.formatRunTime(result.getRuntime()))));

        // Page 3
        onView(withId(R.id.pager)).perform(swipeLeft());
        onView(withText(result.network.country_code)).check(matches(isDisplayed()));
        onView(withText(containsString(result.network.network_name))).check(matches(isDisplayed()));
        onView(withText(containsString(result.network.asn))).check(matches(isDisplayed()));
    }

    void assertMeasurementOutcome(boolean wasSuccess) {
        String outcome = wasSuccess ? SUCCESSFUL_OUTCOME : BLOCKED_OUTCOME;
        onView(withId(R.id.outcome)).check(matches(withText(outcome)));
    }

    void assertMeasurementRuntimeAndNetwork(Measurement measurement, Network network) {
        onView(withId(R.id.startTime)).check(matches(withText(FormattingUtils.formatStartTime(measurement.start_time))));
        onView(withId(R.id.runtime)).check(matches(withText(FormattingUtils.formatRunTime(measurement.runtime))));
        onView(withId(R.id.country)).check(matches(withText(network.country_code)));
        onView(withId(R.id.networkName)).check(matches(withText(network.network_name)));
        onView(withId(R.id.networkDetail)).check(matches(withText(containsString(network.asn))));
    }

}
