package org.qtum.wallet.ui.fragment.store_categories;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.qtum.wallet.ui.fragment.qstore.TestTokenObject;
import java.util.List;


public class StoreCategoriesAdapter extends RecyclerView.Adapter<StoreCategoryViewHolder> {

    private List<TestTokenObject> items;
    int resId;

    public StoreCategoriesAdapter(List<TestTokenObject> items, int resId) {
        this.items = items;
        this.resId = resId;
    }

    public void updateItems(List<TestTokenObject> items){
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public StoreCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StoreCategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(resId, parent, false));
    }

    @Override
    public void onBindViewHolder(StoreCategoryViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
