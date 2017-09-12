package org.qtum.wallet.ui.fragment.currency_fragment;


import org.qtum.wallet.R;
import org.qtum.wallet.model.Currency;
import org.qtum.wallet.model.CurrencyToken;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

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
