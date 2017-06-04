package com.andreabardella.aifaservicesconsumer.base;

public interface Presenter {

    PresenterStatus getStatus();

    void resume();

    void pause();

    void release();
}
