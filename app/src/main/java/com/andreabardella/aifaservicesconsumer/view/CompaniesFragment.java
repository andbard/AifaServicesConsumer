package com.andreabardella.aifaservicesconsumer.view;

import android.os.Bundle;

import com.andreabardella.aifaservicesconsumer.component.CompaniesFragmentComponent;
import com.andreabardella.aifaservicesconsumer.presenter.CompaniesFragmentPresenter;

import javax.inject.Inject;

public class CompaniesFragment extends SearchFragment {

    @Inject
    CompaniesFragmentPresenter presenter;

    @Override
    protected Class<CompaniesFragmentComponent> getComponentClass() {
        return CompaniesFragmentComponent.class;
    }

    @Override
    protected <M> M getModule() {
        return null;
    }

    @Override
    protected CompaniesFragmentPresenter getBasePresenter() {
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
