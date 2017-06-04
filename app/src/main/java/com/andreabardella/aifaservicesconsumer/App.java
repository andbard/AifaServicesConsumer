package com.andreabardella.aifaservicesconsumer;

import android.os.StrictMode;
import android.util.Log;

import com.andreabardella.aifaservicesconsumer.base.BaseApp;
import com.andreabardella.aifaservicesconsumer.base.component.SubComponentBuildersProvider;
import com.andreabardella.aifaservicesconsumer.component.AppComponent;
import com.andreabardella.aifaservicesconsumer.component.DaggerAppComponent;
import com.andreabardella.aifaservicesconsumer.module.AppModule;
import com.andreabardella.aifaservicesconsumer.util.DevelopmentTree;

import timber.log.Timber;

public class App extends BaseApp {

    private static final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected SubComponentBuildersProvider buildAppComponent() {
        if (BuildConfig.DEBUG) {
            configureLogging();
            configureStrictMode();
        }

        AppComponent component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        return component;
    }

    private void configureLogging() {
        Log.d(TAG, "configureLogging");
        Timber.plant(new DevelopmentTree());
    }

    private void configureStrictMode() {
        Log.d(TAG, "configureStrictMode");
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDeathOnNetwork()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .detectActivityLeaks()
                .penaltyLog()
                .build());
    }
}
