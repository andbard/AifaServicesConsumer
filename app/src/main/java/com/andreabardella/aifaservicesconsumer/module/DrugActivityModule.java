package com.andreabardella.aifaservicesconsumer.module;

import com.andreabardella.aifaservicesconsumer.api.ApiServicesManager;
import com.andreabardella.aifaservicesconsumer.base.scope.ActivityScope;
import com.andreabardella.aifaservicesconsumer.presenter.DrugActivityPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class DrugActivityModule {

    @Provides
    @ActivityScope
    static DrugActivityPresenter provideDrugActivityPresenter(ApiServicesManager manager) {
        return new DrugActivityPresenter(manager);
    }
}
