package com.andreabardella.aifaservicesconsumer.presenter;

import com.andreabardella.aifaservicesconsumer.FooBar;
import com.andreabardella.aifaservicesconsumer.SearchType;
import com.andreabardella.aifaservicesconsumer.api.ApiServicesManager;
import com.andreabardella.aifaservicesconsumer.base.BasePresenter;
import com.andreabardella.aifaservicesconsumer.model.ItemLight;

import java.util.HashMap;
import java.util.Set;

import io.reactivex.Observable;
import timber.log.Timber;

public class SearchActivityPresenter extends BasePresenter {

    private ApiServicesManager apiServicesManager;
    private FooBar fooBar;

    private HashMap<SearchType, Observable<?>> observableMap;

    public SearchActivityPresenter(ApiServicesManager manager, FooBar fooBar) {
        Timber.d("SearchActivityPresenter.this.fooBar.getSearchType(): %s", fooBar.getSearchType());
        this.apiServicesManager = manager;
        this.fooBar = fooBar;

        observableMap = new HashMap<>();
    }

    @Override
    public void resume() {
//        Timber.d("before presenter.resume() -> getStatus(): %s", getStatus());
        super.resume();
//        Timber.d("after presenter.resume() -> getStatus(): %s", getStatus());
    }

    @Override
    public void pause() {
        super.pause();
    }

    public Observable<Set<? extends ItemLight>> search(String text, SearchType type, SearchType by, boolean refreshCache) {
        Observable observableDotCache = null;
        Observable cachedObservable = null;
        switch (type) {
            case DRUG:
//                return drugLightObservable = apiServicesManager.getDrugsByDrugName(text);
//                observableDotCache = apiServicesManager.getDrugsByDrugName(text).cache();
                observableDotCache = apiServicesManager.getDrugsBy(text, by).cache();
                break;
            case ACTIVE_INGREDIENT:
//                return activeIngredientLightObservable = apiServicesManager.getActiveIngredientsByActiveIngredientName(text);
                observableDotCache = apiServicesManager.getActiveIngredientsByActiveIngredientName(text).cache();
                break;
            case COMPANY:
//                return industryLightObservable = apiServicesManager.getIndustriesByIndustryName(text);
                observableDotCache = apiServicesManager.getIndustriesByIndustryName(text).cache();
                break;
        }
        // check, by type, if an observableDotCache already is in map and return it
        // if it is not in map, cache and return it
        if (observableDotCache != null) {
            cachedObservable = cacheObservable(type, observableDotCache, refreshCache);
        }
        return cachedObservable;
    }

    private <T> Observable<T> cacheObservable(SearchType type, Observable<T> observable, boolean refreshCache) {
        Observable<T> cachedObservable = (Observable<T>) observableMap.get(type);
        if (!refreshCache && cachedObservable != null) {
            return cachedObservable;
        }
        cachedObservable = observable;
        updateCache(type, cachedObservable);
        return cachedObservable;
    }

    private <T> void updateCache(SearchType type, Observable<T> observable) {
        observableMap.put(type, observable);
    }

    @Override
    public void release() {
        super.release();
//        for (Map.Entry<SearchType, Observable<?>> entry : observableMap.entrySet()) {
//            Observable observable = entry.getValue();
//            observable.unsubscribeOn(Schedulers.io());
//        }
        apiServicesManager.cancelPendingRequests();
        observableMap.clear();
    }
}
