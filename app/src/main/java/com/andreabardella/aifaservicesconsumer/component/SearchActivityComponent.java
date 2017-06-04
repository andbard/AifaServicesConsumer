package com.andreabardella.aifaservicesconsumer.component;

import com.andreabardella.aifaservicesconsumer.module.SearchActivityModule;
import com.andreabardella.aifaservicesconsumer.module.SearchFragmentsBindingModule;
import com.andreabardella.aifaservicesconsumer.base.SubComponentBuilder;
import com.andreabardella.aifaservicesconsumer.base.component.SubComponentBuildersProvider;
import com.andreabardella.aifaservicesconsumer.base.scope.ActivityScope;
import com.andreabardella.aifaservicesconsumer.view.SearchActivity;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {
        SearchActivityModule.class,
        SearchFragmentsBindingModule.class
})
public interface SearchActivityComponent extends SubComponentBuildersProvider<SearchActivity> {

//    SearchActivity inject(Activity activity);

    @Subcomponent.Builder
    interface Builder extends SubComponentBuilder<SearchActivityModule, SearchActivityComponent> {}
}

// n.b.: The following could be used if we do not want to instantiate a component per hosted fragment
/*public interface SearchActivityComponent extends MembersInjector<SearchActivity> {

//    SearchActivity inject(Activity activity);

    @Subcomponent.Builder
    interface Builder extends SubComponentBuilder<SearchActivityModule, SearchActivityComponent> {}
}*/
