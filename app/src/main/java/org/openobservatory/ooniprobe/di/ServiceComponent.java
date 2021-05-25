package org.openobservatory.ooniprobe.di;

import org.openobservatory.ooniprobe.common.ResubmitTask;
import org.openobservatory.ooniprobe.common.service.RunTestService;
import org.openobservatory.ooniprobe.common.service.ServiceUtil;
import org.openobservatory.ooniprobe.di.annotations.PerService;

import dagger.Subcomponent;

@PerService
@Subcomponent()
public interface ServiceComponent {
    void inject(ResubmitTask.Dependencies dependencies);
    void inject(ServiceUtil.Dependencies dependencies);
    void inject(RunTestService service);

}