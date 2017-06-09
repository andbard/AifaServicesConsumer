package com.andreabardella.aifaservicesconsumer.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andreabardella.aifaservicesconsumer.R;
import com.andreabardella.aifaservicesconsumer.SearchType;
import com.andreabardella.aifaservicesconsumer.base.PresenterStatus;
import com.andreabardella.aifaservicesconsumer.component.SearchActivityComponent;
import com.andreabardella.aifaservicesconsumer.base.BaseActivity;
import com.andreabardella.aifaservicesconsumer.model.ItemLight;
import com.andreabardella.aifaservicesconsumer.module.SearchActivityModule;
import com.andreabardella.aifaservicesconsumer.presenter.SearchActivityPresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
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

public class SearchActivity extends BaseActivity implements SearchFragment.SearchListener {

    @BindView(R.id.search_activity_search_et)
    EditText searchEt;
    @BindView(R.id.search_activity_search_btn)
    Button searchBtn;
    @BindView(R.id.search_activity_list_container)
    ViewGroup container;

    private SearchType type;
    private SearchType by;
    private String industryCode;
    private boolean performSearchOnResume = false;

    @Inject
    SearchActivityPresenter presenter;

    private CompositeDisposable compositeDisposable;

    private boolean isSearchInProgress = false;

    private ArrayList<ItemLight> items = new ArrayList<>();

    private SearchFragment fragment;

    @Override
    protected void onNewIntent(Intent intent) {
        Timber.d("onNewIntent");
        super.onNewIntent(intent);
        setIntent(intent);

        items = new ArrayList<>();

        type = (SearchType) intent.getSerializableExtra(MainActivity.SEARCH_TYPE);
        by = (SearchType) intent.getSerializableExtra(MainActivity.SEARCH_BY);
        setActivityTitle(type, by);

        if (by == SearchType.COMPANY) {
            industryCode = intent.getStringExtra(MainActivity.SEARCH_INDUSTRY_CODE);
        }

        String text = intent.getStringExtra(MainActivity.SEARCH_TEXT);
        searchEt.setText(text);
        searchEt.setEnabled(false);
        searchBtn.setEnabled(false);

        fragment = new DrugsFragment();
        getFragmentManager().beginTransaction().replace(R.id.search_activity_list_container, fragment, "fragment").commit();

        performSearchOnResume = true;
    }

    @Override
    protected void onCreateAfterViewInjection(Bundle savedInstanceState) {
        super.onCreateAfterViewInjection(savedInstanceState);

        compositeDisposable = new CompositeDisposable();

        if (savedInstanceState == null) {
            Intent incomingIntent = getIntent();
            type = (SearchType) incomingIntent.getSerializableExtra(MainActivity.SEARCH_TYPE);
            by = (SearchType) incomingIntent.getSerializableExtra(MainActivity.SEARCH_BY);

            if (by == SearchType.COMPANY) {
                industryCode = incomingIntent.getStringExtra(MainActivity.SEARCH_INDUSTRY_CODE);
            }

            String text = incomingIntent.getStringExtra(MainActivity.SEARCH_TEXT);
            if (text != null) {
                searchEt.setText(text);
            }

            switch (type) {
                case DRUG:
                    fragment = new DrugsFragment();
                    break;
                case COMPANY:
                    fragment = new IndustriesFragment();
                    break;
                case ACTIVE_INGREDIENT:
                    fragment = new ActiveIngredientsFragment();
                    break;
            }
            //fragment.setArguments(args);
            //fragment.setSearchListener(this);
            getFragmentManager().beginTransaction().replace(R.id.search_activity_list_container, fragment, "fragment").commit();
        } else {
            fragment = (SearchFragment) getFragmentManager().findFragmentByTag("fragment");

            items = savedInstanceState.getParcelableArrayList("items");
            type = (SearchType) savedInstanceState.getSerializable(MainActivity.SEARCH_TYPE);
            by = (SearchType) savedInstanceState.getSerializable(MainActivity.SEARCH_BY);
            industryCode = savedInstanceState.getString(MainActivity.SEARCH_INDUSTRY_CODE);
        }

        if (by != null && by != SearchType.DRUG) {
            searchEt.setEnabled(false);
            searchBtn.setEnabled(false);

            if (savedInstanceState == null) {
                performSearchOnResume = true;
            }
        }

        setActivityTitle(type, by);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = searchEt.getText().toString();
                if (text.length() < 3) {
                    Toast.makeText(SearchActivity.this, "Use at least 3 characters", Toast.LENGTH_SHORT).show();
                } else {
                    search(text, type, by, true);
                }
            }
        });
    }

    @Override
    protected void onCreateAfterDependencyInjection(Bundle savedInstanceState) {
        super.onCreateAfterDependencyInjection(savedInstanceState);

        if (presenter.getStatus() == PresenterStatus.CREATED) {
            // the presenter has just been created because the this Activity has just been instantiated too
            // or the Activity has been popped out from its back-stack after the Android system kill the related application process
            isSearchInProgress = false;
        } else if (savedInstanceState != null) {
            isSearchInProgress = savedInstanceState.getBoolean("isSearchInProgress");
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.search_activity;
    }

    @Override
    protected SearchActivityPresenter getBasePresenter() {
        return presenter;
    }

    @Override
    protected Class<SearchActivityComponent> getComponentClass() {
        return SearchActivityComponent.class;
    }

    @Override
    protected SearchActivityModule getModule() {
        return new SearchActivityModule(type);
    }

    ViewGroup getContainer() {
        return container;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isSearchInProgress) {
            onSearchInProgress();
            search(searchEt.getText().toString(), type, by, false);
        } else {
            onSearchCompleted(items);
            if (performSearchOnResume) {
                performSearchOnResume = false;
                if (by != null && by == SearchType.COMPANY) {
                    search(industryCode, type, by, true);
                } else {
                    search(searchEt.getText().toString(), type, by, true);
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        compositeDisposable.clear();
    }

    private void search(String text, SearchType type, SearchType by, boolean refreshCache) {
        Observable<Set<? extends ItemLight>> observable = presenter.search(text, type, by, refreshCache);
        if (observable != null) {
            observable
                    .compose(applyChain())
                    .subscribe(new Observer<Object>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            Timber.d("onSubscribe");
                        }

                        @Override
                        public void onNext(@NonNull Object objects) {
                            Timber.d("onNext");
                            if (objects != null) {
                                items = new ArrayList<ItemLight>();
                                for (ItemLight o : (Set<ItemLight>) objects) {
                                    items.add((ItemLight) o);
                                }
                            } else {
                                items = new ArrayList<>();
                            }
                            onSearchCompleted(items);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            Timber.d("onError");
                            onSearchError(e);
                        }

                        @Override
                        public void onComplete() {
                            Timber.d("onComplete");
                        }
                    });
        } else {
            Observable.create(new ObservableOnSubscribe<Object>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                    e.onError(new Exception("Oops...something went really wrong"));
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(MainActivity.SEARCH_TYPE, type);
        outState.putSerializable(MainActivity.SEARCH_BY, by);
        outState.putString(MainActivity.SEARCH_INDUSTRY_CODE, industryCode);
        outState.putBoolean("isSearchInProgress", isSearchInProgress);
        outState.putParcelableArrayList("items", items);
    }

    private void setActivityTitle(SearchType type, SearchType by) {
        String label = getString(R.string.search) + " ";
        switch (type) {
            case DRUG:
                label += getString(R.string.drug);
                break;
            case ACTIVE_INGREDIENT:
                label += getString(R.string.active_ingredient);
                break;
            case COMPANY:
                label += getString(R.string.industry);
                break;
        }
        if (by != null) {
            switch (by) {
//                case DRUG:
//                    label += getString(R.string.drug);
//                    break;
                case ACTIVE_INGREDIENT:
                    label += " " + getString(R.string.by) + " " + getString(R.string.active_ingredient);
                    break;
                case COMPANY:
                    label += " " + getString(R.string.by) + " " + getString(R.string.industry);
                    break;
            }
        }
        this.setTitle(label);
    }

    private void onSearchInProgress() {
        searchEt.setEnabled(false);
        searchBtn.setEnabled(false);
        fragment.onSearchInProgress();
    }

    private void onSearchCompleted(List<ItemLight> items) {
        if (by != null && by != SearchType.DRUG) {
            searchEt.setEnabled(false);
            searchBtn.setEnabled(false);
        } else {
            searchEt.setEnabled(true);
            searchBtn.setEnabled(true);
        }
        fragment.onSearchCompleted(type, items);
    }

    private static final int ERROR_DIALOG = 1;
    private static final String ERROR_DIALOG_TAG = "ERROR_DIALOG_TAG";
    private void onSearchError(Throwable error) {
        error.printStackTrace();
        onSearchCompleted(new ArrayList<ItemLight>());
        DismissOnlyDialog dialog = DismissOnlyDialog.newInstance(ERROR_DIALOG, "Error", error.getMessage(), "dismiss");
        dialog.show(getFragmentManager(), ERROR_DIALOG_TAG);
    }

    @Override
    public void onUpdateSearchHint(String hint) {
        searchEt.setHint(hint);
    }

    @Override
    public void onSearchDrugsBy(String text, SearchType by, String industryCode) {
        // n.b.: since this activity is singleTop no new instance should be created an onNewIntent() should be fired
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(MainActivity.SEARCH_TEXT, text);
        intent.putExtra(MainActivity.SEARCH_TYPE, SearchType.DRUG);
        intent.putExtra(MainActivity.SEARCH_BY, by);
        intent.putExtra(MainActivity.SEARCH_INDUSTRY_CODE, industryCode);
        startActivity(intent);
    }

    @Override
    public void onSearchDrugByAic(String aic) {
        Intent intent = new Intent(this, DrugActivity.class);
        intent.putExtra(MainActivity.SEARCH_TEXT, aic);
        startActivity(intent);
    }

    private <T> ObservableTransformer<T, T> applyChain() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return (ObservableSource<T>) upstream
                        .compose(addToCompositeDisposable())
                        .compose(applySchedulers())
                        .compose(applyRequestStatus(SearchActivity.this));
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

    private <T> ObservableTransformer<T, T> applyRequestStatus(final SearchActivity searchActivity) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) throws Exception {
                                Timber.d("doOnSubscribe");
                                searchActivity.isSearchInProgress = true;
                                searchActivity.onSearchInProgress();
                            }
                        }).doOnTerminate(new Action() {
                            @Override
                            public void run() throws Exception {
                                Timber.d("doOnTerminate");
                                searchActivity.isSearchInProgress = false;
                                searchActivity.onSearchCompleted(searchActivity.items);
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

    /*public class OnSubscribeRefreshingCache<T> implements ObservableOnSubscribe<T> {

        private final AtomicBoolean refresh = new AtomicBoolean(false);
        private final Observable<T> source;
        private volatile Observable<T> current;

        public OnSubscribeRefreshingCache(Observable<T> source) {
            this.source = source;
            this.current = source;
        }

        public void reset() {
            refresh.set(true);
        }

        @Override
        public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
            if (refresh.compareAndSet(true, false)) {
                current = source.cache();
            }
            current.unsubscribeOn(Schedulers.computation());
        }
    }*/
}
