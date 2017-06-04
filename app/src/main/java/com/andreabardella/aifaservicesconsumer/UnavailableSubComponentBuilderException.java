package com.andreabardella.aifaservicesconsumer;

import dagger.MembersInjector;

public class UnavailableSubComponentBuilderException extends Exception {

    public UnavailableSubComponentBuilderException(Class<? extends MembersInjector> componentClass) {
        super("Unable to build " + componentClass.getSimpleName() + ": " +
                "no builder for the requested sub-component is installed in any previously instantiated component");
    }
}
