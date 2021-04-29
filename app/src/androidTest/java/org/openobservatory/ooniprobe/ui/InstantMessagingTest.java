package org.openobservatory.ooniprobe.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openobservatory.ooniprobe.DatabaseUtils;
import org.openobservatory.ooniprobe.MeasurementFactory;
import org.openobservatory.ooniprobe.R;
import org.openobservatory.ooniprobe.ResultFactory;
import org.openobservatory.ooniprobe.UrlFactory;
import org.openobservatory.ooniprobe.activity.MainActivity;
import org.openobservatory.ooniprobe.activity.ResultDetailActivity;
import org.openobservatory.ooniprobe.model.database.Measurement;
import org.openobservatory.ooniprobe.model.database.Network;
import org.openobservatory.ooniprobe.model.database.Result;
import org.openobservatory.ooniprobe.model.database.Url;
import org.openobservatory.ooniprobe.test.suite.InstantMessagingSuite;

import tools.fastlane.screengrab.locale.LocaleTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.openobservatory.ooniprobe.ResultFactory.GroupName.INSTANT_MESSAGING;

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
        Result testResult = ResultFactory.createAndSave(new InstantMessagingSuite(), 3, 1);

        // Act
        launchActivity(testResult.id);

        // Assert
        onView(withId(R.id.pager)).perform(swipeLeft());
        onView(withId(R.id.download)).check(matches(withText(testResult.getFormattedDataUsageDown())));
    }

}
