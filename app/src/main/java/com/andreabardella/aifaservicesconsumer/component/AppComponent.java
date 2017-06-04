package com.andreabardella.aifaservicesconsumer.component;

import com.andreabardella.aifaservicesconsumer.module.ActivitiesBindingModule;
import com.andreabardella.aifaservicesconsumer.App;
import com.andreabardella.aifaservicesconsumer.module.ApiServicesModule;
import com.andreabardella.aifaservicesconsumer.module.AppModule;
import com.andreabardella.aifaservicesconsumer.module.JsonModule;
import com.andreabardella.aifaservicesconsumer.module.NetModule;
import com.andreabardella.aifaservicesconsumer.base.component.SubComponentBuildersProvider;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        JsonModule.class,
        NetModule.class,
        ApiServicesModule.class,
        ActivitiesBindingModule.class
})
public interface AppComponent extends SubComponentBuildersProvider<App> {

//    Application inject(Application application);

}
