package org.openobservatory.ooniprobe.di;

import org.openobservatory.ooniprobe.common.Application;
import org.openobservatory.ooniprobe.di.Components.ActivityComponent;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApplicationModule.class
})
public interface AppComponent {

    ActivityComponent activityComponent();

    void inject(Application app);

}