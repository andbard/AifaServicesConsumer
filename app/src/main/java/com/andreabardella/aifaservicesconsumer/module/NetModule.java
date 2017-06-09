package com.andreabardella.aifaservicesconsumer.module;

import android.app.Application;

import com.andreabardella.aifaservicesconsumer.util.LoggingInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Module
public class NetModule {

    public static final String BASE_URL = "https://www.agenziafarmaco.gov.it";
    public static final String DOCS_URL = "https://farmaci.agenziafarmaco.gov.it/aifa/servlet/PdfDownloadServlet";
//    private String baseUrl = "https://www.agenziafarmaco.gov.it";

//    public NetModule(String baseUrl) {
//        this.baseUrl = baseUrl;
//    }

    @Provides
    @Singleton
    OkHttpClient
    provideOkHttpClient(Application application) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        setLoggingInterceptor(okHttpClientBuilder);
//        setTls(okHttpClientBuilder, application);

        return okHttpClientBuilder.build();
    }

    private static void setLoggingInterceptor(OkHttpClient.Builder builder) {
//        HttpLoggingInterceptor httpLogging = new HttpLoggingInterceptor(); // provided by square
        LoggingInterceptor logging = new LoggingInterceptor(); // custom
//        httpLogging.setLevel(HttpLoggingInterceptor.Level.BODY);
//        client.interceptors().add(httpLogging);
        builder.interceptors().add(logging);
    }

    @Provides
    @Singleton
    static Retrofit provideRetrofit(ObjectMapper mapper, OkHttpClient client) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()));
        return builder.build();
    }
}
