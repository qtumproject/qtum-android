package org.qtum.wallet.ui.fragment.qstore;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.qtum.wallet.ui.fragment.qstore.categories.QstoreCategory;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreViewHolder> {

    private List<QstoreCategory> items;

    private StoreItemClickListener listener;

    int catResId, itemResId;

    public StoreAdapter(List<QstoreCategory> items, StoreItemClickListener listener, int catResId, int itemResId) {
        this.items = items;
        this.listener = listener;
        this.catResId = catResId;
        this.itemResId = itemResId;
    }

    @Override
    public StoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StoreViewHolder(LayoutInflater.from(parent.getContext()).inflate(catResId, parent, false), listener, itemResId);
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
