package com.andreabardella.aifaservicesconsumer.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.andreabardella.aifaservicesconsumer.R;
import com.andreabardella.aifaservicesconsumer.model.Packaging;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PackagingListAdapter extends ArrayAdapter<Packaging> {

    private Activity activity;
    private int resId;
    private List<Packaging> list;

    public PackagingListAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Packaging> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.resId = resource;
        this.list = objects;
    }

    static class ViewHolder {
        @BindView(R.id.packaging_list_item_name_tv)
        TextView name;
        @BindView(R.id.packaging_list_item_aic_tv)
        TextView aic;
        @BindView(R.id.packaging_list_item_status_tv)
        TextView status;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(resId, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(list.get(position).getDescription());
        holder.aic.setText(list.get(position).getAic());
        holder.status.setText(list.get(position).getStatus());

        return convertView;
    }
}
