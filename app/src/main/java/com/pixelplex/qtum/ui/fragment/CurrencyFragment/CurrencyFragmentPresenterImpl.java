package com.pixelplex.qtum.ui.fragment.CurrencyFragment;


import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.Currency;
import com.pixelplex.qtum.model.CurrencyToken;
import com.pixelplex.qtum.model.contract.Token;
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
        List<Currency> currencyList = new ArrayList<>();
        Currency currency = new Currency("Qtum " + getView().getContext().getString(R.string.default_currency));
        currencyList.add(currency);
        for(Token token : getInteractor().getTokenList()){
            if(token.isHasBeenCreated() && token.isSubscribe()){
                currency = new CurrencyToken(token.getContractName(),token);
                currencyList.add(currency);
            }
        }
        getView().setCurrencyList(currencyList);
    }

    private CurrencyFragmentInteractorImpl getInteractor() {
        return mCurrencyFragmentInteractor;
    }
}
