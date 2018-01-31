package org.qtum.wallet.ui.fragment.wallet_fragment;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import org.qtum.wallet.model.gson.history.History;
import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public abstract class TransactionAdapter extends RealmRecyclerViewAdapter<History, RecyclerView.ViewHolder> {

    protected List<History> mHistoryList;
    protected History mHistory;
    protected final int TYPE_PROGRESS_BAR = 0;
    protected final int TYPE_TRANSACTION = 1;
    protected boolean mLoadingFlag = false;

    public TransactionAdapter(@Nullable OrderedRealmCollection<History> data, boolean autoUpdate, TransactionClickListener listener) {
        super(data, autoUpdate);
    }

    public TransactionAdapter(@Nullable OrderedRealmCollection<History> data, boolean autoUpdate, boolean updateOnModification) {
        super(data, autoUpdate, updateOnModification);
    }

    protected TransactionClickListener listener;

//    public TransactionAdapter(List<History> historyList, TransactionClickListener listener) {
//        mHistoryList = historyList;
//        this.listener = listener;
//    }

    @Override
    public int getItemViewType(int position) {
        if(position == super.getItemCount()){
            return TYPE_PROGRESS_BAR;
        }
        return TYPE_TRANSACTION;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount()+1;
    }

    public void setHistoryList(List<History> historyList) {
        mHistoryList = historyList;
    }

    public void setLoadingFlag(boolean loadingFlag) {
        mLoadingFlag = loadingFlag;
    }
}
