package com.pixelplex.qtum.ui.fragment.QStore;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.QStore.categories.QstoreCategory;

import java.util.List;


class StoreAdapter extends RecyclerView.Adapter<StoreViewHolder> {

    private List<QstoreCategory> items;

    private StoreItemClickListener listener;

    public StoreAdapter(List<QstoreCategory> items, StoreItemClickListener listener){
        this.items = items;
        this.listener = listener;
    }

    @Override
    public StoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StoreViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_store_list_item, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(StoreViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
