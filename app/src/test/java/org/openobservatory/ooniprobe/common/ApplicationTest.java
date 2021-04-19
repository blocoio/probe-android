package org.openobservatory.ooniprobe.common;

import androidx.test.filters.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openobservatory.ooniprobe.RobolectricAbstractTest;
import org.openobservatory.ooniprobe.di.TestAppComponent;
import org.openobservatory.ooniprobe.di.TestApplication;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.openobservatory.ooniprobe.di.TestAppComponent;
import org.openobservatory.ooniprobe.di.TestApplication;


@SmallTest
public class ApplicationTest extends RobolectricAbstractTest {
    @Test
    public void packageName() {
        assertEquals("org.openobservatory.ooniprobe.dev", a.getPackageName());
    }

    @Test
    public void testApp() {
        assertTrue(a instanceof TestApplication);
    }
    @Test
    public void component() {
        assertTrue(a.component instanceof TestAppComponent);
    }
}
