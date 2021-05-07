package org.openobservatory.ooniprobe.activity;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ServiceTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openobservatory.ooniprobe.AbstractTest;
import org.openobservatory.ooniprobe.common.service.RunTestService;
import org.openobservatory.ooniprobe.engine.TestEngineInterface;
import org.openobservatory.ooniprobe.model.jsonresult.EventResult;
import org.openobservatory.ooniprobe.test.EngineProvider;
import org.openobservatory.ooniprobe.test.suite.AbstractSuite;
import org.openobservatory.ooniprobe.test.suite.WebsitesSuite;
import org.openobservatory.ooniprobe.utils.DatabaseUtils;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import static org.openobservatory.ooniprobe.testing.ActivityAssertions.assertCurrentActivity;
import static org.openobservatory.ooniprobe.testing.ActivityAssertions.waitForCurrentActivity;

@RunWith(AndroidJUnit4.class)
public class RunningActivityTest extends AbstractTest {

    @Rule
    public ServiceTestRule serviceRule = new ServiceTestRule();

    private ActivityScenario<RunningActivity> scenario;
    private final TestEngineInterface testEngine = new TestEngineInterface();

    @Before
    public void setUp() {
        DatabaseUtils.resetDatabase();
        EngineProvider.engineInterface = testEngine;
        a.getPreferenceManager().setShowOnboarding(false);
        a.getPreferenceManager().setAppOpenCount(0L);
    }

    @After
    public void tearDown() {
        scenario.close();
        serviceRule.unbindService();
    }

    @Test
    public void startAndDone() {
        launch();
        assertCurrentActivity(RunningActivity.class);

        EventResult start = new EventResult();
        start.key = "status.started";
        testEngine.sendNextEvent(start);

        EventResult done = new EventResult();
        done.key = "status.end";
        testEngine.sendNextEvent(done);

        testEngine.isTaskDone = true;

        waitForCurrentActivity(MainActivity.class);
    }

    private void launch() {
        startRunTestService();
        scenario = ActivityScenario.launch(RunningActivity.class);
    }

    private void startRunTestService() {
        try {
            serviceRule.startService(
                    new Intent(c, RunTestService.class)
                            .putExtra("testSuites", new ArrayList<AbstractSuite>() {{
                                add(new WebsitesSuite());
                            }})
            );
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
