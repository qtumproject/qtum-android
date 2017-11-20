package org.qtum.wallet.ui.fragment.token_fragment;

import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class TokenPresenterImpl extends BaseFragmentPresenterImpl implements TokenPresenter {

    private TokenView view;
    private TokenInteractor interactor;
    private Token token;
    private String abi;

    public TokenPresenterImpl(TokenView view, TokenInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        setQtumAddress();

        if (token.getDecimalUnits() == null) {
            getInteractor().setupPropertyDecimalsValue(token, getView().getDecimalsValueCallback());
        } else {
            getView().onContractPropertyUpdated(TokenFragment.decimals, String.valueOf(token.getDecimalUnits()));
            getView().setBalance(token.getTokenBalanceWithDecimalUnits().toString());
            getInteractor().setupPropertyTotalSupplyValue(token, getView().getTotalSupplyValueCallback());
        }

        getInteractor().setupPropertySymbolValue(token, getView().getSymbolValueCallback());
        getInteractor().setupPropertyNameValue(token, getView().getNameValueCallback());
    }

    @Override
    public Token getToken() {
        return token;
    }

    public String getAbi() {
        abi = (getView().isAbiEmpty(abi)) ? getInteractor().readAbiContract(token.getUiid()) : abi;
        return abi;
    }

    @Override
    public void setToken(Token token) {
        this.token = token;
    }

    private void setQtumAddress() {
        getView().setQtumAddress(getInteractor().getCurrentAddress());
    }

    @Override
    public TokenView getView() {
        return view;
    }

    public TokenInteractor getInteractor() {
        return interactor;
    }

    @Override
    public void onDecimalsPropertySuccess(String value) {
        token = getInteractor().setTokenDecimals(token, value);
        getView().setBalance(token.getTokenBalanceWithDecimalUnits().toString());
        getInteractor().setupPropertyTotalSupplyValue(token, getView().getTotalSupplyValueCallback());
    }

    @Override
    public String onTotalSupplyPropertySuccess(Token token, String value) {
        return getInteractor().handleTotalSupplyValue(token, value);
    }

    public void setAbi(String abi) {
        this.abi = abi;
    }
}