package org.openobservatory.ooniprobe;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.openobservatory.ooniprobe.common.Application;

public class AbstractTest {
	protected Context c;
	protected Application a;

	@Before public void before() {
		c = InstrumentationRegistry.getInstrumentation().getTargetContext();
		a = (Application) c.getApplicationContext();
	}

	protected String getResourceString(int id) {
		Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
		return targetContext.getResources().getString(id);
	}

	protected void onlyRunForAutomationFlag() {
		// This assumption will be false, causing the test to halt and be ignored.
		org.junit.Assume.assumeTrue(BuildConfig.RUN_AUTOMATION);
	}
}
