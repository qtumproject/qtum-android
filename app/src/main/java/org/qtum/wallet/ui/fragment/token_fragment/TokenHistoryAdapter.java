package org.qtum.wallet.ui.fragment.token_fragment;

import android.support.v7.widget.RecyclerView;

import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.token_history.TokenHistory;
import org.qtum.wallet.ui.fragment.wallet_fragment.TransactionClickListener;

import java.util.List;

public abstract class TokenHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected List<TokenHistory> mHistoryList;
    protected TokenHistory mHistory;
    protected final int TYPE_PROGRESS_BAR = 0;
    protected final int TYPE_TRANSACTION = 1;
    protected boolean mLoadingFlag = false;
    protected String mSymbol = "";

    public TokenHistory getItem(int position){
        return mHistoryList.get(position);
    }

    protected TokenHistoryClickListener listener;
    protected int decimalUnits = 0;

    public TokenHistoryAdapter(List<TokenHistory> historyList, TokenHistoryClickListener listener, int decimalUnits) {
        mHistoryList = historyList;
        this.listener = listener;
        this.decimalUnits = decimalUnits;
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

    public void setHistoryList(List<TokenHistory> historyList) {
        mHistoryList = historyList;
    }

    public void setLoadingFlag(boolean loadingFlag) {
        mLoadingFlag = loadingFlag;
    }

    public void setSymbol(String symbol) {
        mSymbol = symbol;
    }
}
