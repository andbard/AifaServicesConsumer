package com.andreabardella.aifaservicesconsumer.module;

import android.app.Application;

import com.andreabardella.aifaservicesconsumer.BuildConfig;
import com.andreabardella.aifaservicesconsumer.api.ApiServicesManager;
import com.andreabardella.aifaservicesconsumer.api.MockApiManager;
import com.andreabardella.aifaservicesconsumer.util.JsonHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApiServicesModule {

    @Provides
    @Singleton
    public ApiServicesManager provideApiServicesManager(Application application, JsonHelper helper) {
//        if (BuildConfig.FLAVOR.equals("mock")) {
            // todo: avoid helper injection -> I do so in order to provide an instance of ObjectMapper to JsonHelper
            return new MockApiManager(application, helper);
//        }
    }
}
