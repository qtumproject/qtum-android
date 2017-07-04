package com.pixelplex.qtum.ui.fragment.QStore.StoreCategories;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.QStore.TestTokenObject;
import java.util.List;


class StoreCategoriesAdapter extends RecyclerView.Adapter<StoreCategoryViewHolder> {

    private List<TestTokenObject> items;

    public StoreCategoriesAdapter(List<TestTokenObject> items) {
        this.items = items;
    }

    public void updateItems(List<TestTokenObject> items){
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public StoreCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StoreCategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_store_category_list_item, parent, false));
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
