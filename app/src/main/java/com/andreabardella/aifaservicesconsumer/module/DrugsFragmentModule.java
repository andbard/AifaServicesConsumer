package com.andreabardella.aifaservicesconsumer.module;

import com.andreabardella.aifaservicesconsumer.BuildConfig;
import com.andreabardella.aifaservicesconsumer.FooBar;
import com.andreabardella.aifaservicesconsumer.SearchType;
import com.andreabardella.aifaservicesconsumer.base.scope.FragmentScope;
import com.andreabardella.aifaservicesconsumer.presenter.DrugsFragmentPresenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class DrugsFragmentModule {

    @Provides
    @FragmentScope
    @Named("drugs_fragment_dependency")
    static FooBar provideFooBar() {
        if (BuildConfig.FLAVOR.equals("mock")) {
            return new FooBar(SearchType.DRUG, "mesu", "ehf");
        }
        return new FooBar(SearchType.DRUG, "", "");
    }

    @Provides
    @FragmentScope
    static DrugsFragmentPresenter provideDrugActivityPresenter(@Named("drugs_fragment_dependency") FooBar fooBar) {
        return new DrugsFragmentPresenter(fooBar);
    }
}
