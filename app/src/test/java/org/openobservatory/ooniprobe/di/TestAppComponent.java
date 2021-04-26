package org.openobservatory.ooniprobe.di;

import org.openobservatory.ooniprobe.di.Components.ActivityComponent;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        TestAppModule.class
})
public interface TestAppComponent extends AppComponent{

    ActivityComponent activityComponent();

    void inject(TestApplication app);

}
