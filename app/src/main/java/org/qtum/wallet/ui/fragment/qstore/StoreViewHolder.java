package org.qtum.wallet.ui.fragment.qstore;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.qtum.wallet.ui.fragment.qstore.categories.QstoreCategory;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoreViewHolder extends RecyclerView.ViewHolder {

    @BindView(org.qtum.wallet.R.id.section_name)
    TextView sectionName;

    @BindView(org.qtum.wallet.R.id.list)
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
        sectionName.setText(item.getTitle());
        manager = new GridLayoutManager(list.getContext(), (item.getItems().size() <= 5) ? 1 : item.getItems().size() / 5, LinearLayoutManager.HORIZONTAL, false);
        list.setLayoutManager(manager);
        adapter = new StoreTokensAdapter(item.getItems(), listener, itemResId);
        list.setAdapter(adapter);
    }
}
