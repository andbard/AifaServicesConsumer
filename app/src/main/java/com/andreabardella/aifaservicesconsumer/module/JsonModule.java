package com.andreabardella.aifaservicesconsumer.module;

import com.andreabardella.aifaservicesconsumer.util.JsonHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class JsonModule {

    @Provides
    @Singleton
    static ObjectMapper provideObjectMapper() {
        return new ObjectMapper();
    }

    @Provides
    @Singleton
    static JsonHelper provideJsonHelper(ObjectMapper mapper) {
        JsonHelper helper = new JsonHelper();
        JsonHelper.setMapper(mapper);
        // no sense since the JsonHelper methods are static
        return helper;
    }
}
