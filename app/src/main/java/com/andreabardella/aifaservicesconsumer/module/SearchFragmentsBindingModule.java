package com.andreabardella.aifaservicesconsumer.module;

import com.andreabardella.aifaservicesconsumer.base.SubComponentBuilder;
import com.andreabardella.aifaservicesconsumer.base.SubComponentKey;
import com.andreabardella.aifaservicesconsumer.component.ActiveIngredientsFragmentComponent;
import com.andreabardella.aifaservicesconsumer.component.DrugsFragmentComponent;
import com.andreabardella.aifaservicesconsumer.component.CompaniesFragmentComponent;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module(subcomponents = {
        ActiveIngredientsFragmentComponent.class,
        CompaniesFragmentComponent.class,
        DrugsFragmentComponent.class
})
public abstract class SearchFragmentsBindingModule {

    @Binds
    @IntoMap
    @SubComponentKey(ActiveIngredientsFragmentComponent.class)
    public abstract SubComponentBuilder activeIngredientsFragmentComponentBuilder(ActiveIngredientsFragmentComponent.Builder impl);

    @Binds
    @IntoMap
    @SubComponentKey(CompaniesFragmentComponent.class)
    public abstract SubComponentBuilder companiesFragmentComponentBuilder(CompaniesFragmentComponent.Builder impl);

    @Binds
    @IntoMap
    @SubComponentKey(DrugsFragmentComponent.class)
    public abstract SubComponentBuilder drugsFragmentComponentBuilder(DrugsFragmentComponent.Builder impl);
}
