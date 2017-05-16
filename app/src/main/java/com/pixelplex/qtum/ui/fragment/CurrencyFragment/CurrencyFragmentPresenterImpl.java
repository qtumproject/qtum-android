package com.pixelplex.qtum.ui.fragment.CurrencyFragment;


import android.content.Context;

import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;


class CurrencyFragmentPresenterImpl extends BaseFragmentPresenterImpl implements CurrencyFragmentPresenter{

    private CurrencyFragmentView mCurrencyFragmentView;
    private CurrencyFragmentInteractorImpl mCurrencyFragmentInteractor;

    CurrencyFragmentPresenterImpl(CurrencyFragmentView currencyFragmentView){
        mCurrencyFragmentView = currencyFragmentView;
        mCurrencyFragmentInteractor = new CurrencyFragmentInteractorImpl();
    }

    @Override
    public CurrencyFragmentView getView() {
        return mCurrencyFragmentView;
    }

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        getView().setTokenList(getInteractor().getTokenList());
    }

    public CurrencyFragmentInteractorImpl getInteractor() {
        return mCurrencyFragmentInteractor;
    }
}
