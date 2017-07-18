package com.pixelplex.qtum.ui.fragment.MyContractsFragment;

import com.pixelplex.qtum.model.contract.Contract;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.datastorage.TinyDB;

import java.util.List;


public class MyContractsFragmentPresenter extends BaseFragmentPresenterImpl {

    private MyContractsFragmentView mMyContractsFragmentView;

    MyContractsFragmentPresenter(MyContractsFragmentView myContractsFragmentView){
        mMyContractsFragmentView = myContractsFragmentView;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        TinyDB tinyDB = new TinyDB(getView().getContext());
        List<Contract> contractList = tinyDB.getContractList();
        if(contractList != null) {
            getView().updateRecyclerView(contractList);
        } else {
            getView().setAlertDialog("Error","Fail to get contracts", BaseFragment.PopUpType.error);
        }
    }

    @Override
    public MyContractsFragmentView getView() {
        return mMyContractsFragmentView;
    }
}
