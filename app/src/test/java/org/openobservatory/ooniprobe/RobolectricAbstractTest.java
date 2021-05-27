package org.openobservatory.ooniprobe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Looper;

import androidx.test.core.app.ApplicationProvider;

import com.raizlabs.android.dbflow.config.FlowManager;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.openobservatory.ooniprobe.common.Application;
import org.openobservatory.ooniprobe.factory.TestApplication;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Calendar;
import java.util.Date;

import static org.robolectric.Shadows.shadowOf;

@Config(application = TestApplication.class)
@RunWith(RobolectricTestRunner.class)
public abstract class RobolectricAbstractTest {
    protected Context c;
    protected Application a;

    @Before
    public void setUp() {
        a = ApplicationProvider.getApplicationContext();
        c = a;
    }

    @After
    public void tearDown() {
        FlowManager.destroy();
    }

    public <T extends Activity> T buildActivity(Class<T> activityClass, Intent intent) {
        return Robolectric.buildActivity(activityClass, intent)
                .create()
                .get();
    }

    @SuppressWarnings("rawtypes")
    public void idleTaskUntilFinished(AsyncTask task) {
        try {
            while (task.getStatus() != AsyncTask.Status.FINISHED) {
                shadowOf(Looper.getMainLooper()).idle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
