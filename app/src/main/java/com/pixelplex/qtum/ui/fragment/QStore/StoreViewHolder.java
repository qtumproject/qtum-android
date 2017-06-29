package com.pixelplex.qtum.ui.fragment.QStore;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pixelplex.qtum.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kirillvolkov on 28.06.17.
 */

public class StoreViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.section_name)
    TextView sectionName;

    @BindView(R.id.list)
    RecyclerView list;

    GridLayoutManager manager;

    StoreTokensAdapter adapter;

    StoreItemClickListener listener;

    public StoreViewHolder(View itemView, StoreItemClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.listener = listener;
    }

    public void bind(TestStoreObject item) {
        sectionName.setText(item.title);
        manager = new GridLayoutManager(list.getContext(),item.rowCount, LinearLayoutManager.HORIZONTAL,false);
        list.setLayoutManager(manager);
        adapter = new StoreTokensAdapter(item.itemsCount * item.rowCount, listener);
        list.setAdapter(adapter);
    }
}
