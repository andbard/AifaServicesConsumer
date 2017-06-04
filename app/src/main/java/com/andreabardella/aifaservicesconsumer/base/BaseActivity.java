package com.andreabardella.aifaservicesconsumer.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.andreabardella.aifaservicesconsumer.UnavailableSubComponentBuilderException;

import butterknife.ButterKnife;
import dagger.MembersInjector;

public abstract class BaseActivity extends AppCompatActivity {

    private String tag;

    private boolean componentReleasedDuringOnPause = false;

    protected abstract int getLayout();

    protected abstract <C extends MembersInjector> Class<C> getComponentClass();

    /**
     * Provide the module if its methods are not all static
     * @param <M> the module data type
     * @return the module instance
     */
    protected abstract <M> M getModule();

    private BasePresenter basePresenter;
    protected abstract <T extends BasePresenter> T getBasePresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tag = this.getClass().getSimpleName();
        Log.d(tag, "onCreate, savedInstanceState " + (savedInstanceState==null?'=':'!') + "= null");

        onCreateBeforeViewInjection(savedInstanceState);
        injectActivityViews(savedInstanceState);
        onCreateAfterViewInjection(savedInstanceState);

        onCreateBeforeDependencyInjection(savedInstanceState);
        injectActivityDependencies(savedInstanceState);
        onCreateAfterDependencyInjection(savedInstanceState);
    }

    /* VIEWS INJECTION -begin- */
    protected void onCreateBeforeViewInjection(Bundle savedInstanceState) {
        Log.v(tag, "onCreateBeforeViewInjection, savedInstanceState " + (savedInstanceState==null?'=':'!') + "= null");
    }

    /**
     * This method uses the resource returned by {@link BaseActivity#getLayout()}
     * to call {@link android.app.Activity#setContentView(int)}
     * and injects (thanks {@link ButterKnife}) the {@link android.view.View} instances into the extending activity
     */
    protected void injectActivityViews(Bundle savedInstanceState) {
        Log.d(tag, "injectActivityViews, savedInstanceState " + (savedInstanceState==null?'=':'!') + "= null");
        setContentView(getLayout());
        ButterKnife.bind(this);
    }

    protected void onCreateAfterViewInjection(Bundle savedInstanceState) {
        Log.v(tag, "onCreateAfterViewInjection, savedInstanceState " + (savedInstanceState==null?'=':'!') + "= null");
    }
    /* VIEWs INJECTION -end- */

    /* DEPENDENCIES INJECTION -begin- */
    protected void onCreateBeforeDependencyInjection(Bundle savedInstanceState) {
        Log.v(tag, "onCreateBeforeDependencyInjection, savedInstanceState " + (savedInstanceState==null?'=':'!') + "= null");
    }

    protected <Component extends MembersInjector> void injectActivityDependencies(Bundle savedInstanceState) {
        Log.d(tag, "injectActivityDependencies, savedInstanceState " + (savedInstanceState==null?'=':'!') + "= null");

        Component component = (Component) ((BaseApp) getApplication()).getComponent(this.getComponentClass());
        if (component == null) {
            Log.d(tag, "There is not a component associated to this activity => build it");
            try {
                component = (Component) ((BaseApp) getApplication()).buildSubComponent(this.getComponentClass(), this.getModule());
            } catch (UnavailableSubComponentBuilderException e) {
                Log.e(tag, e.getMessage(), e);
            }
        } else {
            Log.d(tag, "Retrieved the (already built) component associated to this activity");
        }
        if (component != null) {
            // inject the dependencies used by this activity
            component.injectMembers(this);
        }
    }

    protected void onCreateAfterDependencyInjection(Bundle savedInstanceState) {
        Log.v(tag, "onCreateAfterDependencyInjection, savedInstanceState " + (savedInstanceState==null?'=':'!') + "= null");
        setBasePresenter(getBasePresenter());
    }

    private void setBasePresenter(BasePresenter basePresenter) {
        this.basePresenter = basePresenter;
    }
    /* DEPENDENCIES INJECTION -begin- */

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(tag, "onResume");

        if (basePresenter != null) {
            basePresenter.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (basePresenter != null) {
            basePresenter.pause();
        }

        if (isFinishing()) {
            Log.d(tag, "onPause, isFinishing");
            onPauseBeforeRelease();
            componentReleasedDuringOnPause = true;
            releaseComponent();
            onPauseAfterRelease();
        } else {
            Log.d(tag, "onPause");
            componentReleasedDuringOnPause = false;
        }
    }

    protected void onPauseBeforeRelease() {
        Log.d(tag, "onPauseBeforeRelease");
    }

    protected void onPauseAfterRelease() {
        Log.d(tag, "onPauseAfterRelease");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(tag, "onSaveInstanceState");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Q.:
        // is it possible for onDestroy to be called with isFinishing set to true
        // even though the last onPause call had isFinishing set to false?
        // e.g.:
        // call startActivityForResult from A to B. A is paused and isFinishing is set to false;
        // in B setResult to CANCEL and finish B; A receives onActivityResult before restoring the activity
        // and if in onActivityResult finish is called, then onDestroy should be fired with isFinishing set to true
        // e.g.:
        // activity goes in background because user pressed the home button
        // => onPause is called with isFinishing set to false
        // after a while the system, requiring resources, removes the activity.
        // Is onDestroy called? Will it be preceded by onPause with isFinishing set to true?
        if (isFinishing()) {
            Log.d(tag, "onDestroy, isFinishing");
            if (!componentReleasedDuringOnPause) {
                Log.d(tag, "component NOT released within onPause => release it now");
                onDestroyBeforeRelease();
                releaseComponent();
                onDestroyAfterRelease();
            } else {
                Log.d(tag, "component already released within onPause");
            }
        } else {
            Log.d(tag, "onDestroy");
        }
    }

    protected void onDestroyBeforeRelease() {
        Log.d(tag, "onDestroyBeforeRelease");
    }

    protected void onDestroyAfterRelease() {
        Log.d(tag, "onDestroyAfterRelease");
    }

    protected void releaseComponent() {
        Log.d(tag, "releaseComponent");

        if (basePresenter != null) {
            basePresenter.release();
        }

        //App.getInstance().releaseSubComponent(getComponentClass());
        ((BaseApp) getApplication()).releaseSubComponent(this.getComponentClass());
    }
}
