package com.andreabardella.aifaservicesconsumer.view;

import android.os.Bundle;

import com.andreabardella.aifaservicesconsumer.base.BasePresenter;
import com.andreabardella.aifaservicesconsumer.component.ActiveIngredientsFragmentComponent;
import com.andreabardella.aifaservicesconsumer.presenter.ActiveIngredientsFragmentPresenter;

import javax.inject.Inject;

public class ActiveIngredientsFragment extends SearchFragment {

    @Inject
    ActiveIngredientsFragmentPresenter presenter;

    @Override
    protected Class<ActiveIngredientsFragmentComponent> getComponentClass() {
        return ActiveIngredientsFragmentComponent.class;
    }

    @Override
    protected <M> M getModule() {
        return null;
    }

    @Override
    protected ActiveIngredientsFragmentPresenter getBasePresenter() {
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
