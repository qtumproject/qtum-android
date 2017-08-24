package com.pixelplex.qtum.ui.fragment.QStore;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pixelplex.qtum.model.gson.qstore.QstoreItem;

import java.util.List;


class StoreTokensAdapter extends RecyclerView.Adapter<StoreTokenViewHolder> {

    private List<QstoreItem> items;

    private StoreItemClickListener listener;

    int itemResId;

    public StoreTokensAdapter(List<QstoreItem> items, StoreItemClickListener listener, int itemResId){
        this.items = items;
        this.listener = listener;
        this.itemResId = itemResId;
    }

    @Override
    public StoreTokenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StoreTokenViewHolder(LayoutInflater.from(parent.getContext()).inflate(itemResId, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(StoreTokenViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
