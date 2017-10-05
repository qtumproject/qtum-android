package org.qtum.wallet.ui.fragment.store_categories;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.qtum.wallet.model.gson.QstoreContractType;

import java.util.List;


public class StoreCategoriesAdapter extends RecyclerView.Adapter<StoreCategoryViewHolder> {

    private List<QstoreContractType> items;
    int resId;
    StoreCategoryViewHolder.OnCategoryClickListener mListener;

    public StoreCategoriesAdapter(List<QstoreContractType> items, int resId, StoreCategoryViewHolder.OnCategoryClickListener listener) {
        this.items = items;
        this.resId = resId;
        mListener = listener;
    }

    public void updateItems(List<QstoreContractType> items){
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public StoreCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StoreCategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(resId, parent, false));
    }

    @Override
    public void onBindViewHolder(StoreCategoryViewHolder holder, int position) {
        holder.bind(items.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
