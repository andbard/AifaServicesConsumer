package com.andreabardella.aifaservicesconsumer.module;

import com.andreabardella.aifaservicesconsumer.BuildConfig;
import com.andreabardella.aifaservicesconsumer.FooBar;
import com.andreabardella.aifaservicesconsumer.SearchType;
import com.andreabardella.aifaservicesconsumer.base.scope.FragmentScope;
import com.andreabardella.aifaservicesconsumer.presenter.CompaniesFragmentPresenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class CompaniesFragmentModule {

    @Provides
    @FragmentScope
    @Named("companies_fragment_dependency")
    static FooBar provideFooBar() {
        if (BuildConfig.FLAVOR.equals("mock")) {
            return new FooBar(SearchType.COMPANY, "lab", "zatta");
        }
        return new FooBar(SearchType.COMPANY, "", "");
    }

    @Provides
    @FragmentScope
    static CompaniesFragmentPresenter provideCompaniesFragmentPresenter(@Named("companies_fragment_dependency") FooBar fooBar) {
        return new CompaniesFragmentPresenter(fooBar);
    }
}
