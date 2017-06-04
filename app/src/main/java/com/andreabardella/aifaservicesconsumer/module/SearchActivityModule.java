package com.andreabardella.aifaservicesconsumer.module;

import com.andreabardella.aifaservicesconsumer.FooBar;
import com.andreabardella.aifaservicesconsumer.SearchType;
import com.andreabardella.aifaservicesconsumer.api.ApiServicesManager;
import com.andreabardella.aifaservicesconsumer.base.scope.ActivityScope;
import com.andreabardella.aifaservicesconsumer.presenter.SearchActivityPresenter;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class SearchActivityModule {

    private SearchType type;

    public SearchActivityModule(SearchType type) {
        this.type = type;
    }

    public void setSearchType(SearchType type) {
        this.type = type;
    }

    @Provides
    @ActivityScope
    public SearchType provideSearchType() {
        return type;
    }

    @Provides
    @ActivityScope
    @Named("search_activity_dependency")
    static FooBar provideFooBar(SearchType type) {
        return new FooBar(type, null, null);
    }

    @Provides
    @ActivityScope
    static SearchActivityPresenter provideSearchActivityPresenter(ApiServicesManager manager, @Named("search_activity_dependency") FooBar fooBar) {
        return new SearchActivityPresenter(manager, fooBar);
    }
}
