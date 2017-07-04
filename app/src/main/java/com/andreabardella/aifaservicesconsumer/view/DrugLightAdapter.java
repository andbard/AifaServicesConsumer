package com.andreabardella.aifaservicesconsumer.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andreabardella.aifaservicesconsumer.R;
import com.andreabardella.aifaservicesconsumer.model.DrugLight;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrugLightAdapter extends RecyclerView.Adapter<DrugLightAdapter.ViewHolder> {

    private Set<DrugLight> items;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.drug_list_item_drug_tv)
        TextView drug;
        @BindView(R.id.drug_list_item_aic_tv)
        TextView aic;
        @BindView(R.id.drug_list_item_company_tv)
        TextView company;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public DrugLightAdapter(Set<DrugLight> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drug_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int pos) {
        DrugLight item = (DrugLight) (items.toArray())[pos];
        viewHolder.drug.setText(item.getName());
        viewHolder.aic.setText(item.getCode());
        viewHolder.company.setText(item.getCompany());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
