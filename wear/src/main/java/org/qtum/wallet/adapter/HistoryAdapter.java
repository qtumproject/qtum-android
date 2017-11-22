package org.qtum.wallet.adapter;


import android.support.v7.widget.RecyclerView;
import android.support.wear.widget.WearableRecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import org.qtum.wallet.R;
import org.qtum.wallet.entity.HeaderData;
import org.qtum.wallet.entity.History;
import org.qtum.wallet.listener.HeaderClickListener;
import org.qtum.wallet.listener.ItemClickListener;
import org.qtum.wallet.storage.DataStorage;
import org.qtum.wallet.viewholder.HeaderViewHolder;
import org.qtum.wallet.viewholder.HistoryViewHolder;

import java.util.List;

/**
 * Created by kirillvolkov on 22.11.2017.
 */

public class HistoryAdapter extends WearableRecyclerView.Adapter {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    List<History> items;
    HeaderData headerData;

    ItemClickListener itemClickListener;
    HeaderClickListener headerClickListener;

    public HistoryAdapter(List<History> items, HeaderData headerData, ItemClickListener itemClickListener, HeaderClickListener headerClickListener) {
        this.items = items;
        this.headerData = headerData;
        this.itemClickListener = itemClickListener;
        this.headerClickListener = headerClickListener;
    }

    public void updateHistory(List<History> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void addToEnd(List<History> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void addToStart(List<History> items) {
        this.items.addAll(0, items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    @Override
    public WearableRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            return new HistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false), itemClickListener);
        } else {
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false), headerClickListener);
        }
    }

    @Override
    public void onBindViewHolder(WearableRecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HistoryViewHolder) {
            ((HistoryViewHolder)holder).bind(getItem(position));
        } else {
            ((HeaderViewHolder)holder).bind(headerData);
        }
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        return items.size() + 1;
    }

    private History getItem(int position) {
        return items.get(position - 1);
    }
}
