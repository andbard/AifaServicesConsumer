package com.andreabardella.aifaservicesconsumer.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.andreabardella.aifaservicesconsumer.R;
import com.andreabardella.aifaservicesconsumer.SearchType;
import com.andreabardella.aifaservicesconsumer.model.ActiveIngredientLight;
import com.andreabardella.aifaservicesconsumer.model.DrugLight;
import com.andreabardella.aifaservicesconsumer.model.IndustryLight;
import com.andreabardella.aifaservicesconsumer.model.ItemLight;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ItemLightAdapter extends RecyclerView.Adapter<ItemLightAdapter.ViewHolder> implements Filterable {

    public interface OnItemClickListener {
        void onItemClick(ItemLight itemLight);
    }

    private OnItemClickListener listener;

    private List<ItemLight> originalItems;
    private List<ItemLight> items;
    private SearchType type = null;

    /* Constructors */
    public ItemLightAdapter(List<ItemLight> list, OnItemClickListener listener) {
        this.originalItems = list;
        if (list != null) {
            items = new ArrayList<>(list);
        } else {
            items = new ArrayList<>();
        }
        this.listener = listener;
    }

    public ItemLightAdapter(List<ItemLight> list, SearchType type, OnItemClickListener listener) {
        this(list, listener);
        this.type = type;

    }

    /* ViewHolders */
    static class ViewHolder extends RecyclerView.ViewHolder {
        OnItemClickListener listener;
        private ItemLight item;

        void setItem(ItemLight item, OnItemClickListener listener) {
            ViewHolder.this.listener = listener;
            this.item = item;
        }

        ItemLight getItem() {
            return item;
        }

        ViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class ActiveIngredientViewHolder extends ViewHolder {
        @BindView(R.id.active_ingredient_list_item_tv)
        TextView activeIngredient;
        ActiveIngredientViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            activeIngredient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(getItem());
                }
            });
        }
    }

    static class IndustryViewHolder extends ViewHolder {
        @BindView(R.id.industry_list_item_tv)
        TextView industry;
        IndustryViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            industry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(getItem());
                }
            });
        }
    }

    static class DrugViewHolder extends ViewHolder {
        @BindView(R.id.drug_list_item_ll)
        View item;
        @BindView(R.id.drug_list_item_drug_tv)
        TextView drug;
        @BindView(R.id.drug_list_item_aic_tv)
        TextView aic;
        @BindView(R.id.drug_list_item_industry_tv)
        TextView industry;
        DrugViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(getItem());
                }
            });
        }
    }

    @Override
    public int getItemViewType(int pos) {
        if (type != null) {
            return type.getId();
        } else if (items.get(pos) instanceof DrugLight) {
            return SearchType.DRUG.getId();
        } else if (items.get(pos) instanceof ActiveIngredientLight) {
            return SearchType.ACTIVE_INGREDIENT.getId();
        } else if (items.get(pos) instanceof IndustryLight) {
            return SearchType.INDUSTRY.getId();
        } else {
            return 0;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == SearchType.ACTIVE_INGREDIENT.getId()) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.active_ingredient_list_item, parent, false);
            return new ItemLightAdapter.ActiveIngredientViewHolder(view);
        } else if (viewType == SearchType.DRUG.getId()) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drug_list_item, parent, false);
            return new ItemLightAdapter.DrugViewHolder(view);
        } else if (viewType == SearchType.INDUSTRY.getId()) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.indutry_list_item, parent, false);
            return new ItemLightAdapter.IndustryViewHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int pos) {
        ItemLight item = items.get(pos);
        viewHolder.setItem(item, ItemLightAdapter.this.listener);
        if (getItemViewType(pos) == SearchType.DRUG.getId()) {
            DrugLight drug = (DrugLight) item;
            ((DrugViewHolder) viewHolder).drug.setText(drug.getName());
            ((DrugViewHolder) viewHolder).aic.setText(drug.getCode());
            ((DrugViewHolder) viewHolder).industry.setText(drug.getIndustry());
        } else if (getItemViewType(pos) == SearchType.ACTIVE_INGREDIENT.getId()) {
            ActiveIngredientLight activeIngredient = (ActiveIngredientLight) item;
            ((ActiveIngredientViewHolder) viewHolder).activeIngredient.setText(activeIngredient.getName());
        } else if (getItemViewType(pos) == SearchType.INDUSTRY.getId()) {
            IndustryLight industry = (IndustryLight) item;
            ((IndustryViewHolder) viewHolder).industry.setText(industry.getName());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public Filter getFilter() {
        return new ItemFilter(originalItems);
    }

    private class ItemFilter extends Filter {

        private List<ItemLight> sourceList;

        ItemFilter(List<ItemLight> list) {
            this.sourceList = new ArrayList<>();
            this.sourceList.addAll(list);
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (sourceList != null && sourceList.size() > 1) {
                if (constraint != null && constraint.length() > 0) {
                    constraint = constraint.toString().toLowerCase();
                    synchronized (this) {
                        List<ItemLight> filteredList = new ArrayList<>();
                        for (ItemLight item : sourceList) {
                            if (getItemViewType(0) == SearchType.DRUG.getId()) {
                                DrugLight drug = (DrugLight) item;
                                String name = drug.getName().toLowerCase();
                                String aic = drug.getCode().toLowerCase();
                                String industry = drug.getIndustry().toLowerCase();
                                if (name.contains(constraint) || aic.contains(constraint) || industry.contains(constraint)) {
                                    filteredList.add(item);
                                }
                            } else {
                                String name = item.getName().toLowerCase();
                                if (name.contains(constraint)) {
                                    filteredList.add(item);
                                }
                            }
                        }
                        filterResults.values = filteredList;
                        filterResults.count = filteredList.size();
                    }
                } else {
                    synchronized (this) {
                        filterResults.values = sourceList;
                        filterResults.count = sourceList.size();
                    }
                }
            } else {
                synchronized (this) {
                    filterResults.values = sourceList;
                    filterResults.count = sourceList.size();
                }
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results != null && results.values != null) {
                ItemLightAdapter.this.items = new ArrayList<>();
                ItemLightAdapter.this.items.addAll((Collection<? extends ItemLight>) results.values);
                ItemLightAdapter.this.notifyDataSetChanged();
            }
        }
    }
}
