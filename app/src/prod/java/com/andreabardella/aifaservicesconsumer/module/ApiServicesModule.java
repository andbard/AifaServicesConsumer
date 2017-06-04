package com.andreabardella.aifaservicesconsumer.module;

import android.app.Application;

import com.andreabardella.aifaservicesconsumer.api.ApiServices;
import com.andreabardella.aifaservicesconsumer.api.ApiServicesManager;
import com.andreabardella.aifaservicesconsumer.api.RestApiManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ApiServicesModule {

    @Provides
    @Singleton
    public ApiServices provideApiServices(Retrofit retrofit) {
        return retrofit.create(ApiServices.class);
    }

    @Provides
    @Singleton
    public ApiServicesManager provideApiServicesManager(ApiServices apiService, Application application) {
        return new RestApiManager(apiService, application);
    }
}
