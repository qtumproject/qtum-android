package com.pixelplex.qtum.ui.fragment.QStore;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.pixelplex.qtum.R;

import java.util.ArrayList;
import java.util.List;


public class StoreTokensAdapter extends RecyclerView.Adapter<StoreTokenViewHolder> {

    int count;

    List<TestTokenObject> items;

    StoreItemClickListener listener;

    public StoreTokensAdapter(int count, StoreItemClickListener listener){
        this.count = count;
        items = new ArrayList<>();
        for (int i = 0; i < count; i ++){
            items.add(new TestTokenObject());
        }
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
