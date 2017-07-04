package com.andreabardella.aifaservicesconsumer.component;

import com.andreabardella.aifaservicesconsumer.module.CompaniesFragmentModule;
import com.andreabardella.aifaservicesconsumer.base.SubComponentBuilder;
import com.andreabardella.aifaservicesconsumer.base.scope.FragmentScope;
import com.andreabardella.aifaservicesconsumer.view.CompaniesFragment;

import dagger.MembersInjector;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = {
        CompaniesFragmentModule.class
})
public interface CompaniesFragmentComponent extends MembersInjector<CompaniesFragment> {

    @Subcomponent.Builder
    interface Builder extends SubComponentBuilder<CompaniesFragmentModule, CompaniesFragmentComponent> {}
}
