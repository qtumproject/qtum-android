package com.pixelplex.qtum.ui.fragment.WalletFragment;

import android.support.v7.widget.RecyclerView;
import com.pixelplex.qtum.model.gson.history.History;
import java.util.List;

/**
 * Created by kirillvolkov on 05.07.17.
 */

public abstract class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected List<History> mHistoryList;
    protected History mHistory;
    protected final int TYPE_PROGRESS_BAR = 0;
    protected final int TYPE_TRANSACTION = 1;

    protected TransactionClickListener listener;

    public TransactionAdapter(List<History> historyList, TransactionClickListener listener) {
        mHistoryList = historyList;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == mHistoryList.size()){
            return TYPE_PROGRESS_BAR;
        }
        return TYPE_TRANSACTION;
    }

    @Override
    public int getItemCount() {
        return mHistoryList.size()+1;
    }

    public void setHistoryList(List<History> historyList) {
        mHistoryList = historyList;
    }
}
