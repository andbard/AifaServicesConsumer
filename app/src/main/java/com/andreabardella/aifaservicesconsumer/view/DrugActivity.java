package com.andreabardella.aifaservicesconsumer.view;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andreabardella.aifaservicesconsumer.R;
import com.andreabardella.aifaservicesconsumer.SearchType;
import com.andreabardella.aifaservicesconsumer.base.BaseActivity;
import com.andreabardella.aifaservicesconsumer.base.PresenterStatus;
import com.andreabardella.aifaservicesconsumer.component.DrugActivityComponent;
import com.andreabardella.aifaservicesconsumer.model.DrugItem;
import com.andreabardella.aifaservicesconsumer.model.IndustryLight;
import com.andreabardella.aifaservicesconsumer.presenter.DrugActivityPresenter;
import com.andreabardella.aifaservicesconsumer.util.font.StyleableSpannableStringBuilder;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class DrugActivity extends BaseActivity {

    @BindView(R.id.drug_activity_patient_information_leaflet_btn)
    Button fiBtn;
    @BindView(R.id.drug_activity_patient_information_leaflet_pb)
    ProgressBar fiPb;
    @BindView(R.id.drug_activity_product_characteristics_summary_btn)
    Button rcpBtn;
    @BindView(R.id.drug_activity_product_characteristics_summary_pb)
    ProgressBar rcpPb;

    @BindView(R.id.drug_activity_drug_item_pb)
    ProgressBar drugItemPb;
    @BindView(R.id.drug_activity_drug_tv)
    TextView drugNameTv;
    @BindView(R.id.drug_activity_aic_tv)
    TextView aicTv;
    @BindView(R.id.drug_activity_industry_tv)
    TextView industryTv;
    @BindView(R.id.drug_activity_active_ingredient_tv)
    TextView activeIngredientsTv;

    @BindView(R.id.drug_activity_packaging_lv)
    ListView packagingList;
    @BindView(R.id.drug_activity_packaging_list_empty_tv)
    TextView emptyListTv;

    @Inject
    DrugActivityPresenter presenter;

    private DrugItem drugItem;

    private CompositeDisposable compositeDisposable;

    private boolean isGetDrugItemInProgress;
    private boolean isGetFiSizeInProgress;
    private boolean isGetRcpSizeInProgress;
    private boolean isGetFiInProgress;
    private boolean isGetRcpInProgress;

    private String aic;

    private boolean performDrugItemSearchOnResume = false;

    @Override
    protected Class<DrugActivityComponent> getComponentClass() {
        return DrugActivityComponent.class;
    }

    @Override
    protected <M> M getModule() {
        return null;
    }

    @Override
    protected DrugActivityPresenter getBasePresenter() {
        return presenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.drug_activity;
    }

    @Override
    protected void onCreateAfterDependencyInjection(Bundle savedInstanceState) {
        super.onCreateAfterDependencyInjection(savedInstanceState);

        compositeDisposable = new CompositeDisposable();

        packagingList.setEmptyView(emptyListTv);

        fiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFi(drugItem.getFiUrl());
            }
        });

        rcpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRcp(drugItem.getRcpUrl());
            }
        });

        if (presenter.getStatus() == PresenterStatus.CREATED) {
            // the presenter has just been created because the this Activity has just been instantiated too
            // or the Activity has been popped out from its back-stack after the Android system kill the related application process
            isGetDrugItemInProgress = isGetFiSizeInProgress = isGetRcpSizeInProgress = isGetFiInProgress = isGetRcpInProgress = false;
            Intent callingIntent = getIntent();
            if (callingIntent != null) {
                aic = callingIntent.getStringExtra(MainActivity.SEARCH_TEXT);
                performDrugItemSearchOnResume = true;
            }
        } else if (savedInstanceState != null) {
            isGetDrugItemInProgress = savedInstanceState.getBoolean("isGetDrugItemInProgress");
            isGetFiSizeInProgress = savedInstanceState.getBoolean("isGetFiSizeInProgress");
            isGetRcpSizeInProgress = savedInstanceState.getBoolean("isGetRcpSizeInProgress");
            isGetFiInProgress = savedInstanceState.getBoolean("isGetFiInProgress");
            isGetRcpInProgress = savedInstanceState.getBoolean("isGetRcpInProgress");

            drugItem = savedInstanceState.getParcelable("drugItem");
        }

        setDrugItemUI(drugItem);
    }

    private void setDrugItemUI(DrugItem drugItem) {
        if (drugItem != null) {
            fiBtn.setEnabled(drugItem.getFiSize() > 0);
            rcpBtn.setEnabled(drugItem.getRcpSize() > 0);
            fiPb.setVisibility(View.GONE);
            rcpPb.setVisibility(View.GONE);

            drugItemPb.setVisibility(View.GONE);

            if (drugItem.getNameSet() != null) {
                StringBuilder builder = new StringBuilder();
                for (String str : drugItem.getNameSet()) {
                    if (builder.toString().length() == 0) {
                        builder.append(str);
                    } else {
                        builder.append(", " + str);
                    }
                }
                drugNameTv.setText(builder.toString());
            }

            aicTv.setText(drugItem.getAic());

            if (drugItem.getIndustrySet() != null) {
                StyleableSpannableStringBuilder builder = new StyleableSpannableStringBuilder();
                for (IndustryLight industry : drugItem.getIndustrySet()) {
                    if (builder.toString().length() > 0) {
                        builder.append(", ");
                    }
                    Intent intent = new Intent(this, SearchActivity.class);
                    intent.putExtra(MainActivity.SEARCH_TYPE, SearchType.DRUG);
                    intent.putExtra(MainActivity.SEARCH_BY, SearchType.INDUSTRY);
                    intent.putExtra(MainActivity.SEARCH_TEXT, industry.getName());
                    intent.putExtra(MainActivity.SEARCH_INDUSTRY_CODE, industry.getCode());
                    builder.appendClickableSpanStartActivity(industry.getName(), intent, this, true);
                }
                industryTv.setText(builder, TextView.BufferType.SPANNABLE);
                industryTv.setMovementMethod(LinkMovementMethod.getInstance());
            }

            if (drugItem.getActiveIngredientSet() != null) {
                StyleableSpannableStringBuilder builder = new StyleableSpannableStringBuilder();
                for (String str : drugItem.getActiveIngredientSet()) {
                    if (builder.toString().length() > 0) {
                        builder.append(", ");
                    }
                    Intent intent = new Intent(this, SearchActivity.class);
                    intent.putExtra(MainActivity.SEARCH_TYPE, SearchType.DRUG);
                    intent.putExtra(MainActivity.SEARCH_BY, SearchType.ACTIVE_INGREDIENT);
                    intent.putExtra(MainActivity.SEARCH_TEXT, str);
                    builder.appendClickableSpanStartActivity(str, intent, this, true);
                }
                activeIngredientsTv.setText(builder, TextView.BufferType.SPANNABLE);
                activeIngredientsTv.setMovementMethod(LinkMovementMethod.getInstance());
            }

            if (drugItem.getPackagingList() != null) {
                PackagingListAdapter adapter = new PackagingListAdapter(this, R.layout.packaging_list_item, drugItem.getPackagingList());
                packagingList.setAdapter(adapter);
            }
        } else {
            fiBtn.setEnabled(false);
            rcpBtn.setEnabled(false);
            fiPb.setVisibility(View.GONE);
            rcpPb.setVisibility(View.GONE);
            drugItemPb.setVisibility(View.GONE);
        }
    }

    private void getDrugByAic(final String aic) {
        final int requestId = DrugActivityPresenter.DRUG_ITEM;
        presenter.getDrugByAic(aic, false)
                .compose(applyChain(requestId))
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Timber.d("onSubscribe, aic: %s, requestId: %d", aic, requestId);
                    }

                    @Override
                    public void onNext(@NonNull Object o) {
                        drugItem = (DrugItem) o;
                        getFiSize(drugItem.getFiUrl());
                        getRcpSize(drugItem.getRcpUrl());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        DismissOnlyDialog dialog = DismissOnlyDialog.newInstance(requestId, "Error", e.getMessage(), "dismiss");
                        dialog.show(getFragmentManager(), "ERROR_DIALOG_" + requestId);
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("onComplete, aic: %s, requestId: %d", aic, requestId);
                    }
                });
    }

    private void getFiSize(final String url) {
        final int requestId = DrugActivityPresenter.FI_SIZE;
        presenter.getFiSize(drugItem.getFiUrl(), false)
                .compose(applyChain(requestId))
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
//                        Timber.d("onSubscribe, url: %s, requestId: %d", url, requestId);
                    }

                    @Override
                    public void onNext(@NonNull Object o) {
                        Integer size = (Integer) o;
                        drugItem.setFiSize(size);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        DismissOnlyDialog dialog = DismissOnlyDialog.newInstance(requestId, "Error", e.getMessage(), "dismiss");
                        dialog.show(getFragmentManager(), "ERROR_DIALOG_" + requestId);
                    }

                    @Override
                    public void onComplete() {
//                        Timber.d("onComplete, url: %s, requestId: %d", url, requestId);
                    }
                });
    }

    private void getRcpSize(final String url) {
        final int requestId = DrugActivityPresenter.RCP_SIZE;
        presenter.getRcpSize(drugItem.getRcpUrl(), false)
                .compose(applyChain(requestId))
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
//                        Timber.d("onSubscribe, url: %s, requestId: %d", url, requestId);
                    }

                    @Override
                    public void onNext(@NonNull Object o) {
                        Integer size = (Integer) o;
                        drugItem.setRcpSize(size);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        DismissOnlyDialog dialog = DismissOnlyDialog.newInstance(requestId, "Error", e.getMessage(), "dismiss");
                        dialog.show(getFragmentManager(), "ERROR_DIALOG_" + requestId);
                    }

                    @Override
                    public void onComplete() {
//                        Timber.d("onComplete, url: %s, requestId: %d", url, requestId);
                    }
                });
    }

    private void getFi(final String url) {
        final int requestId = DrugActivityPresenter.FI;
        presenter.getFi(drugItem.getFiUrl(), false)
                .compose(applyChain(requestId))
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
//                        Timber.d("onSubscribe, url: %s, requestId: %d", url, requestId);
                    }

                    @Override
                    public void onNext(@NonNull Object o) {
                        File file = (File) o;
                        if (file == null) {
                            DismissOnlyDialog dialog = DismissOnlyDialog.newInstance(requestId, "Error", "Oops...file is null", "dismiss");
                            dialog.show(getFragmentManager(), "ERROR_DIALOG_" + requestId);
                            return;
                        }
                        String path = file.getPath();
                        Timber.d("onNext, url: %s, requestId: %d, path: %s", url, requestId, path);
                        drugItem.setFiPath(path);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        DismissOnlyDialog dialog = DismissOnlyDialog.newInstance(requestId, "Error", e.getMessage(), "dismiss");
                        dialog.show(getFragmentManager(), "ERROR_DIALOG_" + requestId);
                    }

                    @Override
                    public void onComplete() {
//                        Timber.d("onComplete, url: %s, requestId: %d", url, requestId);
                    }
                });
    }

    private void getRcp(final String url) {
        final int requestId = DrugActivityPresenter.RCP;
        presenter.getRcp(drugItem.getFiUrl(), false)
                .compose(applyChain(requestId))
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
//                        Timber.d("onSubscribe, url: %s, requestId: %d", url, requestId);
                    }

                    @Override
                    public void onNext(@NonNull Object o) {
                        File file = (File) o;
                        if (file == null) {
                            DismissOnlyDialog dialog = DismissOnlyDialog.newInstance(requestId, "Error", "Oops...file is null", "dismiss");
                            dialog.show(getFragmentManager(), "ERROR_DIALOG_" + requestId);
                            return;
                        }
                        String path = file.getPath();
//                        Timber.d("onNext, url: %s, requestId: %d, path: %s", url, requestId, path);
                        drugItem.setRcpPath(path);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        DismissOnlyDialog dialog = DismissOnlyDialog.newInstance(requestId, "Error", e.getMessage(), "dismiss");
                        dialog.show(getFragmentManager(), "ERROR_DIALOG_" + requestId);
                    }

                    @Override
                    public void onComplete() {
//                        Timber.d("onComplete, url: %s, requestId: %d", url, requestId);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isGetDrugItemInProgress) {
            onGetDrugItemInProgress();
            getDrugByAic(aic);
        } else if (performDrugItemSearchOnResume) {
            onGetDrugItemInProgress();
            getDrugByAic(aic);
            performDrugItemSearchOnResume = false;
        }
        if (isGetFiSizeInProgress) {
            onGetFiSizeInProgress();
            getFiSize(drugItem.getFiUrl());
        }
        if (isGetRcpSizeInProgress) {
            onGetRcpSizeInProgress();
            getRcpSize(drugItem.getRcpUrl());
        }
        if (isGetFiInProgress) {
            onGetFiInProgress();
            getFi(drugItem.getFiUrl());
        }
        if (isGetRcpInProgress) {
            onGetRcpInProgress();
            getRcp(drugItem.getRcpUrl());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        compositeDisposable.clear();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isGetDrugItemInProgress", isGetDrugItemInProgress);
        outState.putBoolean("isGetFiSizeInProgress", isGetFiSizeInProgress);
        outState.putBoolean("isGetRcpSizeInProgress", isGetRcpSizeInProgress);
        outState.putBoolean("isGetFiInProgress", isGetFiInProgress);
        outState.putBoolean("isGetRcpInProgress", isGetRcpInProgress);

        outState.putParcelable("drugItem", drugItem);
    }

    private <T> ObservableTransformer<T, T> applyChain(final int requestId) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return (ObservableSource<T>) upstream
                        .compose(addToCompositeDisposable())
                        .compose(applySchedulers())
                        .compose(applyRequestStatus(DrugActivity.this, requestId));
            }
        };
    }

    private final ObservableTransformer schedulersTransformer = new ObservableTransformer() {
        @Override
        public ObservableSource apply(@NonNull Observable upstream) {
            return upstream
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    private <T> ObservableTransformer<T, T> applySchedulers() {
        return schedulersTransformer;
    }

    private <T> ObservableTransformer<T, T> applyRequestStatus(final DrugActivity activity, final int requestId) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) throws Exception {
                                Timber.d("doOnSubscribe");
                                switch (requestId) {
                                    case DrugActivityPresenter.DRUG_ITEM:
                                        activity.isGetDrugItemInProgress = true;
                                        activity.onGetDrugItemInProgress();
                                        break;
                                    case DrugActivityPresenter.FI_SIZE:
                                        activity.isGetFiSizeInProgress = true;
                                        activity.onGetFiSizeInProgress();
                                        break;
                                    case DrugActivityPresenter.RCP_SIZE:
                                        activity.isGetRcpSizeInProgress = true;
                                        activity.onGetRcpSizeInProgress();
                                        break;
                                    case DrugActivityPresenter.FI:
                                        activity.isGetFiInProgress= true;
                                        activity.onGetFiInProgress();
                                        break;
                                    case DrugActivityPresenter.RCP:
                                        activity.isGetRcpInProgress = true;
                                        activity.onGetRcpInProgress();
                                        break;
                                }
                            }
                        }).doOnTerminate(new Action() {
                            @Override
                            public void run() throws Exception {
                                Timber.d("doOnTerminate");
                                switch (requestId) {
                                    case DrugActivityPresenter.DRUG_ITEM:
                                        activity.isGetDrugItemInProgress = false;
                                        activity.onGetDrugItemCompleted();
                                        break;
                                    case DrugActivityPresenter.FI_SIZE:
                                        activity.isGetFiSizeInProgress = false;
                                        activity.onGetFiSizeCompleted();
                                        break;
                                    case DrugActivityPresenter.RCP_SIZE:
                                        activity.isGetRcpSizeInProgress = false;
                                        activity.onGetRcpSizeCompleted();
                                        break;
                                    case DrugActivityPresenter.FI:
                                        activity.isGetFiInProgress= false;
                                        activity.onGetFiCompleted();
                                        break;
                                    case DrugActivityPresenter.RCP:
                                        activity.isGetRcpInProgress = false;
                                        activity.onGetRcpCompleted();
                                        break;
                                }
                            }
                        });
            }
        };
    }

    private <T> ObservableTransformer<T, T> addToCompositeDisposable() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        compositeDisposable.add(disposable);
                    }
                });
            }
        };
    }

    private void onGetDrugItemInProgress() {
        drugItemPb.setVisibility(View.VISIBLE);
    }

    private void onGetFiSizeInProgress() {
        fiBtn.setVisibility(View.GONE);
        fiPb.setVisibility(View.VISIBLE);
    }

    private void onGetRcpSizeInProgress() {
        rcpBtn.setVisibility(View.GONE);
        rcpPb.setVisibility(View.VISIBLE);
    }

    private void onGetFiInProgress() {
        fiBtn.setVisibility(View.GONE);
        fiPb.setVisibility(View.VISIBLE);
    }

    private void onGetRcpInProgress() {
        rcpBtn.setVisibility(View.GONE);
        rcpPb.setVisibility(View.VISIBLE);
    }


    private void onGetDrugItemCompleted() {
        drugItemPb.setVisibility(View.GONE);
        setDrugItemUI(drugItem);
    }

    private void onGetFiSizeCompleted() {
        fiPb.setVisibility(View.GONE);
        fiBtn.setVisibility(View.VISIBLE);
        fiBtn.setEnabled(drugItem.getFiSize() > 0);
    }

    private void onGetRcpSizeCompleted() {
        rcpPb.setVisibility(View.GONE);
        rcpBtn.setVisibility(View.VISIBLE);
        rcpBtn.setEnabled(drugItem.getRcpSize() > 0);
    }

    private void onGetFiCompleted() {
        fiPb.setVisibility(View.GONE);
        fiBtn.setVisibility(View.VISIBLE);
        fiBtn.setEnabled(drugItem.getFiSize() > 0);
        File file = new File(drugItem.getFiPath());
        Uri uri = FileProvider.getUriForFile(this, getString(R.string.file_provider_authority), file);
        showPdf(uri);
    }

    private void onGetRcpCompleted() {
        rcpPb.setVisibility(View.GONE);
        rcpBtn.setVisibility(View.VISIBLE);
        rcpBtn.setEnabled(drugItem.getRcpSize() > 0);
        File file = new File(drugItem.getRcpPath());
        Uri uri = FileProvider.getUriForFile(this, getString(R.string.file_provider_authority), file);
        showPdf(uri);
    }

    private void showPdf(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            DismissOnlyDialog dialog = DismissOnlyDialog.newInstance(101, "Error", e.getMessage(), "dismiss");
            dialog.show(getFragmentManager(), "ERROR_DIALOG_" + 101);
        }
    }
}
