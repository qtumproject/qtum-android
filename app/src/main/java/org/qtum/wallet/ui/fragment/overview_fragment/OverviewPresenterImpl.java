package org.qtum.wallet.ui.fragment.overview_fragment;


import android.util.Pair;

import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.history.TransactionReceipt;
import org.qtum.wallet.model.gson.token_history.TokenHistory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class OverviewPresenterImpl extends BaseFragmentPresenterImpl implements OverviewPresenter {

    private OverviewIteractor mOverviewIteractor;
    private OverviewView mOverviewView;

    OverviewPresenterImpl(OverviewView overviewView, OverviewIteractor overviewIteractor){
        mOverviewView = overviewView;
        mOverviewIteractor = overviewIteractor;
    }

    @Override
    public OverviewView getView() {
        return mOverviewView;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        final List<CopyableOverviewItem> overview = new ArrayList<>();
        TransactionReceipt transactionReceipt = getIteractor().getReceiptByRxhHashFromRealm(getView().getTxHash());
        switch (getView().getHistoryType()){
            case Token_History:
                TokenHistory tokenHistory = getIteractor().getTokenHistory(getView().getTxHash());
                overview.add(new CopyableOverviewItem("TxHash", tokenHistory.getTxHash(),true));
                overview.add(new CopyableOverviewItem("Contract Address", transactionReceipt.getContractAddress(), true));
                overview.add(new CopyableOverviewItem("From", transactionReceipt.getFrom(), false));
                overview.add(new CopyableOverviewItem("To", transactionReceipt.getTo(), false));
                overview.add(new CopyableOverviewItem("Cumulative Gas Used", String.valueOf(transactionReceipt.getCumulativeGasUsed()), false));
                overview.add(new CopyableOverviewItem("Gas Used", String.valueOf(transactionReceipt.getGasUsed()), false));
                break;
            case History:
                History history = getIteractor().getHistory(getView().getTxHash());
                overview.add(new CopyableOverviewItem("TxHash", history.getTxHash(),true));
                overview.add(new CopyableOverviewItem("BlockHash", history.getBlockHash(),true));
                overview.add(new CopyableOverviewItem("Block Height", String.valueOf(history.getBlockHeight()),true));
                if(history.isContractType()) {
                    overview.add(new CopyableOverviewItem("Contract Address", transactionReceipt.getContractAddress(), true));
                    overview.add(new CopyableOverviewItem("From", transactionReceipt.getFrom(), false));
                    overview.add(new CopyableOverviewItem("To", transactionReceipt.getTo(), false));
                    overview.add(new CopyableOverviewItem("Cumulative Gas Used", String.valueOf(transactionReceipt.getCumulativeGasUsed()), false));
                    overview.add(new CopyableOverviewItem("Gas Used", String.valueOf(transactionReceipt.getGasUsed()), false));
                }
                break;
        }
        getView().setUpOverview(overview);
    }

    private OverviewIteractor getIteractor(){
        return mOverviewIteractor;
    }
}
