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
import com.andreabardella.aifaservicesconsumer.model.CompanyLight;
import com.andreabardella.aifaservicesconsumer.model.DrugItem;
import com.andreabardella.aifaservicesconsumer.model.DrugLight;
import com.andreabardella.aifaservicesconsumer.model.ItemLight;
import com.andreabardella.aifaservicesconsumer.util.FileUtils;
import com.andreabardella.aifaservicesconsumer.util.InputStreamConversion;
import com.andreabardella.aifaservicesconsumer.util.JsonHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import timber.log.Timber;

public class MockApiManager extends BaseApiManager {

    private Context context;

    private static final int DELAY_PER_CYCLE_MILLIS = 100;
    private static final int CYCLES = 16;

    public MockApiManager(Context context, JsonHelper helper) { // n.b.: helper is injected only in order to provide an ObjectMapper instance to JsonHelper
        super();
        this.context = context;
    }


    @Override
    protected Observable<Set<DrugLight>> getDrugsByBeforeDisposableSetup(String drug, SearchType by) {
        return getItemLightSet(drug, SearchType.DRUG, by)
                .map(new Function<Set<? extends ItemLight>, Set<DrugLight>>() {
                    @Override
                    public Set<DrugLight> apply(@NonNull Set<? extends ItemLight> itemLightSet) throws Exception {
                        Timber.d("mapping ItemLight instances into DrugLight ones");
                        Set<DrugLight> result = new HashSet<>();
                        for (ItemLight item : itemLightSet) {
                            result.add((DrugLight) item);
                        }
                        return result;
                    }
                });
    }

    @Override
    protected Observable<Set<CompanyLight>> getIndustriesByIndustryNameBeforeDisposableSetup(String industry) {
        return getItemLightSet(industry, SearchType.COMPANY, null)
                .map(new Function<Set<? extends ItemLight>, Set<CompanyLight>>() {
                    @Override
                    public Set<CompanyLight> apply(@NonNull Set<? extends ItemLight> itemLightSet) throws Exception {
                        Timber.d("mapping ItemLight instances into CompanyLight ones");
                        Set<CompanyLight> result = new HashSet<>();
                        for (ItemLight item : itemLightSet) {
                            result.add((CompanyLight) item);
                        }
                        return result;
                    }
                });
    }

    @Override
    protected Observable<Set<ActiveIngredientLight>> getActiveIngredientsByActiveIngredientNameBeforeDisposableSetup(String activeIngredient) {
        return getItemLightSet(activeIngredient, SearchType.ACTIVE_INGREDIENT, null)
                .map(new Function<Set<? extends ItemLight>, Set<ActiveIngredientLight>>() {
                    @Override
                    public Set<ActiveIngredientLight> apply(@NonNull Set<? extends ItemLight> itemLightSet) throws Exception {
                        Timber.d("mapping ItemLight instances into ActiveIngredientLight ones");
                        Set<ActiveIngredientLight> result = new HashSet<>();
                        for (ItemLight item : itemLightSet) {
                            result.add((ActiveIngredientLight) item);
                        }
                        return result;
                    }
                });
    }

    @Override
    protected Observable<DrugItem> getDrugByCodeBeforeDisposableSetup(final String code) {
        return getDrugItem(code);
    }

    @Override
    protected Observable<Integer> getPatientInformationLeafletSizeBeforeDisposableSetup(String url) {
        return getPdfSize(url);
    }

    @Override
    protected Observable<Integer> getProductCharacteristicsSummarySizeBeforeDisposableSetup(String url) {
        return getPdfSize(url);
    }

    @Override
    protected Observable<File> getPatientInformationLeafletBeforeDisposableSetup(final String url) {
        return getPdf(url);
    }

    @Override
    protected Observable<File> getProductCharacteristicsSummaryBeforeDisposableSetup(final String url) {
        return getPdf(url);
    }


    private Observable<Set<? extends ItemLight>> getItemLightSet(final String text, final SearchType type, SearchType by) {
        final GetItemLightSet getItemLightSet = new GetItemLightSet(text, type, by);
        return Observable.create(getItemLightSet)
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        Timber.d("doOnDispose -> getItemLightSet.terminate()");
                        getItemLightSet.terminate();
                    }
                });
    }

    private class GetItemLightSet implements ObservableOnSubscribe<Set<? extends ItemLight>> {

        private boolean terminate = false;
        private SearchType type;
        private SearchType by;
        private String text;

        GetItemLightSet(String text, SearchType type, SearchType by) {
            this.text = text;
            this.type = type;
            this.by = by;
        }

        void terminate() {
            terminate = true;
        }

        @Override
        public void subscribe(@NonNull ObservableEmitter<Set<? extends ItemLight>> emitter) throws Exception {
            ResponseDto responseDto = null;
            for (int i=0; i<CYCLES; i++) {
                if (terminate) {
                    // todo: onError? omComplete?
                    return; // todo: check the correctness
                }
                Timber.d("simulate latency (%d/%d)", i+1, CYCLES);
                TimeUnit.MILLISECONDS.sleep(DELAY_PER_CYCLE_MILLIS);
            }
            InputStream is = null;
            boolean emitError = false;
            if (type == SearchType.DRUG) {
                if (text.equals("mesu")) {
                    is = context.getAssets().open("getByDrugLight_mesu.json");
                } else if (text.equals("error")) {
                    emitError = true;
                }
            } else if (type == SearchType.ACTIVE_INGREDIENT) {
                if (text.equals("parac")) {
                    is = context.getAssets().open("getByActiveIngredientLight_parac.json");
                } else if (text.equals("error")) {
                    emitError = true;
                }
            } else if (type == SearchType.COMPANY) {
                if (text.equals("lab")) {
                    is = context.getAssets().open("getByIndustryLight_lab.json");
                } else if (text.equals("error")) {
                    emitError = true;
                }
            }
            if (is != null) {
                try {
                    String response = InputStreamConversion.inputStreamToString(is);
                    responseDto = JsonHelper.readJsonString(response, ResponseDto.class);
                } catch (IOException ex) {
                    emitter.onError(ex);
                }
                // todo: if by
                if (responseDto != null && responseDto.response != null) {
                    int numFound = responseDto.response.numFound;
                    int numRetrieved = responseDto.response.drugList != null ? responseDto.response.drugList.size() : -1;
                    if (numFound == 0) {
                        emitter.onNext(new HashSet<ItemLight>());
                        emitter.onComplete();
                    } else if (numRetrieved == -1) {
                        emitter.onError(new Exception("null drug list"));
                    } /*else if (numFound > numRetrieved) {
                            // todo: request the other results exploiting the "start" query param or other paging techniques
                        }*/ else {
                        Set<ItemLight> result = new HashSet<>();
                        for (DrugDto dto : responseDto.response.drugList) {
                            ItemLight item = null;
                            if (type == SearchType.DRUG) {
                                item = DrugDtoToDrugLightMapper.map(dto);
                            } else if (type == SearchType.ACTIVE_INGREDIENT) {
                                item = DrugDtoToActiveIngredientLightMapper.map(dto);
                            } else if (type == SearchType.COMPANY) {
                                item = DrugDtoToCompanyLightMapper.map(dto);
                            }
                            if (item != null && !result.contains(item)) {
                                result.add(item);
                            }
                        }
                        emitter.onNext(result);
                        emitter.onComplete();
                    }
                }
            } else if (emitError) {
                emitter.onError(new Exception("Oops...something went wrong"));
            } else {
                emitter.onNext(new HashSet<ItemLight>());
                emitter.onComplete();
            }
        }
    }


    private Observable<DrugItem> getDrugItem(final String text) {
        final GetDrugItem getDrugItem = new GetDrugItem(text);
        return Observable.create(getDrugItem)
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        Timber.d("doOnDispose -> getDrugItem.terminate()");
                        getDrugItem.terminate();
                    }
                });
    }

    private class GetDrugItem implements ObservableOnSubscribe<DrugItem> {

        private boolean terminate = false;
        private String text;

        GetDrugItem(String text) {
            this.text = text;
        }

        void terminate() {
            terminate = true;
        }

        @Override
        public void subscribe(@NonNull ObservableEmitter<DrugItem> emitter) throws Exception {
            ResponseDto responseDto = null;
            for (int i=0; i<CYCLES; i++) {
                if (terminate) {
                    // todo: onError? omComplete?
                    return; // todo: check the correctness
                }
                Timber.d("simulate latency (%d/%d)", i+1, CYCLES);
                TimeUnit.MILLISECONDS.sleep(DELAY_PER_CYCLE_MILLIS);
            }
            if (text.equals("042692") || text.equals("029007")) {
                try {
                    InputStream is = context.getAssets().open("getByAic_"+text+".json");
                    String response = InputStreamConversion.inputStreamToString(is);
                    responseDto = JsonHelper.readJsonString(response, ResponseDto.class);
                } catch (IOException ex) {
                    emitter.onError(ex);
                }
            }
            if (responseDto != null && responseDto.response != null) {
                int numFound = responseDto.response.numFound;
                int numRetrieved = responseDto.response.drugList != null ? responseDto.response.drugList.size() : -1;
                if (numFound == 0) {
                    emitter.onNext(null);
                    emitter.onComplete();
                } else if (numRetrieved == -1) {
                    emitter.onError(new Exception("null drug"));
                } /*else if (numFound > numRetrieved) {
                        // todo: request the other results exploiting the "start" query param or other paging techniques
                    }*/ else {
                    DrugItem result = DrugDtoToDrugItemMapper.map(responseDto.response.drugList);
                    emitter.onNext(result);
                    emitter.onComplete();
                }
            }
        }
    }


    private Observable<Integer> getPdfSize(final String url) {
        final GetPdfSize getPdfSize = new GetPdfSize(url);
        return Observable.create(getPdfSize)
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        Timber.d("doOnDispose -> getPdfSize(%s)", url);
                        getPdfSize.terminate();
                    }
                });
    }

    private class GetPdfSize implements ObservableOnSubscribe<Integer> {

        private boolean terminate = false;
        private String text;

        GetPdfSize(String url) {
            this.text = url;
        }

        void terminate() {
            this.terminate = true;
        }

        @Override
        public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
            for (int i=0; i<CYCLES; i++) {
                if (terminate) {
                    // todo: onError? omComplete?
                    return; // todo: check the correctness
                }
                Timber.d("simulate latency (%d/%d)", i+1, CYCLES);
                TimeUnit.MILLISECONDS.sleep(DELAY_PER_CYCLE_MILLIS);
            }
            int size = 0;
            if (text.contains("000608_029007_FI")) {
                size = 310129;
            } else if (text.contains("001392_042692_FI")) {
                size = 295719;
            } else if (text.contains("000608_029007_RCP")) {
                size = 470343;
            } else if (text.contains("001392_042692_RCP")) {
                size = 403079;
            }
            emitter.onNext(size);
            emitter.onComplete();
        }
    }


    private Observable<File> getPdf(final String url) {
        final GetPdf getPdf = new GetPdf(url);
        return Observable.create(getPdf)
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        Timber.d("doOnDispose -> getPdf(%s)", url);
                        getPdf.terminate();
                    }
                });
    }

    private class GetPdf implements ObservableOnSubscribe<File> {

        private boolean terminate = false;
        private String text;

        GetPdf(String url) {
            this.text = url;
        }

        void terminate() {
            this.terminate = true;
        }

        @Override
        public void subscribe(@NonNull ObservableEmitter<File> emitter) throws Exception {
            for (int i=0; i<CYCLES; i++) {
                if (terminate) {
                    // todo: onError? omComplete?
                    return; // todo: check the correctness
                }
                Timber.d("simulate latency (%d/%d)", i+1, CYCLES);
                TimeUnit.MILLISECONDS.sleep(DELAY_PER_CYCLE_MILLIS);
            }
            File file = null;
            File dir = new File(context.getCacheDir(), "documents");
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }
            if (text.contains("000608_029007_FI")) {
                file = File.createTempFile("000608_029007_FI", ".pdf", dir);
                FileUtils.copy(context.getAssets().open("footer_000608_029007_FI.pdf"), file);
            } else if (text.contains("001392_042692_FI")) {
                file = File.createTempFile("001392_042692_FI", ".pdf", dir);
                FileUtils.copy(context.getAssets().open("footer_001392_042692_FI.pdf"), file);
            } else if (text.contains("000608_029007_RCP")) {
                file = File.createTempFile("000608_029007_RCP", ".pdf", dir);
                FileUtils.copy(context.getAssets().open("footer_000608_029007_RCP.pdf"), file);
            } else if (text.contains("001392_042692_RCP")) {
                file = File.createTempFile("001392_042692_RCP", ".pdf", dir);
                FileUtils.copy(context.getAssets().open("footer_001392_042692_RCP.pdf"), file);
            }
            emitter.onNext(file);
            emitter.onComplete();
        }
    }
}
