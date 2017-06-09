package com.andreabardella.aifaservicesconsumer.api;

import android.content.Context;

import com.andreabardella.aifaservicesconsumer.SearchType;
import com.andreabardella.aifaservicesconsumer.dto.DrugDto;
import com.andreabardella.aifaservicesconsumer.dto.ResponseDto;
import com.andreabardella.aifaservicesconsumer.dto.mapper.DrugDtoToActiveIngredientLightMapper;
import com.andreabardella.aifaservicesconsumer.dto.mapper.DrugDtoToDrugItemMapper;
import com.andreabardella.aifaservicesconsumer.dto.mapper.DrugDtoToDrugLightMapper;
import com.andreabardella.aifaservicesconsumer.dto.mapper.DrugDtoToCompanyLightMapper;
import com.andreabardella.aifaservicesconsumer.model.ActiveIngredientLight;
import com.andreabardella.aifaservicesconsumer.model.DrugItem;
import com.andreabardella.aifaservicesconsumer.model.DrugLight;
import com.andreabardella.aifaservicesconsumer.model.CompanyLight;
import com.andreabardella.aifaservicesconsumer.module.NetModule;
import com.andreabardella.aifaservicesconsumer.util.FileUtils;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class RestApiManager extends BaseApiManager {

    private ApiServices apiServices;
    private Context context;

    public RestApiManager(ApiServices apiServices, Context context) {
        super();
        this.apiServices = apiServices;
        this.context = context;
    }


    @Override
    protected Observable<Set<DrugLight>> getDrugsByBeforeDisposableSetup(String text, SearchType by) {
        Observable<ResponseDto> observable = Observable.just(new ResponseDto());
        if (by == null || by == SearchType.DRUG) {
            observable = apiServices.getByDrugName("bundle:confezione_farmaco+sm_field_descrizione_farmaco:" + text + "*");
        } else if (by == SearchType.ACTIVE_INGREDIENT) {
            observable = apiServices.getDrugsByActiveIngredient("bundle:confezione_farmaco+sm_field_descrizione_atc:" + text);
        } else if (by == SearchType.COMPANY) {
            observable = apiServices.getDrugsByCompany("bundle:confezione_farmaco+sm_field_codice_ditta:*" + text + "*");
        }
        return observable.map(new Function<ResponseDto, Set<DrugLight>>() {
            @Override
            public Set<DrugLight> apply(@NonNull ResponseDto responseDto) throws Exception {
                if (responseDto != null && responseDto.response != null) {
                    int numFound = responseDto.response.numFound;
                    int numRetrieved = responseDto.response.drugList != null ? responseDto.response.drugList.size() : -1;
                    if (numFound == 0) {
                        return new HashSet<>();
                    } else if (numRetrieved == -1) {
                        throw new Exception("null drug list");
                    } /*else if (numFound > numRetrieved) {
                        // todo: request the other results exploiting the "start" query param or other paging techniques
                    }*/ else {
                        Set<DrugLight> result = new HashSet<>();
                        for (DrugDto dto : responseDto.response.drugList) {
                            DrugLight item = DrugDtoToDrugLightMapper.map(dto);
                            if (item != null && !result.contains(item)) {
                                result.add(item);
                            }
                        }
                        return result;
                    }
                }
                throw new Exception("null response");
            }
        });
    }

    @Override
    protected Observable<Set<CompanyLight>> getIndustriesByIndustryNameBeforeDisposableSetup(String industry) {
        Observable<ResponseDto> observable = apiServices.getByCompany("bundle:confezione_farmaco+sm_field_descrizione_ditta:" + industry + "*");
        return observable.map(new Function<ResponseDto, Set<CompanyLight>>() {
            @Override
            public Set<CompanyLight> apply(@NonNull ResponseDto responseDto) throws Exception {
                if (responseDto != null && responseDto.response != null) {
                    int numFound = responseDto.response.numFound;
                    int numRetrieved = responseDto.response.drugList != null ? responseDto.response.drugList.size() : -1;
                    if (numFound == 0) {
                        return new HashSet<>();
                    } else if (numRetrieved == -1) {
                        throw new Exception("null industry list");
                    } else {
                        Set<CompanyLight> result = new HashSet<>();
                        for (DrugDto dto : responseDto.response.drugList) {
                            CompanyLight item = DrugDtoToCompanyLightMapper.map(dto);
                            if (item != null && !result.contains(item)) {
                                result.add(item);
                            }
                        }
                        return result;
                    }
                }
                throw new Exception("null response");
            }
        });
    }

    @Override
    protected Observable<Set<ActiveIngredientLight>> getActiveIngredientsByActiveIngredientNameBeforeDisposableSetup(String activeIngredient) {
        Observable<ResponseDto> observable = apiServices.getByActiveIngredient("bundle:confezione_farmaco+sm_field_descrizione_atc:" + activeIngredient + "*");
        return observable.map(new Function<ResponseDto, Set<ActiveIngredientLight>>() {
            @Override
            public Set<ActiveIngredientLight> apply(@NonNull ResponseDto responseDto) throws Exception {
                if (responseDto != null && responseDto.response != null) {
                    int numFound = responseDto.response.numFound;
                    int numRetrieved = responseDto.response.drugList != null ? responseDto.response.drugList.size() : -1;
                    if (numFound == 0) {
                        return new HashSet<>();
                    } else if (numRetrieved == -1) {
                        throw new Exception("null active ingredient list");
                    } else {
                        Set<ActiveIngredientLight> result = new HashSet<>();
                        for (DrugDto dto : responseDto.response.drugList) {
                            ActiveIngredientLight item = DrugDtoToActiveIngredientLightMapper.map(dto);
                            if (item != null && !result.contains(item)) {
                                result.add(item);
                            }
                        }
                        return result;
                    }
                }
                throw new Exception("null response");
            }
        });
    }

    @Override
    protected Observable<DrugItem> getDrugByCodeBeforeDisposableSetup(String code) {
        Observable<ResponseDto> observable = apiServices.getByAic("bundle:confezione_farmaco+sm_field_codice_farmaco:" + code);
        return observable.map(new Function<ResponseDto, DrugItem>() {
            @Override
            public DrugItem apply(@NonNull ResponseDto responseDto) throws Exception {
                if (responseDto != null && responseDto.response != null) {
                    int numFound = responseDto.response.numFound;
                    int numRetrieved = responseDto.response.drugList != null ? responseDto.response.drugList.size() : -1;
                    if (numFound == 0) {
                        return null;
                    } else if (numRetrieved == -1) {
                        throw new Exception("null drug");
                    } else {
                        return DrugDtoToDrugItemMapper.map(responseDto.response.drugList);
                    }
                }
                throw new Exception("null response");
            }
        });
    }

    @Override
    protected Observable<Integer> getPatientInformationLeafletSizeBeforeDisposableSetup(String url) {
        String[] arr = url.split("pdfFileName=");
        if (arr.length > 1) {
            String partialUrl = arr[1];
            String[] array = parseUrlFromPdfFileName(partialUrl);
            Observable<Response<Void>> observable = apiServices.headFi(NetModule.DOCS_URL, array[0], array[1], array[2]); // footer_001392_042692_FI.pdf&retry=0&sys=m0b1l3
            return observable.map(new Function<Response<Void>, Integer>() {
                @Override
                public Integer apply(@NonNull Response<Void> voidResponse) throws Exception {
                    Headers headers = voidResponse.headers();
                    String str = headers.get("Content-Length");
                    int size = 0;
                    if (str != null) {
                        size = Integer.parseInt(str);
                    }
                    return size;
                }
            });
        }
        return Observable.just(0);
    }

    @Override
    protected Observable<Integer> getProductCharacteristicsSummarySizeBeforeDisposableSetup(String url) {
        String[] arr = url.split("pdfFileName=");
        if (arr.length > 1) {
            String partialUrl = arr[1];
            String[] array = parseUrlFromPdfFileName(partialUrl);
            Observable<Response<Void>> observable = apiServices.headRcp(NetModule.DOCS_URL, array[0], array[1], array[2]); // footer_001392_042692_RCP.pdf&retry=0&sys=m0b1l3
            return observable.map(new Function<Response<Void>, Integer>() {
                @Override
                public Integer apply(@NonNull Response<Void> voidResponse) throws Exception {
                    Headers headers = voidResponse.headers();
                    String str = headers.get("Content-Length");
                    int size = 0;
                    if (str != null) {
                        size = Integer.parseInt(str);
                    }
                    return size;
                }
            });
        }
        return Observable.just(0);
    }

    @Override
    protected Observable<File> getPatientInformationLeafletBeforeDisposableSetup(String url) {
        String[] arr = url.split("pdfFileName=");
        if (arr.length > 1) {
            String partialUrl = arr[1];
            final String[] array = parseUrlFromPdfFileName(partialUrl);
            Observable<ResponseBody> observable = apiServices.getFi(NetModule.DOCS_URL, array[0], array[1], array[2]); // footer_001392_042692_FI.pdf&retry=0&sys=m0b1l3
            return observable.map(new Function<ResponseBody, File>() {
                @Override
                public File apply(@NonNull ResponseBody responseBody) throws Exception {
                    File dir = new File(context.getCacheDir(), "documents"); // see file_provider_paths.xml
                    if (!dir.isDirectory()) {
                        dir.mkdirs();
                    }
                    File file = File.createTempFile(array[0].split("\\.")[0], ".pdf", dir);
                    if (FileUtils.copy(responseBody.byteStream(), file)) {
                        return file;
                    }
                    return null;
                }
            });
        }
        return Observable.create(new ObservableOnSubscribe<File>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<File> emitter) throws Exception {
                emitter.onError(new Exception("no file found"));
            }
        });
    }

    @Override
    protected Observable<File> getProductCharacteristicsSummaryBeforeDisposableSetup(String url) {
        String[] arr = url.split("pdfFileName=");
        if (arr.length > 1) {
            String partialUrl = arr[1];
            final String[] array = parseUrlFromPdfFileName(partialUrl);
            Observable<ResponseBody> observable = apiServices.getRcp(NetModule.DOCS_URL, array[0], array[1], array[2]); // footer_001392_042692_RCP.pdf&retry=0&sys=m0b1l3
            return observable.map(new Function<ResponseBody, File>() {
                @Override
                public File apply(@NonNull ResponseBody responseBody) throws Exception {
                    File dir = new File(context.getCacheDir(), "documents"); // see file_provider_paths.xml
                    if (!dir.isDirectory()) {
                        dir.mkdirs();
                    }
                    File file = File.createTempFile(array[0].split("\\.")[0], ".pdf", dir);
                    if (FileUtils.copy(responseBody.byteStream(), file)) {
                        return file;
                    }
                    return null;
                }
            });
        }
        return Observable.create(new ObservableOnSubscribe<File>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<File> emitter) throws Exception {
                emitter.onError(new Exception("no file found"));
            }
        });
    }

    private String[] parseUrlFromPdfFileName(String partialUrl) {
        String[] array = partialUrl.split("&");
        String name = "";
        String retry = "";
        String sys = "";
        for (int i=0; i<array.length; i++) {
            switch (i) {
                case 0:
                    name = array[0];
                    break;
                case 1:
                    retry = array[1].split("=")[1];
                    break;
                case 2:
                    sys = array[2].split("=")[1];
                    break;
            }
        }
        return new String[] {name, retry, sys};
    }
}
