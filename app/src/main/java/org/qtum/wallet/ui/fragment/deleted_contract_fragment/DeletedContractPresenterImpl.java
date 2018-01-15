package org.qtum.wallet.ui.fragment.deleted_contract_fragment;

import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.Iterator;
import java.util.List;


public class DeletedContractPresenterImpl extends BaseFragmentPresenterImpl implements DeletedContractPresenter {

    private DeletedContractView mDeletedContractView;
    private DeletedContractInteractor mDeletedContractInteractor;

    DeletedContractPresenterImpl(DeletedContractView view, DeletedContractInteractor interactor){
        mDeletedContractView = view;
        mDeletedContractInteractor = interactor;
    }

    @Override
    public DeletedContractView getView() {
        return mDeletedContractView;
    }


    @Override
    public void onUnubscribeClick(String contractAddress) {
        List<Contract> contractListWithoutTokens = getInteractor().getContractsWithoutTokens();
        for (Iterator<Contract> contractIterator = contractListWithoutTokens.iterator(); contractIterator.hasNext(); ) {
            if (contractAddress.equals(contractIterator.next().getContractAddress())) {
                contractIterator.remove();
                getInteractor().setContractWithoutTokens(contractListWithoutTokens);
                return;
            }
        }
        List<Token> tokens = getInteractor().getTokens();
        for (Iterator<Token> tokenIterator = tokens.iterator(); tokenIterator.hasNext(); ) {
            if (contractAddress.equals(tokenIterator.next().getContractAddress())) {
                tokenIterator.remove();
                getInteractor().setTokens(tokens);
                return;
            }
        }
    }

    private DeletedContractInteractor getInteractor(){
        return mDeletedContractInteractor;
    }
}
