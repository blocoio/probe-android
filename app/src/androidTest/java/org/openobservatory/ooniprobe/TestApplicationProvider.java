package org.openobservatory.ooniprobe;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

public class TestApplicationProvider {
    public static Context context() {
        return InstrumentationRegistry.getInstrumentation()
                .getTargetContext().getApplicationContext();
    }

    public static TestApplication app() {
        return (TestApplication) context();
    }
}
