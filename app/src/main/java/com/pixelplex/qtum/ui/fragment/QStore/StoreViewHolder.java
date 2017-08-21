package com.pixelplex.qtum.ui.fragment.QStore;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.QStore.categories.QstoreCategory;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StoreViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.section_name)
    TextView sectionName;

    @BindView(R.id.list)
    RecyclerView list;

    private GridLayoutManager manager;

    private StoreTokensAdapter adapter;

    private StoreItemClickListener listener;

    int itemResId;

    public StoreViewHolder(View itemView, StoreItemClickListener listener, int itemResId) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.listener = listener;
        this.itemResId = itemResId;
    }

    public void bind(QstoreCategory item) {
        sectionName.setText(item.title);
        manager = new GridLayoutManager(list.getContext(),(item.items.size() <= 5)? 1 : item.items.size() / 5, LinearLayoutManager.HORIZONTAL,false);
        list.setLayoutManager(manager);
        adapter = new StoreTokensAdapter(item.items, listener, itemResId);
        list.setAdapter(adapter);
    }
}
