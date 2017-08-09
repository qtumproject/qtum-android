package com.pixelplex.qtum.ui.fragment.QStore;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.gson.store.QstoreItem;

import java.util.ArrayList;
import java.util.List;


class StoreTokensAdapter extends RecyclerView.Adapter<StoreTokenViewHolder> {

    private int count;

    private List<QstoreItem> items;

    private StoreItemClickListener listener;

    public StoreTokensAdapter(List<QstoreItem> items, StoreItemClickListener listener){
        this.items = items;
        this.listener = listener;
    }

    @Override
    public StoreTokenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StoreTokenViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_store_token_list_item, parent, false), listener);
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
