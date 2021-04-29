package org.openobservatory.ooniprobe.ui.measurements;

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

public class UIMeasurementsUtils {

    private static final String SUCCESSFUL_OUTCOME = "Working";
    private static final String BLOCKED_OUTCOME = "Likely blocked";

    static void assertMeasurementHeader(Result result) {
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

    static void assertMeasurementOutcome(boolean wasSuccess) {
        String outcome = wasSuccess ? SUCCESSFUL_OUTCOME : BLOCKED_OUTCOME;
        onView(withId(R.id.outcome)).check(matches(withText(outcome)));
    }

    static void assertMeasurementRuntimeAndNetwork(Measurement measurement, Network network) {
        onView(withId(R.id.startTime)).check(matches(withText(FormattingUtils.formatStartTime(measurement.start_time))));
        onView(withId(R.id.runtime)).check(matches(withText(FormattingUtils.formatRunTime(measurement.runtime))));
        onView(withId(R.id.country)).check(matches(withText(network.country_code)));
        onView(withId(R.id.networkName)).check(matches(withText(network.network_name)));
        onView(withId(R.id.networkDetail)).check(matches(withText(containsString(network.asn))));
    }
}
