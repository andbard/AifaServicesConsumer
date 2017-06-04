package com.andreabardella.aifaservicesconsumer.base.component;

import com.andreabardella.aifaservicesconsumer.base.SubComponentBuilder;

import java.util.Map;

import javax.inject.Provider;

import dagger.MembersInjector;

public interface SubComponentBuildersProvider<T> extends MembersInjector<T> {
    Map<Class<? extends MembersInjector>, Provider<SubComponentBuilder>> subComponentBuilders();
}
