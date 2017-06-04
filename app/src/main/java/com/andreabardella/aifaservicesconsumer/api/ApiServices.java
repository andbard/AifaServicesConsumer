package com.andreabardella.aifaservicesconsumer.api;

import com.andreabardella.aifaservicesconsumer.dto.ResponseDto;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiServices {

//    @GET("/services/search/select/?wt=json")
    @GET("/services/search/select/?wt=json&df=sm_field_descrizione_farmaco")
    @Headers("Accept: application/json")
    Observable<ResponseDto> getResponse(
            //e.g.: fl=sm_field_codice_farmaco,sm_field_descrizione_farmaco,sm_field_descrizione_ditta,sm_field_descrizione_atc,sm_field_codice_atc
            @Query(value = "fl", encoded = true) String projections,
            //e.g.: q=bundle:confezione_farmaco+sm_field_descrizione_atc:parac*
            @Query(value = "q", encoded = true) String queryFieldValuePairs,
            @Query("rows") int maxResultsNumber,
            @Query("start") int startingResultIndex
    );

    /**********/
    /* BY AIC */
    /**********/
    @GET("/services/search/select/?wt=json&df=sm_field_descrizione_farmaco&rows=1000")
    @Headers("Accept: application/json")
    Observable<ResponseDto> getByAic(
            @Query(value = "fl", encoded = true) String projections,
            //e.g.: q=bundle:confezione_farmaco+sm_field_codice_farmaco:029125
            @Query(value = "q", encoded = true) String queryFieldValuePairs
    );
    @GET("/services/search/select/?wt=json&df=sm_field_descrizione_farmaco&rows=1000")
    @Headers("Accept: application/json")
    Observable<ResponseDto> getByAic(
            //e.g.: q=bundle:confezione_farmaco+sm_field_codice_farmaco:029125
            @Query(value = "q", encoded = true) String queryFieldValuePairs
    );

    /***********/
    /* BY DRUG */
    /***********/
    @GET("/services/search/select/?wt=json&df=sm_field_descrizione_farmaco&rows=100000")
    @Headers("Accept: application/json")
    Observable<ResponseDto> getByDrug(
            @Query(value = "fl", encoded = true) String projections,
            //e.g.: bundle:confezione_farmaco+sm_field_descrizione_farmaco:nimes*
            @Query(value = "q", encoded = true) String queryFieldValuePairs
    );
    @GET("/services/search/select/?wt=json&fl=sm_field_codice_farmaco,sm_field_descrizione_farmaco,sm_field_descrizione_ditta&df=sm_field_descrizione_farmaco&rows=100000")
    @Headers("Accept: application/json")
    Observable<ResponseDto> getByDrug(
            //e.g.: bundle:confezione_farmaco+sm_field_descrizione_farmaco:nimes*
            @Query(value = "q", encoded = true) String queryFieldValuePairs
    );

    /************************/
    /* BY ACTIVE INGREDIENT */
    /************************/
    @GET("/services/search/select/?wt=json&df=sm_field_descrizione_atc&rows=100000")
    @Headers("Accept: application/json")
    Observable<ResponseDto> getByActiveIngredient(
            @Query(value = "fl", encoded = true) String projections,
            //e.g.: bundle:confezione_farmaco+sm_field_descrizione_atc:parac*
            @Query(value = "q", encoded = true) String queryFieldValuePairs
    );
    @GET("/services/search/select/?wt=json&fl=sm_field_descrizione_atc&df=sm_field_descrizione_atc&rows=100000")
    @Headers("Accept: application/json")
    Observable<ResponseDto> getByActiveIngredient(
            //e.g.: bundle:confezione_farmaco+sm_field_descrizione_atc:parac*
            @Query(value = "q", encoded = true) String queryFieldValuePairs
    );
    @GET("/services/search/select/?wt=json&fl=sm_field_codice_farmaco,sm_field_descrizione_farmaco,sm_field_descrizione_ditta,sm_field_descrizione_atc&df=sm_field_descrizione_atc&rows=100000")
    @Headers("Accept: application/json")
    Observable<ResponseDto> getDrugsByActiveIngredient(
            //e.g.: bundle:confezione_farmaco+sm_field_descrizione_atc:Paracalcitolo
            @Query(value = "q", encoded = true) String queryFieldValuePairs
    );

    /***************/
    /* BY INDUSTRY */
    /***************/
    @GET("/services/search/select/?wt=json&df=sm_field_descrizione_ditta&rows=100000")
    @Headers("Accept: application/json")
    Observable<ResponseDto> getByIndustry(
            @Query(value = "fl", encoded = true) String projections,
            //e.g.: bundle:confezione_farmaco+sm_field_descrizione_ditta:*lab*
            @Query(value = "q", encoded = true) String queryFieldValuePairs
    );
    @GET("/services/search/select/?wt=json&fl=sm_field_descrizione_ditta,sm_field_codice_ditta&df=sm_field_descrizione_ditta&rows=100000")
    @Headers("Accept: application/json")
    Observable<ResponseDto> getByIndustry(
            //e.g.: bundle:confezione_farmaco+sm_field_descrizione_ditta:*lab*
            @Query(value = "q", encoded = true) String queryFieldValuePairs
    );
    @GET("/services/search/select/?wt=json&fl=sm_field_codice_farmaco,sm_field_descrizione_farmaco,sm_field_codice_ditta,sm_field_descrizione_ditta&df=sm_field_codice_ditta&rows=100000")
    @Headers("Accept: application/json")
    Observable<ResponseDto> getDrugsByIndustry(
            //e.g.: bundle:confezione_farmaco+sm_field_codice_ditta:*2752*
            @Query(value = "q", encoded = true) String queryFieldValuePairs
    );

    /******/
    /* FI */
    /******/
//    https://farmaci.agenziafarmaco.gov.it/aifa/servlet/PdfDownloadServlet?pdfFileName=footer_001392_042692_FI.pdf&retry=0&sys=m0b1l3
    @HEAD("/aifa/servlet/PdfDownloadServlet")
    @Headers("Accept: application/json")
    Observable<Response<Void>> headFi(
            @Query("pdfFileName") String pdfFileName
    );
    @GET("/aifa/servlet/PdfDownloadServlet")
    @Headers("Accept: application/json")
    Observable<ResponseBody> getFi(
            @Query("pdfFileName") String pdfFileName
    );

    /*******/
    /* RCP */
    /*******/
//    https://farmaci.agenziafarmaco.gov.it/aifa/servlet/PdfDownloadServlet?pdfFileName=footer_001392_042692_RCP.pdf&retry=0&sys=m0b1l3
    @HEAD("/aifa/servlet/PdfDownloadServlet")
    @Headers("Accept: application/json")
    Observable<Response<Void>> headRcp(
            @Query("pdfFileName") String pdfFileName
    );
    @HEAD("/aifa/servlet/PdfDownloadServlet")
    @Headers("Accept: application/json")
    Observable<ResponseBody> getRcp(
            @Query("pdfFileName") String pdfFileName
    );

}
