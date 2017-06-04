package com.andreabardella.aifaservicesconsumer.component;

import com.andreabardella.aifaservicesconsumer.module.DrugActivityModule;
import com.andreabardella.aifaservicesconsumer.base.SubComponentBuilder;
import com.andreabardella.aifaservicesconsumer.base.scope.ActivityScope;
import com.andreabardella.aifaservicesconsumer.view.DrugActivity;

import dagger.MembersInjector;
import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {
    DrugActivityModule.class
})
public interface DrugActivityComponent extends MembersInjector<DrugActivity> {

    @Subcomponent.Builder
    interface Builder extends SubComponentBuilder<DrugActivityModule, DrugActivityComponent> {}
}
