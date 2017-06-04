package com.andreabardella.aifaservicesconsumer.view;

import android.os.Bundle;
import android.view.View;

import com.andreabardella.aifaservicesconsumer.component.DrugsFragmentComponent;
import com.andreabardella.aifaservicesconsumer.presenter.DrugsFragmentPresenter;

import javax.inject.Inject;

public class DrugsFragment extends SearchFragment {

    @Inject
    DrugsFragmentPresenter presenter;

    @Override
    protected Class<DrugsFragmentComponent> getComponentClass() {
        return DrugsFragmentComponent.class;
    }

    @Override
    protected <M> M getModule() {
        return null;
    }

    @Override
    protected DrugsFragmentPresenter getBasePresenter() {
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
