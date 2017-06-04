package com.andreabardella.aifaservicesconsumer.presenter;

import android.os.Build;
import android.text.Html;

import com.andreabardella.aifaservicesconsumer.api.ApiServicesManager;
import com.andreabardella.aifaservicesconsumer.base.BasePresenter;
import com.andreabardella.aifaservicesconsumer.model.DrugItem;

import java.io.File;
import java.util.HashMap;

import io.reactivex.Observable;

public class DrugActivityPresenter extends BasePresenter {

    private ApiServicesManager apiServicesManager;

    private Observable<DrugItem> drugItem;
    private Observable<Integer> fiSize;
    private Observable<Integer> rcpSize;
    private Observable<File> fi;
    private Observable<File> rcp;

    public static final int DRUG_ITEM = 1;
    public static final int FI_SIZE = 2;
    public static final int RCP_SIZE = 3;
    public static final int FI = 4;
    public static final int RCP = 5;

    private HashMap<Integer, Observable<?>> observableMap;

    public DrugActivityPresenter(ApiServicesManager manager) {
        this.apiServicesManager = manager;

        observableMap = new HashMap<>();
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
        apiServicesManager.cancelPendingRequests();
        observableMap.clear();
    }

    public Observable<DrugItem> getDrugByAic(String aic, boolean refreshCache) {
        Observable observableDotCache = apiServicesManager.getDrugByCode(aic).cache();
        return cacheObservable(DRUG_ITEM, observableDotCache, refreshCache);
    }

    public Observable<Integer> getFiSize(String url, boolean refreshCache) {
        return getSize(FI_SIZE, url, refreshCache);
    }

    public Observable<Integer> getRcpSize(String url, boolean refreshCache) {
        return getSize(RCP_SIZE, url, refreshCache);
    }

    public Observable<File> getFi(String url, boolean refreshCache) {
        return getPdf(FI, url, refreshCache);
    }

    public Observable<File> getRcp(String url, boolean refreshCache) {
        return getPdf(RCP, url, refreshCache);
    }

    private Observable<Integer> getSize(int cachedObservableId, String url, boolean refreshCache) {
        String decodedUrl = decodeHtml(url);
        Observable observableDotCache;
        if (cachedObservableId == FI_SIZE) {
            observableDotCache = apiServicesManager.getPatientInformationLeafletSize(decodedUrl).cache();
        } else {
            observableDotCache = apiServicesManager.getProductCharacteristicsSummarySize(decodedUrl).cache();
        }
        return cacheObservable(cachedObservableId, observableDotCache, refreshCache);
    }

    private Observable<File> getPdf(int cachedObservableId, String url, boolean refreshCache) {
        String decodedUrl = decodeHtml(url);
        Observable observableDotCache;
        if (cachedObservableId == FI) {
            observableDotCache = apiServicesManager.getPatientInformationLeaflet(decodedUrl).cache();
        } else {
            observableDotCache = apiServicesManager.getProductCharacteristicsSummary(decodedUrl).cache();
        }
        return cacheObservable(cachedObservableId, observableDotCache, refreshCache);
    }

    private <T> Observable<T> cacheObservable(int cachedObservableId, Observable<T> observable, boolean refreshCache) {
        Observable<T> cachedObservable = (Observable<T>) observableMap.get(cachedObservableId);
        if (!refreshCache && cachedObservable != null) {
            return cachedObservable;
        }
        cachedObservable = observable;
        updateCache(cachedObservableId, cachedObservable);
        return cachedObservable;
    }

    private <T> void updateCache(int cachedObservableId, Observable<T> observable) {
        observableMap.put(cachedObservableId, observable);
    }

    private String decodeHtml(String toDecode) {
        String str;
        if (Build.VERSION.SDK_INT >= 24) {
            str = Html.fromHtml(toDecode, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            //noinspection deprecation
            str = Html.fromHtml(toDecode).toString();
        }
        return str;
    }
}
