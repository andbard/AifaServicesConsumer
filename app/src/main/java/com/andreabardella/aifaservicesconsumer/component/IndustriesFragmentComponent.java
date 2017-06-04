package com.andreabardella.aifaservicesconsumer.component;

import com.andreabardella.aifaservicesconsumer.module.IndustriesFragmentModule;
import com.andreabardella.aifaservicesconsumer.base.SubComponentBuilder;
import com.andreabardella.aifaservicesconsumer.base.scope.FragmentScope;
import com.andreabardella.aifaservicesconsumer.view.IndustriesFragment;

import dagger.MembersInjector;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = {
        IndustriesFragmentModule.class
})
public interface IndustriesFragmentComponent extends MembersInjector<IndustriesFragment> {

    @Subcomponent.Builder
    interface Builder extends SubComponentBuilder<IndustriesFragmentModule, IndustriesFragmentComponent> {}
}
