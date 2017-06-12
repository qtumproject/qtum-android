package com.pixelplex.qtum.ui.fragment.MyContractsFragment;

import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.datastorage.TinyDB;

/**
 * Created by max-v on 6/2/2017.
 */

public class MyContractsFragmentPresenter extends BaseFragmentPresenterImpl {

    MyContractsFragmentView mMyContractsFragmentView;

    MyContractsFragmentPresenter(MyContractsFragmentView myContractsFragmentView){
        mMyContractsFragmentView = myContractsFragmentView;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        TinyDB tinyDB = new TinyDB(getView().getContext());
        getView().updateRecyclerView(tinyDB.getContractList());
    }

    @Override
    public MyContractsFragmentView getView() {
        return mMyContractsFragmentView;
    }
}
