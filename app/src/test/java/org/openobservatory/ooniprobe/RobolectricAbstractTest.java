package org.openobservatory.ooniprobe;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.raizlabs.android.dbflow.config.FlowManager;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.openobservatory.ooniprobe.common.Application;
import org.robolectric.RobolectricTestRunner;

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
}
