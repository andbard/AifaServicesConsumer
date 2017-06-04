package com.andreabardella.aifaservicesconsumer.module;

import com.andreabardella.aifaservicesconsumer.BuildConfig;
import com.andreabardella.aifaservicesconsumer.FooBar;
import com.andreabardella.aifaservicesconsumer.SearchType;
import com.andreabardella.aifaservicesconsumer.base.scope.FragmentScope;
import com.andreabardella.aifaservicesconsumer.presenter.IndustriesFragmentPresenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class IndustriesFragmentModule {

    @Provides
    @FragmentScope
    @Named("industries_fragment_dependency")
    static FooBar provideFooBar() {
        if (BuildConfig.FLAVOR.equals("mock")) {
            return new FooBar(SearchType.INDUSTRY, "lab", "zatta");
        }
        return new FooBar(SearchType.INDUSTRY, "", "");
    }

    @Provides
    @FragmentScope
    static IndustriesFragmentPresenter provideIndustriesFragmentPresenter(@Named("industries_fragment_dependency") FooBar fooBar) {
        return new IndustriesFragmentPresenter(fooBar);
    }
}
