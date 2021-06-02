package org.openobservatory.ooniprobe.activity;

import androidx.appcompat.app.AppCompatActivity;

import org.openobservatory.ooniprobe.common.Application;
import org.openobservatory.ooniprobe.di.ActivityComponent;
import org.openobservatory.ooniprobe.di.AppComponent;

public abstract class AbstractActivity extends AppCompatActivity {

	public Application getApp() {
		return ((Application) getApplication());
	}

	public AppComponent getComponent() {
		return getApp().getComponent();
	}

	public ActivityComponent getActivityComponent() {
		return getApp().getActivityComponent();
	}

	boolean isTestRunning() {
		return ((Application) getApplication()).isTestRunning();
	}
}
