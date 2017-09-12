package org.qtum.wallet.ui.fragment.my_contracts_fragment;

import org.qtum.wallet.R;
import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.datastorage.TinyDB;

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
            getView().setAlertDialog(getView().getContext().getString(R.string.error),getView().getContext().getString(R.string.fail_to_get_contracts), BaseFragment.PopUpType.error);
        }
    }

    @Override
    public MyContractsFragmentView getView() {
        return mMyContractsFragmentView;
    }
}
