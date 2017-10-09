package org.qtum.wallet.ui.fragment.my_contracts_fragment;

import org.qtum.wallet.R;
import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.List;


public class MyContractsPresenterImpl extends BaseFragmentPresenterImpl implements MyContractsPresenter {

    private MyContractsView mMyContractsView;
    private MyContractsInteractor mMyContractsInteractor;

    MyContractsPresenterImpl(MyContractsView myContractsView, MyContractsInteractor myContractsInteractor) {
        mMyContractsView = myContractsView;
        mMyContractsInteractor = myContractsInteractor;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        List<Contract> contractList = getInteractor().getContracts();
        if (contractList != null) {
            if (contractList.size() != 0) {
                getView().updateRecyclerView(contractList);
            } else {
                getView().setPlaceHolder();
            }
        } else {
            getView().setAlertDialog(R.string.error, R.string.fail_to_get_contracts, BaseFragment.PopUpType.error);
        }
    }

    @Override
    public MyContractsView getView() {
        return mMyContractsView;
    }

    public MyContractsInteractor getInteractor() {
        return mMyContractsInteractor;
    }
}
