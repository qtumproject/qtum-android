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
        History history = getIteractor().getHistory(getView().getPosition());
        List<Pair<String, String>> overview = new ArrayList<>();
        overview.add(new Pair<>("TxHash", history.getTxHash()));
        overview.add(new Pair<>("BlockHash", history.getBlockHash()));
        overview.add(new Pair<>("Block Height", String.valueOf(history.getBlockHeight())));
        if(history.getTransactionReceipt()!=null){
            overview.add(new Pair<>("Contract Address",history.getTransactionReceipt().getContractAddress()));
            overview.add(new Pair<>("From",history.getTransactionReceipt().getFrom()));
            overview.add(new Pair<>("To",history.getTransactionReceipt().getTo()));
            overview.add(new Pair<>("Cumulative Gas Used",String.valueOf(history.getTransactionReceipt().getCumulativeGasUsed())));
            overview.add(new Pair<>("Gas Used",String.valueOf(history.getTransactionReceipt().getGasUsed())));
        }
        getView().setUpOverview(overview);
    }

    private OverviewIteractor getIteractor(){
        return mOverviewIteractor;
    }
}
