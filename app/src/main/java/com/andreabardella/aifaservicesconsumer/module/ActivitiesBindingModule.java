package com.andreabardella.aifaservicesconsumer.module;

import com.andreabardella.aifaservicesconsumer.base.SubComponentBuilder;
import com.andreabardella.aifaservicesconsumer.base.SubComponentKey;
import com.andreabardella.aifaservicesconsumer.component.DrugActivityComponent;
import com.andreabardella.aifaservicesconsumer.component.SearchActivityComponent;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module(subcomponents = {
        SearchActivityComponent.class,
        DrugActivityComponent.class
})
public abstract class ActivitiesBindingModule {

    @Binds
    @IntoMap
    @SubComponentKey(SearchActivityComponent.class)
    public abstract SubComponentBuilder searchActivityComponentBuilder(SearchActivityComponent.Builder impl);

    @Binds
    @IntoMap
    @SubComponentKey(DrugActivityComponent.class)
    public abstract SubComponentBuilder drugActivityComponentBuilder(DrugActivityComponent.Builder impl);
}
