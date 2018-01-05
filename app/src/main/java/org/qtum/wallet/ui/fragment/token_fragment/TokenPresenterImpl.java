package org.qtum.wallet.ui.fragment.token_fragment;

import org.qtum.wallet.datastorage.TokenHistoryList;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.model.gson.token_history.TokenHistoryResponse;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.internal.util.SubscriptionList;
import rx.schedulers.Schedulers;

public class TokenPresenterImpl extends BaseFragmentPresenterImpl implements TokenPresenter {

    private TokenView view;
    private TokenInteractor interactor;
    private Token token;
    private String abi;
    private SubscriptionList mSubscriptionList = new SubscriptionList();

    private final int ONE_PAGE_COUNT = 25;

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
        loadAndUpdateData();
        getView().updateHistory(getInteractor().getHistoryList());
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

    @Override
    public void onLastItem(final int currentItemCount) {
        if (getInteractor().getHistoryList().size() != getInteractor().getTotalHistoryItem()) {
            //getView().loadNewHistory();
            mSubscriptionList.add(getInteractor().getHistoryList(token.getContractAddress(), ONE_PAGE_COUNT, currentItemCount)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<TokenHistoryResponse>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(TokenHistoryResponse historyResponse) {

                            TokenHistoryList.newInstance().getTokenHistories().addAll(historyResponse.getItems());
                            //getView().addHistory(currentItemCount, getInteractor().getHistoryList().size() - currentItemCount + 1,
                            //        getInteractor().getHistoryList());
                        }
                    }));

        }
    }

    private void loadAndUpdateData() {
        //getView().startRefreshAnimation();
        mSubscriptionList.add(getInteractor().getHistoryList(token.getContractAddress(), ONE_PAGE_COUNT, 0).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TokenHistoryResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().setAlertDialog(org.qtum.wallet.R.string.no_internet_connection,
                                org.qtum.wallet.R.string.please_check_your_network_settings,
                                org.qtum.wallet.R.string.ok,
                                BaseFragment.PopUpType.error);
                        //getView().stopRefreshRecyclerAnimation();
                    }

                    @Override
                    public void onNext(TokenHistoryResponse historyResponse) {
                        TokenHistoryList.newInstance().setTokenHistories(historyResponse.getItems());
                        TokenHistoryList.newInstance().setTotalItems(historyResponse.getCount());
                        getView().updateHistory(getInteractor().getHistoryList());
                    }
                }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSubscriptionList != null) {
            mSubscriptionList.clear();
        }
    }
}