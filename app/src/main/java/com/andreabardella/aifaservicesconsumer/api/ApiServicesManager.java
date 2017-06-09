package com.andreabardella.aifaservicesconsumer.api;

import com.andreabardella.aifaservicesconsumer.SearchType;
import com.andreabardella.aifaservicesconsumer.model.ActiveIngredientLight;
import com.andreabardella.aifaservicesconsumer.model.DrugItem;
import com.andreabardella.aifaservicesconsumer.model.DrugLight;
import com.andreabardella.aifaservicesconsumer.model.CompanyLight;

import java.io.File;
import java.util.Set;

import io.reactivex.Observable;

public interface ApiServicesManager {

    Observable<Set<DrugLight>> getDrugsBy(String drug, SearchType by);

    void cancelDrugsByDrugName();

    Observable<Set<ActiveIngredientLight>> getActiveIngredientsByActiveIngredientName(String activeIngredient);

    void cancelGetActiveIngredientsByActiveIngredientName();

    // first 6 characters of AIC
    Observable<DrugItem> getDrugByCode(String code);

    void cancelGetDrugByCode();

    // Folgio Illustrativo -> bugiardino -> patient information leaflet
    Observable<Integer> getPatientInformationLeafletSize(String url);

    void cancelGetPatientInformationLeafletSize();

    // Riassunti delle Caratteristiche del Prodotto -> Summary of Product Characteristics
    Observable<Integer> getProductCharacteristicsSummarySize(String url);

    void cancelGetProductCharacteristicsSummarySize();

    Observable<File> getPatientInformationLeaflet(String url);

    void cancelGetPatientInformationLeaflet();

    Observable<File> getProductCharacteristicsSummary(String url);

    void cancelGetProductCharacteristicsSummary();

    Observable<Set<CompanyLight>> getIndustriesByIndustryName(String industry);

    void cancelGetIndustriesByIndustryName();

    /**/
    void cancelPendingRequests();
}
