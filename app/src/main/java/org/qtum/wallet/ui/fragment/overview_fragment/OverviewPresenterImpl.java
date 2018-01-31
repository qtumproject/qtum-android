package org.qtum.wallet.ui.fragment.overview_fragment;


import android.util.Pair;

import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.ArrayList;
import java.util.List;

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
        History history = getIteractor().getHistory(getView().getTxHash());
        List<CopyableOverviewItem> overview = new ArrayList<>();
        overview.add(new CopyableOverviewItem("TxHash", history.getTxHash(),true));
        overview.add(new CopyableOverviewItem("BlockHash", history.getBlockHash(),true));
        overview.add(new CopyableOverviewItem("Block Height", String.valueOf(history.getBlockHeight()),true));
        if(history.getTransactionReceipt()!=null){
            overview.add(new CopyableOverviewItem("Contract Address",history.getTransactionReceipt().getContractAddress(),true));
            overview.add(new CopyableOverviewItem("From",history.getTransactionReceipt().getFrom(),false));
            overview.add(new CopyableOverviewItem("To",history.getTransactionReceipt().getTo(),false));
            overview.add(new CopyableOverviewItem("Cumulative Gas Used",String.valueOf(history.getTransactionReceipt().getCumulativeGasUsed()),false));
            overview.add(new CopyableOverviewItem("Gas Used",String.valueOf(history.getTransactionReceipt().getGasUsed()),false));
        }
        getView().setUpOverview(overview);
    }

    private OverviewIteractor getIteractor(){
        return mOverviewIteractor;
    }
}
