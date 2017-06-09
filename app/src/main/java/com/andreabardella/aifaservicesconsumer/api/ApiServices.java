package com.andreabardella.aifaservicesconsumer.api;

import com.andreabardella.aifaservicesconsumer.dto.ResponseDto;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * This interface defines the calls against the AIFA rest services
 */
public interface ApiServices {

    /**
     * Get the results for the submitted parameters
     *
     * @param projections the fields to be returned
     * @param queryFieldValuePairs the search fields and parameters
     * @param maxResultsNumber the max number of results to be returned
     * @param startingResultIndex the starting index of the results to be returned (leverage on this for paging)
     * @return the response
     */
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

    /**
     * Get the results for the submitted AIC
     * (the AIC is a sequence of 9 digits, the first 6 identifying the drug, the last 3 identifying the packaging).
     *
     * @param projections the fields to be returned
     * @param queryFieldValuePairs the search fields and parameters
     * @return the response
     */
    @GET("/services/search/select/?wt=json&df=sm_field_descrizione_farmaco&rows=1000")
    @Headers("Accept: application/json")
    Observable<ResponseDto> getByAic(
            @Query(value = "fl", encoded = true) String projections,
            //e.g.: q=bundle:confezione_farmaco+sm_field_codice_farmaco:029125
            @Query(value = "q", encoded = true) String queryFieldValuePairs
    );
    /**
     * Get the results for the submitted AIC
     * (the AIC is a sequence of 9 digits, the first 6 identifying the drug, the last 3 identifying the packaging).
     *
     * @param queryFieldValuePairs the search fields and parameters
     * @return the response
     */
    @GET("/services/search/select/?wt=json&df=sm_field_descrizione_farmaco&rows=1000")
    @Headers("Accept: application/json")
    Observable<ResponseDto> getByAic(
            //e.g.: q=bundle:confezione_farmaco+sm_field_codice_farmaco:029125
            @Query(value = "q", encoded = true) String queryFieldValuePairs
    );

    /**
     * Get the results for the submitted drug name (e.g.: "nimes", "nimesulide").
     *
     * @param projections the fields to be returned
     * @param queryFieldValuePairs the search fields and parameters
     * @return the response
     */
    @GET("/services/search/select/?wt=json&df=sm_field_descrizione_farmaco&rows=100000")
    @Headers("Accept: application/json")
    Observable<ResponseDto> getByDrugName(
            @Query(value = "fl", encoded = true) String projections,
            //e.g.: q=bundle:confezione_farmaco+sm_field_descrizione_farmaco:nimes*
            @Query(value = "q", encoded = true) String queryFieldValuePairs
    );
    /**
     * Get the result (items composed of drug name, AIC and company name)
     * for the submitted drug name (e.g.: "nimes", "nimesulide").
     *
     * @param queryFieldValuePairs the search fields and parameters
     * @return the response
     */
    @GET("/services/search/select/?wt=json&fl=sm_field_codice_farmaco,sm_field_descrizione_farmaco,sm_field_descrizione_ditta&df=sm_field_descrizione_farmaco&rows=100000")
    @Headers("Accept: application/json")
    Observable<ResponseDto> getByDrugName(
            //e.g.: q=bundle:confezione_farmaco+sm_field_descrizione_farmaco:nimes*
            @Query(value = "q", encoded = true) String queryFieldValuePairs
    );

    /**
     * Get the results for the submitted active ingredient name (e.g.: "parac", "paracetamolo").
     *
     * @param projections the fields to be returned
     * @param queryFieldValuePairs the search fields and parameters
     * @return the response
     */
    @GET("/services/search/select/?wt=json&df=sm_field_descrizione_atc&rows=100000")
    @Headers("Accept: application/json")
    Observable<ResponseDto> getByActiveIngredient(
            @Query(value = "fl", encoded = true) String projections,
            //e.g.: q=bundle:confezione_farmaco+sm_field_descrizione_atc:parac*
            @Query(value = "q", encoded = true) String queryFieldValuePairs
    );
    /**
     * Get the result (items composed of active ingredient name)
     * for the submitted active ingredient name (e.g.: "parac", "paracetamolo").
     *
     * @param queryFieldValuePairs the search fields and parameters
     * @return the response
     */
    @GET("/services/search/select/?wt=json&fl=sm_field_descrizione_atc&df=sm_field_descrizione_atc&rows=100000")
    @Headers("Accept: application/json")
    Observable<ResponseDto> getByActiveIngredient(
            //e.g.: q=bundle:confezione_farmaco+sm_field_descrizione_atc:parac*
            @Query(value = "q", encoded = true) String queryFieldValuePairs
    );
    /**
     * Get the result (items composed of drug name, AIC and company name)
     * for the submitted active ingredient name (e.g.: "paracalcitolo").
     *
     * @param queryFieldValuePairs the search fields and parameters
     * @return the response
     */
    @GET("/services/search/select/?wt=json&fl=sm_field_codice_farmaco,sm_field_descrizione_farmaco,sm_field_descrizione_ditta,sm_field_descrizione_atc&df=sm_field_descrizione_atc&rows=100000")
    @Headers("Accept: application/json")
    Observable<ResponseDto> getDrugsByActiveIngredient(
            //e.g.: q=bundle:confezione_farmaco+sm_field_descrizione_atc:Paracalcitolo
            @Query(value = "q", encoded = true) String queryFieldValuePairs
    );

    /**
     * Get the results for the submitted company name (e.g.: "lab", "laboratorio").
     *
     * @param projections the fields to be returned
     * @param queryFieldValuePairs the search fields and parameters
     * @return the response
     */
    @GET("/services/search/select/?wt=json&df=sm_field_descrizione_ditta&rows=100000")
    @Headers("Accept: application/json")
    Observable<ResponseDto> getByCompany(
            @Query(value = "fl", encoded = true) String projections,
            //e.g.: q=bundle:confezione_farmaco+sm_field_descrizione_ditta:*lab*
            @Query(value = "q", encoded = true) String queryFieldValuePairs
    );
    /**
     * Get the result (items composed of company name and company ID)
     * for the submitted company name (e.g.: "lab", "laboratorio").
     *
     * @param queryFieldValuePairs the search fields and parameters
     * @return the response
     */
    @GET("/services/search/select/?wt=json&fl=sm_field_descrizione_ditta,sm_field_codice_ditta&df=sm_field_descrizione_ditta&rows=100000")
    @Headers("Accept: application/json")
    Observable<ResponseDto> getByCompany(
            //e.g.: q=bundle:confezione_farmaco+sm_field_descrizione_ditta:*lab*
            @Query(value = "q", encoded = true) String queryFieldValuePairs
    );
    /**
     * Get the result (items composed of drug name, AIC and company name)
     * for the submitted company ID (e.g.: "2752").
     *
     * @param queryFieldValuePairs the search fields and parameters
     * @return the response
     */
    @GET("/services/search/select/?wt=json&fl=sm_field_codice_farmaco,sm_field_descrizione_farmaco,sm_field_codice_ditta,sm_field_descrizione_ditta&df=sm_field_codice_ditta&rows=100000")
    @Headers("Accept: application/json")
    Observable<ResponseDto> getDrugsByCompany(
            //e.g.: q=bundle:confezione_farmaco+sm_field_codice_ditta:*2752*
            @Query(value = "q", encoded = true) String queryFieldValuePairs
    );

    /**
     * Perform an HEAD http request in order to retrieve the document size
     *
     * @param url should be "https://farmaci.agenziafarmaco.gov.it/aifa/servlet/PdfDownloadServlet"
     * @param pdfFileName the pdf filename
     * @param retry ?
     * @param sys ?
     * @return an empty body response with the needed headers, among with the "Content-Length"
     */
//    https://farmaci.agenziafarmaco.gov.it/aifa/servlet/PdfDownloadServlet?pdfFileName=footer_001392_042692_FI.pdf&retry=0&sys=m0b1l3
    @HEAD
    Observable<Response<Void>> headFi(
            @Url String url,
            @Query(value = "pdfFileName", encoded = true) String pdfFileName,
            @Query(value = "retry", encoded = true) String retry,
            @Query(value = "sys", encoded = true) String sys
    );
    /**
     * Perform an HEAD http request in order to retrieve the document size
     *
     * @param url should be "https://farmaci.agenziafarmaco.gov.it/aifa/servlet/PdfDownloadServlet"
     * @param pdfFileName the pdf filename
     * @param retry ?
     * @param sys ?
     * @return an empty body response with the needed headers, among with the "Content-Length"
     */
    @GET
    Observable<ResponseBody> getFi(
            @Url String url,
            @Query(value = "pdfFileName", encoded = true) String pdfFileName,
            @Query(value = "retry", encoded = true) String retry,
            @Query(value = "sys", encoded = true) String sys
    );

    /**
     * Retrieve the document
     *
     * @param url should be "https://farmaci.agenziafarmaco.gov.it/aifa/servlet/PdfDownloadServlet"
     * @param pdfFileName the pdf filename
     * @param retry ?
     * @param sys ?
     * @return the requested pdf
     */
//    https://farmaci.agenziafarmaco.gov.it/aifa/servlet/PdfDownloadServlet?pdfFileName=footer_001392_042692_RCP.pdf&retry=0&sys=m0b1l3
    @HEAD
    Observable<Response<Void>> headRcp(
            @Url String url,
            @Query(value = "pdfFileName", encoded = true) String pdfFileName,
            @Query(value = "retry", encoded = true) String retry,
            @Query(value = "sys", encoded = true) String sys
    );
    /**
     * Retrieve the document
     *
     * @param url should be "https://farmaci.agenziafarmaco.gov.it/aifa/servlet/PdfDownloadServlet"
     * @param pdfFileName the pdf filename
     * @param retry ?
     * @param sys ?
     * @return the requested pdf
     */
    @GET
    Observable<ResponseBody> getRcp(
            @Url String url,
            @Query(value = "pdfFileName", encoded = true) String pdfFileName,
            @Query(value = "retry", encoded = true) String retry,
            @Query(value = "sys", encoded = true) String sys
    );

}
