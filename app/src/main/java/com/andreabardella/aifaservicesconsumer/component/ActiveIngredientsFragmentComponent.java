package com.andreabardella.aifaservicesconsumer.component;

import com.andreabardella.aifaservicesconsumer.module.ActiveIngredientsFragmentModule;
import com.andreabardella.aifaservicesconsumer.base.SubComponentBuilder;
import com.andreabardella.aifaservicesconsumer.base.scope.FragmentScope;
import com.andreabardella.aifaservicesconsumer.view.ActiveIngredientsFragment;

import dagger.MembersInjector;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = {
        ActiveIngredientsFragmentModule.class
})
public interface ActiveIngredientsFragmentComponent extends MembersInjector<ActiveIngredientsFragment> {

    @Subcomponent.Builder
    interface Builder extends SubComponentBuilder<ActiveIngredientsFragmentModule, ActiveIngredientsFragmentComponent> {}
}
