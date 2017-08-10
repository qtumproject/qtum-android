package com.pixelplex.qtum.ui.fragment.QStore.StoreContract;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.pixelplex.qtum.R;

/**
 * Created by kirillvolkov on 09.08.17.
 */

public class TagRecyclerViewAdapter extends RecyclerView.Adapter<TagViewHolder> {

    String[] tags;
    TagClickListener listener;

    public TagRecyclerViewAdapter(String[] tags, TagClickListener listener){
        this.tags = tags;
        this.listener = listener;
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TagViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false), listener);
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
