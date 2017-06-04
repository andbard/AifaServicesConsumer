package com.andreabardella.aifaservicesconsumer.base;

import dagger.MembersInjector;

public interface SubComponentBuilder<Module, Component extends MembersInjector> {
    SubComponentBuilder<Module, Component> module(Module module);
    Component build();
}
