package com.andreabardella.aifaservicesconsumer.base;

import android.app.Application;
import android.util.Log;

import com.andreabardella.aifaservicesconsumer.UnavailableSubComponentBuilderException;
import com.andreabardella.aifaservicesconsumer.base.component.SubComponentBuildersProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Provider;

import dagger.MembersInjector;
import timber.log.Timber;

public abstract class BaseApp extends Application {
    private static final String TAG = "BaseApp";

    private static BaseApp app;

    public static BaseApp getInstance() {
        return app;
    }

    private Map<Class<? extends MembersInjector>, MembersInjector> componentMap;
    private Map<Class<? extends MembersInjector>, Provider<SubComponentBuilder>> builders;
    private Map<Class<? extends MembersInjector>, List<String>> buildersPerComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        BaseApp.app = this;

        componentMap = new HashMap<>();
        builders = new HashMap<>();
        buildersPerComponent = new HashMap<>();

        MembersInjector appComponent = buildAppComponent();
        Log.w(TAG, "appComponent.getClass = " + appComponent.getClass());
        componentMap.put(appComponent.getClass(), appComponent);

        List<String> builderNameList = new ArrayList<>();
        if (appComponent instanceof SubComponentBuildersProvider) {
            // if this root component is a SubComponentBuildersProvider then it might provide component builders (installed by the means of the related binding module)
            Map<Class<? extends MembersInjector>, Provider<SubComponentBuilder>> builders = ((SubComponentBuildersProvider) appComponent).subComponentBuilders();
            if (builders != null && !builders.isEmpty()) {
                // add the builders to the builders map
                for (Map.Entry<Class<? extends MembersInjector>, Provider<SubComponentBuilder>> entry : builders.entrySet()) {
                    this.builders.put(entry.getKey(), entry.getValue());
                    builderNameList.add(entry.getKey().getName());
                }
                Log.d(TAG, appComponent.getClass() + " has the following sub-component builders: " + builderNameList);
            }
        }

        // ...and map these builders to the component class that provides them
        buildersPerComponent.put(appComponent.getClass(), builderNameList);
    }

    protected abstract MembersInjector buildAppComponent();

    public MembersInjector getComponent(Class<? extends MembersInjector> key) {
        Log.d(TAG, "getComponent(" + key + ")");
        return componentMap.get(key);
    }

    public <Component extends MembersInjector> Component buildSubComponent(Class<Component> componentClass) throws UnavailableSubComponentBuilderException {
        return buildSubComponent(componentClass, null);
    }

    /**
     * Build a sub-component (injector).
     * The binding module,
     * i.e. the module that defines the binding delegations annotating its abstract methods
     * returning the {@link SubComponentBuilder} for the related component,
     * should have been installed within a parent component
     * that will provide the specified (binding module) builders
     *
     * @param componentClass the class of the component that is required to build
     * @param module the module related to the required component
     * @param <Component> the component's type
     * @param <Module> the module's type
     * @return the requested sub-component
     */
    public <Component extends MembersInjector, Module> Component buildSubComponent(Class<Component> componentClass, Module module) throws UnavailableSubComponentBuilderException {
        // get the required builder that should be installed in the parent component
        Provider<SubComponentBuilder> subComponentBuilderProvider = builders.get(componentClass);
        SubComponentBuilder builder;
        if (subComponentBuilderProvider == null || (builder = builders.get(componentClass).get()) == null) {
            Log.e(TAG, "No builder for the required sub-component is installed in any previously instantiated component");
            throw new UnavailableSubComponentBuilderException(componentClass);
        }

        // create the requested sub-component
        Component component = module == null ? (Component) builder.build() : (Component) builder.module(module).build();

        // add component into map
        componentMap.put(componentClass, component);

        // add and map builders eventually installed in this component
        addAndMapBuilders(component);

        return component;
    }

    private <Component extends MembersInjector> void addAndMapBuilders(Component component) {
        Log.v(TAG, "installed builders PRE insertion:\n" + buildersPerComponent);
        // add sub-component builders (installed in component)
        List<String> builderNameList = new ArrayList<>();
        if (component instanceof SubComponentBuildersProvider) {
            Map<Class<? extends MembersInjector>, Provider<SubComponentBuilder>> builders = ((SubComponentBuildersProvider) component).subComponentBuilders();
            if (builders != null && !builders.isEmpty()) {
                for (Map.Entry<Class<? extends MembersInjector>, Provider<SubComponentBuilder>> entry : builders.entrySet()) {
                    this.builders.put(entry.getKey(), entry.getValue());
                    builderNameList.add(entry.getKey().getName());
                }
            }
        }

        // ...and map these builders to the component class that provides them
        buildersPerComponent.put(component.getClass(), builderNameList);
        Log.d(TAG, "installed builders POST insertion:\n" + buildersPerComponent);
    }

    public void releaseSubComponent(Class<? extends MembersInjector> key) {
        Log.v(TAG, "installed builders PRE removal:\n" + buildersPerComponent);
        // remove the builders installed within the component to be removed
        List<String> builderClassNameList = buildersPerComponent.get(key);
        if (builderClassNameList != null) {
            for (String s : builderClassNameList) {
                try {
                    Class clazz = Class.forName(s);
                    this.builders.remove(clazz);
                } catch (ClassNotFoundException e) {
                    Log.w(TAG, "Unable to find a class named " + s, e);
                }
            }
        }
        buildersPerComponent.remove(key);
        // remove the component
        componentMap.remove(key);
        Log.d(TAG, "installed builders POST removal:\n" + buildersPerComponent);
    }
}
