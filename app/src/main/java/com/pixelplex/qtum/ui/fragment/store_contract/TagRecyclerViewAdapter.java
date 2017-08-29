package com.pixelplex.qtum.ui.fragment.store_contract;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by kirillvolkov on 09.08.17.
 */

public class TagRecyclerViewAdapter extends RecyclerView.Adapter<TagViewHolder> {

    String[] tags;
    TagClickListener listener;
    private int resId;

    public TagRecyclerViewAdapter(String[] tags, TagClickListener listener, int resId){
        this.tags = tags;
        this.listener = listener;
        this.resId = resId;
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TagViewHolder(LayoutInflater.from(parent.getContext()).inflate(resId, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(TagViewHolder holder, int position) {
        holder.bind(tags[position]);
    }

    @Override
    public int getItemCount() {
        return tags.length;
    }
}
