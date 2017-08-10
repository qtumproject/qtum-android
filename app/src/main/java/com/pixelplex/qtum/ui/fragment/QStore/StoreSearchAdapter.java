package com.pixelplex.qtum.ui.fragment.QStore;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.gson.store.QSearchItem;
import com.pixelplex.qtum.model.gson.store.QstoreItem;

import java.util.List;


class StoreSearchAdapter extends RecyclerView.Adapter<StoreSearchViewHolder> {

    private List<QSearchItem> items;

    private StoreItemClickListener listener;

    public StoreSearchAdapter(List<QSearchItem> items, StoreItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public void updateItems(List<QSearchItem> items){
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public StoreSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StoreSearchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_store_search_list_item, parent, false), listener);
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
