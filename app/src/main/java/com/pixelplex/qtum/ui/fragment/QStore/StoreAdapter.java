package com.pixelplex.qtum.ui.fragment.QStore;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pixelplex.qtum.R;

import java.util.ArrayList;
import java.util.List;


public class StoreAdapter extends RecyclerView.Adapter<StoreViewHolder> {

    List<TestStoreObject> items;

    StoreItemClickListener listener;

    public StoreAdapter(StoreItemClickListener listener){
        items = new ArrayList<>();
        items.add(new TestStoreObject(2,"Tranding Now",10));
        items.add(new TestStoreObject(1,"What's New",15));
        items.add(new TestStoreObject(3,"Other",5));
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
