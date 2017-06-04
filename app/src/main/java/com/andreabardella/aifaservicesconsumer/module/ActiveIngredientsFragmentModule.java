package com.andreabardella.aifaservicesconsumer.module;

import com.andreabardella.aifaservicesconsumer.BuildConfig;
import com.andreabardella.aifaservicesconsumer.FooBar;
import com.andreabardella.aifaservicesconsumer.SearchType;
import com.andreabardella.aifaservicesconsumer.base.scope.FragmentScope;
import com.andreabardella.aifaservicesconsumer.presenter.ActiveIngredientsFragmentPresenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ActiveIngredientsFragmentModule {

    @Provides
    @FragmentScope
    @Named("active_ingredients_fragment_dependency")
    static FooBar provideFooBar() {
        if (BuildConfig.FLAVOR.equals("mock")) {
            return new FooBar(SearchType.ACTIVE_INGREDIENT, "parac", "assoc");
        }
        return new FooBar(SearchType.ACTIVE_INGREDIENT, "", "");
    }

    @Provides
    @FragmentScope
    static ActiveIngredientsFragmentPresenter provideActiveIngredientsFragmentPresenter(@Named("active_ingredients_fragment_dependency") FooBar fooBar) {
        return new ActiveIngredientsFragmentPresenter(fooBar);
    }
}
