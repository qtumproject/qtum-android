package org.qtum.wallet.ui.fragment.my_contracts_fragment;

import org.qtum.wallet.R;
import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.Iterator;
import java.util.List;


public class MyContractsPresenterImpl extends BaseFragmentPresenterImpl implements MyContractsPresenter {

    private MyContractsView mMyContractsView;
    private MyContractsInteractor mMyContractsInteractor;

    public MyContractsPresenterImpl(MyContractsView myContractsView, MyContractsInteractor myContractsInteractor) {
        mMyContractsView = myContractsView;
        mMyContractsInteractor = myContractsInteractor;
    }

    ContractItemListener mContractItemListener = new ContractItemListener() {
        @Override
        public void onUnsubscribeClick(Contract contract) {
            List<Contract> contractListWithoutTokens = getInteractor().getContractsWithoutTokens();
            for(Iterator<Contract> contractIterator = contractListWithoutTokens.iterator();contractIterator.hasNext();){
                if(contract.getContractAddress().equals(contractIterator.next().getContractAddress())){
                    contractIterator.remove();
                    getInteractor().setContractWithoutTokens(contractListWithoutTokens);
                    getView().updateRecyclerView(getInteractor().getContracts());
                    return;
                }
            }
            List<Token> tokens = getInteractor().getTokens();
            for(Iterator<Token> tokenIterator = tokens.iterator();tokenIterator.hasNext();){
                if(contract.getContractAddress().equals(tokenIterator.next().getContractAddress())){
                    tokenIterator.remove();
                    getInteractor().setTokens(tokens);
                    getView().updateRecyclerView(getInteractor().getContracts());
                    return;
                }
            }
        }
    };

    @Override
    public void initializeViews() {
        super.initializeViews();
        List<Contract> contractList = getInteractor().getContracts();
        if (contractList != null) {
            if (contractList.size() != 0) {
                getView().setUpRecyclerView(contractList, mContractItemListener);
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
