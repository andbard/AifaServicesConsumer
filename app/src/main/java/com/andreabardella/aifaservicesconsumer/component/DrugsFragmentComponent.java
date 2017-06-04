package com.andreabardella.aifaservicesconsumer.component;

import com.andreabardella.aifaservicesconsumer.module.DrugsFragmentModule;
import com.andreabardella.aifaservicesconsumer.base.SubComponentBuilder;
import com.andreabardella.aifaservicesconsumer.base.scope.FragmentScope;
import com.andreabardella.aifaservicesconsumer.view.DrugsFragment;

import dagger.MembersInjector;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = {
        DrugsFragmentModule.class
})
public interface DrugsFragmentComponent extends MembersInjector<DrugsFragment> {

    @Subcomponent.Builder
    interface Builder extends SubComponentBuilder<DrugsFragmentModule, DrugsFragmentComponent> {}
}
