package com.pixelplex.qtum.ui.fragment.QStore;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.gson.store.QSearchItem;
import com.pixelplex.qtum.model.gson.store.QstoreItem;

import java.util.List;


public class StoreSearchAdapter extends RecyclerView.Adapter<StoreSearchViewHolder> {

    private List<QSearchItem> items;

    private StoreItemClickListener listener;

    int resId;

    public StoreSearchAdapter(List<QSearchItem> items, StoreItemClickListener listener, int resId) {
        this.items = items;
        this.listener = listener;
        this.resId = resId;
    }

    public void updateItems(List<QSearchItem> items){
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public StoreSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StoreSearchViewHolder(LayoutInflater.from(parent.getContext()).inflate(resId, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(StoreSearchViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
