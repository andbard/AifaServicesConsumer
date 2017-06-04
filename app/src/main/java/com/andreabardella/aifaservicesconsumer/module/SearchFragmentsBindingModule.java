package com.andreabardella.aifaservicesconsumer.module;

import com.andreabardella.aifaservicesconsumer.base.SubComponentBuilder;
import com.andreabardella.aifaservicesconsumer.base.SubComponentKey;
import com.andreabardella.aifaservicesconsumer.component.ActiveIngredientsFragmentComponent;
import com.andreabardella.aifaservicesconsumer.component.DrugsFragmentComponent;
import com.andreabardella.aifaservicesconsumer.component.IndustriesFragmentComponent;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module(subcomponents = {
        ActiveIngredientsFragmentComponent.class,
        IndustriesFragmentComponent.class,
        DrugsFragmentComponent.class
})
public abstract class SearchFragmentsBindingModule {

    @Binds
    @IntoMap
    @SubComponentKey(ActiveIngredientsFragmentComponent.class)
    public abstract SubComponentBuilder activeIngredientsFragmentComponentBuilder(ActiveIngredientsFragmentComponent.Builder impl);

    @Binds
    @IntoMap
    @SubComponentKey(IndustriesFragmentComponent.class)
    public abstract SubComponentBuilder industriesFragmentComponentBuilder(IndustriesFragmentComponent.Builder impl);

    @Binds
    @IntoMap
    @SubComponentKey(DrugsFragmentComponent.class)
    public abstract SubComponentBuilder drugsFragmentComponentBuilder(DrugsFragmentComponent.Builder impl);
}
