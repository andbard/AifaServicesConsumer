package com.andreabardella.aifaservicesconsumer.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andreabardella.aifaservicesconsumer.R;
import com.andreabardella.aifaservicesconsumer.SearchType;
import com.andreabardella.aifaservicesconsumer.base.BaseFragment;
import com.andreabardella.aifaservicesconsumer.model.ActiveIngredientLight;
import com.andreabardella.aifaservicesconsumer.model.DrugLight;
import com.andreabardella.aifaservicesconsumer.model.IndustryLight;
import com.andreabardella.aifaservicesconsumer.model.ItemLight;

import java.util.List;

import butterknife.BindView;
import timber.log.Timber;

public abstract class SearchFragment extends BaseFragment implements ItemLightAdapter.OnItemClickListener {

    public interface SearchListener {
        void onUpdateSearchHint(String hint);
        void onSearchDrugsBy(String text, SearchType by, String industryCode);
        void onSearchDrugByAic(String aic);
    }

    @BindView(R.id.search_fragment_filter_sv)
    SearchView filter;
    @BindView(R.id.search_fragment_pb)
    ProgressBar pb;
    @BindView(R.id.search_fragment_results)
    RecyclerView list;
    @BindView(R.id.search_fragment_empty_tv)
    TextView emptyTv;

    private SearchListener listener;

//    public void setSearchListener(SearchListener listener) {
//        this.listener = listener;
//    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) { // n.b.: called if API<23
        super.onAttach(activity);
        try {
            this.listener = (SearchListener) activity;
        } catch (ClassCastException e) {
            throw  new ClassCastException(activity.toString() + " must implement " + SearchListener.class.getSimpleName());
        }
    }

    @Override
    public void onAttach(Context context) { // n.b.: called if API>=23
        super.onAttach(context);
        try {
            this.listener = (SearchListener) context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() + " must implement " + SearchListener.class.getSimpleName());
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.search_fragment;
    }

    @Override
    protected ViewGroup getContainer() {
        return ((SearchActivity) getActivity()).getContainer();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // use this setting to improve performance if you know that changes in content do not change the layout size of the RecyclerView
//        list.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(list.getContext(), layoutManager.getOrientation());
        list.addItemDecoration(dividerItemDecoration);

        filter.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Timber.d("onQueryTextSubmit(%s)", query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Timber.d("onQueryTextChange(%s)", newText);
                filter(newText);
                return false;
            }
        });
    }

    @Override
    protected void onActivityCreatedAfterDependenciesInjection(Bundle savedInstanceState) {
        super.onActivityCreatedAfterDependenciesInjection(savedInstanceState);
        listener.onUpdateSearchHint(getSearchHint());
    }

    protected abstract String getSearchHint();

    public void onSearchInProgress() {
        pb.setVisibility(View.VISIBLE);
        list.setAdapter(null);
    }

    public void onSearchCompleted(SearchType type, List<ItemLight> items) {
        if (items != null && items.size() > 0) {
            emptyTv.setVisibility(View.GONE);
            ItemLightAdapter adapter = new ItemLightAdapter(items, type, this);
            list.setAdapter(adapter);
        } else {
            emptyTv.setVisibility(View.VISIBLE);
        }
        pb.setVisibility(View.GONE);
    }

    private void filter(String text) {
        if (list.getAdapter() != null) {
            ((ItemLightAdapter) list.getAdapter()).getFilter().filter(text);
        }
    }

    @Override
    public void onItemClick(ItemLight item) {
//        String str;
//        if (item instanceof DrugLight) {
//            str = "item -> {code:" + item.getCode() + ", name:" + item.getName() + ", industry:" + ((DrugLight) item).getIndustry() + "}";
//        } else {
//            str = "item -> {code:" + item.getCode() + ", name:" + item.getName() + "}";
//        }
//        Toast.makeText(SearchFragment.this.getActivity(), str, Toast.LENGTH_SHORT).show();

        if (item instanceof IndustryLight) {
            listener.onSearchDrugsBy(item.getName(), SearchType.INDUSTRY, item.getCode());
        } else if (item instanceof ActiveIngredientLight) {
            listener.onSearchDrugsBy(item.getName(), SearchType.ACTIVE_INGREDIENT, null);
        } else if (item instanceof DrugLight) {
            listener.onSearchDrugByAic(item.getCode());
        }
    }
}
