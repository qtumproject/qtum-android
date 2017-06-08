package com.pixelplex.qtum.ui.fragment.CurrencyFragment;


import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.ContractInfo;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

import java.util.ArrayList;
import java.util.List;


class CurrencyFragmentPresenterImpl extends BaseFragmentPresenterImpl implements CurrencyFragmentPresenter{

    private CurrencyFragmentView mCurrencyFragmentView;
    private CurrencyFragmentInteractorImpl mCurrencyFragmentInteractor;

    CurrencyFragmentPresenterImpl(CurrencyFragmentView currencyFragmentView){
        mCurrencyFragmentView = currencyFragmentView;
        mCurrencyFragmentInteractor = new CurrencyFragmentInteractorImpl(getView().getContext());
    }

    @Override
    public CurrencyFragmentView getView() {
        return mCurrencyFragmentView;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        List<String> contractTokenList = new ArrayList<>();
        contractTokenList.add("Qtum (default currency)");
        for(ContractInfo contractInfo : getInteractor().getTokenList()){
            if(contractInfo.isToken()){
                contractTokenList.add(contractInfo.getContractName());
            }
        }
        getView().setTokenList(contractTokenList);
    }

    public CurrencyFragmentInteractorImpl getInteractor() {
        return mCurrencyFragmentInteractor;
    }
}
