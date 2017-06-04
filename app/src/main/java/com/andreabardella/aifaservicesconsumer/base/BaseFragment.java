package com.andreabardella.aifaservicesconsumer.base;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andreabardella.aifaservicesconsumer.UnavailableSubComponentBuilderException;
import com.andreabardella.aifaservicesconsumer.base.BaseApp;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.MembersInjector;

public abstract class BaseFragment extends Fragment {

    private boolean componentReleasedDuringOnPause = false;

    private Unbinder unbinder;

    protected abstract int getLayout();

    protected abstract ViewGroup getContainer();

    protected abstract <C extends MembersInjector> Class<C> getComponentClass();

    /**
     * Provide the module if its methods are not all static
     * @param <M> the module data type
     * @return the module instance
     */
    protected abstract <M> M getModule();

    private BasePresenter basePresenter;
    protected abstract <T extends BasePresenter> T getBasePresenter();

    private String tag() {
        return BaseFragment.this.getClass().getSimpleName();
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        Log.v(tag(), "onInflate, savedInstanceState " + (savedInstanceState==null?'=':'!') + "= null");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v(tag(), "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(tag(), "onCreate, savedInstanceState " + (savedInstanceState==null?'=':'!') + "= null");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(tag(), "onCreateView, savedInstanceState " + (savedInstanceState==null?'=':'!') + "= null");
        View view = inflater.inflate(getLayout(), getContainer(), false);

        onCreateViewBeforeViewInjection(savedInstanceState);
        injectFragmentViews(savedInstanceState, view);
        onCreateViewAfterViewInjection(savedInstanceState);

        return view;
    }

    /* VIEWS INJECTION -begin- */
    protected void onCreateViewBeforeViewInjection(Bundle savedInstanceState) {
        Log.v(getTag(), "onCreateViewBeforeViewInjection, savedInstanceState " + (savedInstanceState==null?'=':'!') + "= null");
    }

    protected void injectFragmentViews(Bundle savedInstanceState, View view) {
        unbinder = ButterKnife.bind(this, view);
    }

    protected void onCreateViewAfterViewInjection(Bundle savedInstanceState) {
        Log.v(getTag(), "onCreateViewAfterViewInjection, savedInstanceState " + (savedInstanceState==null?'=':'!') + "= null");
    }
    /* VIEWS INJECTION -end- */

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.v(tag(), "onViewCreated, savedInstanceState " + (savedInstanceState==null?'=':'!') + "= null");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(tag(), "onActivityCreated, savedInstanceState " + (savedInstanceState==null?'=':'!') + "= null");

        onActivityCreatedBeforeDependencyInjection(savedInstanceState);
        injectFragmentDependencies(savedInstanceState);
        onActivityCreatedAfterDependenciesInjection(savedInstanceState);
    }

    /* DEPENDENCIES INJECTION -begin- */
    protected void onActivityCreatedBeforeDependencyInjection(Bundle savedInstanceState) {
        Log.v(getTag(), "onCreateViewBeforeViewInjection, savedInstanceState " + (savedInstanceState==null?'=':'!') + "= null");
    }

    protected <Component extends MembersInjector> void injectFragmentDependencies(Bundle savedInstanceState) {
        Log.d(getTag(), "injectFragmentDependencies, savedInstanceState " + (savedInstanceState==null?'=':'!') + "= null");

        Component component = (Component) ((BaseApp) getActivity().getApplication()).getComponent(this.getComponentClass());
        if (component == null) {
            Log.d(getTag(), "There is not a component associated to this fragment => build it");
            try {
                component = (Component) ((BaseApp) getActivity().getApplication()).buildSubComponent(this.getComponentClass());
            } catch (UnavailableSubComponentBuilderException e) {
                Log.e(getTag(), e.getMessage(), e);
            }
        } else {
            Log.d(getTag(), "Retrieved the (already built) component associated to this fragment");
        }
        if (component != null) {
            // inject the dependencies used by this activity
            component.injectMembers(this);
        }
    }

    protected void onActivityCreatedAfterDependenciesInjection(Bundle savedInstanceState) {
        Log.v(getTag(), "onCreateViewAfterViewInjection, savedInstanceState " + (savedInstanceState==null?'=':'!') + "= null");
        setBasePresenter(this.getBasePresenter());
    }

    private void setBasePresenter(BasePresenter basePresenter) {
        this.basePresenter = basePresenter;
    }
    /* DEPENDENCIES INJECTION -end- */

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.v(tag(), "onViewStateRestored, savedInstanceState " + (savedInstanceState==null?'=':'!') + "= null");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(tag(), "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(tag(), "onResume");

        if (basePresenter != null) {
            basePresenter.resume();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.v(tag(), "onCreateOptionsMenu");
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Log.v(tag(), "onPrepareOptionsMenu");
    }

    @Override
    public void onPause() {
        super.onPause();

        if (basePresenter != null) {
            basePresenter.pause();
        }

        Log.d(tag(), "onPause, isRemoving = " + isRemoving() + ", getActivity().isFinishing = " + getActivity().isFinishing());
        if (isRemoving() || getActivity().isFinishing()) {
            Log.d(getTag(), "onPause, isRemoving or getActivity().isFinishing");
            onPauseBeforeRelease();
            componentReleasedDuringOnPause = true;
            releaseComponent();
            onPauseAfterRelease();
        } else {
            Log.d(getTag(), "onPause");
            componentReleasedDuringOnPause = false;
        }
    }

    protected void onPauseBeforeRelease() {
        Log.d(getTag(), "onPauseBeforeRelease");
    }

    protected void onPauseAfterRelease() {
        Log.d(getTag(), "onPauseAfterRelease");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v(tag(), "onSaveInstanceState");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(tag(), "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.v(tag(), "onDestroyView");
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(tag(), "onDestroy, isRemoving = " + isRemoving() + ", getActivity().isFinishing = " + getActivity().isFinishing());
        if (isRemoving() || getActivity().isFinishing()) {
            Log.d(getTag(), "onDestroy, isRemoving or getActivity().isFinishing");
            if (!componentReleasedDuringOnPause) {
                Log.d(getTag(), "component NOT released within onPause => release it now");
                onDestroyBeforeRelease();
                releaseComponent();
                onDestroyAfterRelease();
            } else {
                Log.d(getTag(), "component already released within onPause");
            }
        } else {
            Log.d(getTag(), "onDestroy");
        }
    }

    protected void onDestroyBeforeRelease() {
        Log.d(getTag(), "onDestroyBeforeRelease");
    }

    protected void onDestroyAfterRelease() {
        Log.d(getTag(), "onDestroyAfterRelease");
    }

    protected void releaseComponent() {
        Log.d(getTag(), "releaseComponent");

        if (basePresenter != null) {
            basePresenter.release();
        }

        ((BaseApp) getActivity().getApplication()).releaseSubComponent(this.getComponentClass());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v(tag(), "onDetach");
    }
}
