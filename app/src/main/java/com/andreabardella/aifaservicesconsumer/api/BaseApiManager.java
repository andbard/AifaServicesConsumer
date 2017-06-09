package com.andreabardella.aifaservicesconsumer.api;

import com.andreabardella.aifaservicesconsumer.SearchType;
import com.andreabardella.aifaservicesconsumer.model.ActiveIngredientLight;
import com.andreabardella.aifaservicesconsumer.model.CompanyLight;
import com.andreabardella.aifaservicesconsumer.model.DrugItem;
import com.andreabardella.aifaservicesconsumer.model.DrugLight;

import java.io.File;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public abstract class BaseApiManager implements ApiServicesManager {

    protected CompositeDisposable compositeDisposable;
    protected Disposable getDrugsByDisposable;
    protected Disposable getIndustriesByIndustryName;
    protected Disposable getActiveIngredientsByActiveIngredientName;
    protected Disposable getDrugItemByCode;
    protected Disposable getPatientInformationLeafletSize;
    protected Disposable getProductCharacteristicsSummarySize;
    protected Disposable getPatientInformationLeaflet;
    protected Disposable getProductCharacteristicsSummary;

    public BaseApiManager() {
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public Observable<Set<DrugLight>> getDrugsBy(String drug, SearchType by) {
        return getDrugsByBeforeDisposableSetup(drug, by)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        getDrugsByDisposable = disposable;
                        compositeDisposable.add(getDrugsByDisposable);
                    }
                });
    }

    protected abstract Observable<Set<DrugLight>> getDrugsByBeforeDisposableSetup(String drug, SearchType type);

    @Override
    public void cancelDrugsByDrugName() {
        dispose(getDrugsByDisposable);
        getDrugsByDisposable = null; // todo: remove?
    }


    @Override
    public Observable<Set<CompanyLight>> getIndustriesByIndustryName(String industry) {
        return getIndustriesByIndustryNameBeforeDisposableSetup(industry)
                .doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(@NonNull Disposable disposable) throws Exception {
                        getIndustriesByIndustryName = disposable;
                        compositeDisposable.add(getIndustriesByIndustryName);
                    }
                });
    }

    protected abstract Observable<Set<CompanyLight>> getIndustriesByIndustryNameBeforeDisposableSetup(String industry);

    @Override
    public void cancelGetIndustriesByIndustryName() {
        dispose(getIndustriesByIndustryName);
    }


    @Override
    public Observable<Set<ActiveIngredientLight>> getActiveIngredientsByActiveIngredientName(String activeIngredient) {
        return getActiveIngredientsByActiveIngredientNameBeforeDisposableSetup(activeIngredient)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        getActiveIngredientsByActiveIngredientName = disposable;
                        compositeDisposable.add(getActiveIngredientsByActiveIngredientName);
                    }
                });
    }

    protected abstract Observable<Set<ActiveIngredientLight>> getActiveIngredientsByActiveIngredientNameBeforeDisposableSetup(String activeIngredient);

    @Override
    public void cancelGetActiveIngredientsByActiveIngredientName() {
        dispose(getActiveIngredientsByActiveIngredientName);
    }


    @Override
    public Observable<DrugItem> getDrugByCode(final String code) {
        return getDrugByCodeBeforeDisposableSetup(code)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        getDrugItemByCode = disposable;
                        compositeDisposable.add(getDrugItemByCode);
                    }
                });
    }

    protected abstract Observable<DrugItem> getDrugByCodeBeforeDisposableSetup(final String code);

    @Override
    public void cancelGetDrugByCode() {
        dispose(getDrugItemByCode);
    }


    @Override
    public Observable<Integer> getPatientInformationLeafletSize(String url) {
        return getPatientInformationLeafletSizeBeforeDisposableSetup(url)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        getPatientInformationLeafletSize = disposable;
                        compositeDisposable.add(getPatientInformationLeafletSize);
                    }
                });
    }

    protected abstract Observable<Integer> getPatientInformationLeafletSizeBeforeDisposableSetup(String url);

    @Override
    public void cancelGetPatientInformationLeafletSize() {
        dispose(getPatientInformationLeafletSize);
    }


    @Override
    public Observable<Integer> getProductCharacteristicsSummarySize(String url) {
        return getProductCharacteristicsSummarySizeBeforeDisposableSetup(url)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        getProductCharacteristicsSummarySize = disposable;
                        compositeDisposable.add(getProductCharacteristicsSummarySize);
                    }
                });
    }

    protected abstract Observable<Integer> getProductCharacteristicsSummarySizeBeforeDisposableSetup(String url);

    @Override
    public void cancelGetProductCharacteristicsSummarySize() {
        dispose(getProductCharacteristicsSummarySize);
    }


    @Override
    public Observable<File> getPatientInformationLeaflet(final String url) {
        return getPatientInformationLeafletBeforeDisposableSetup(url)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        getPatientInformationLeaflet = disposable;
                        compositeDisposable.add(getPatientInformationLeaflet);
                    }
                });
    }

    protected abstract Observable<File> getPatientInformationLeafletBeforeDisposableSetup(final String url);

    @Override
    public void cancelGetPatientInformationLeaflet() {
        dispose(getPatientInformationLeaflet);
    }


    @Override
    public Observable<File> getProductCharacteristicsSummary(final String url) {
        return getProductCharacteristicsSummaryBeforeDisposableSetup(url)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        getProductCharacteristicsSummary = disposable;
                        compositeDisposable.add(getProductCharacteristicsSummary);
                    }
                });
    }

    protected abstract Observable<File> getProductCharacteristicsSummaryBeforeDisposableSetup(final String url);

    @Override
    public void cancelGetProductCharacteristicsSummary() {
        dispose(getProductCharacteristicsSummary);
    }


    @Override
    public void cancelPendingRequests() {
        compositeDisposable.clear();
    }


    private void dispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            compositeDisposable.remove(disposable);
        }
    }
}
