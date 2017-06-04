package com.andreabardella.aifaservicesconsumer.presenter;

import com.andreabardella.aifaservicesconsumer.FooBar;
import com.andreabardella.aifaservicesconsumer.base.BasePresenter;

public class IndustriesFragmentPresenter extends BasePresenter {

    private FooBar fooBar;

    public IndustriesFragmentPresenter(FooBar fooBar) {
        this.fooBar = fooBar;
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void release() {
        super.release();
    }

    public String getSearchHint() {
        return fooBar.getSearchHint();
    }

    public String getFilterHint() {
        return fooBar.getFilterHint();
    }
}
