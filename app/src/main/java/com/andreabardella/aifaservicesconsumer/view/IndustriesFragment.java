package com.andreabardella.aifaservicesconsumer.view;

import android.os.Bundle;

import com.andreabardella.aifaservicesconsumer.component.IndustriesFragmentComponent;
import com.andreabardella.aifaservicesconsumer.presenter.IndustriesFragmentPresenter;

import javax.inject.Inject;

public class IndustriesFragment extends SearchFragment {

    @Inject
    IndustriesFragmentPresenter presenter;

    @Override
    protected Class<IndustriesFragmentComponent> getComponentClass() {
        return IndustriesFragmentComponent.class;
    }

    @Override
    protected <M> M getModule() {
        return null;
    }

    @Override
    protected IndustriesFragmentPresenter getBasePresenter() {
        return presenter;
    }

    @Override
    protected void onActivityCreatedAfterDependenciesInjection(Bundle savedInstanceState) {
        super.onActivityCreatedAfterDependenciesInjection(savedInstanceState);
        filter.setQueryHint(presenter.getFilterHint());
    }

    @Override
    protected String getSearchHint() {
        return presenter.getSearchHint();
    }
}
