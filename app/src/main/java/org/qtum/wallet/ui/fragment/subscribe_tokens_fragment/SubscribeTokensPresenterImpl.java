package org.qtum.wallet.ui.fragment.subscribe_tokens_fragment;

import org.qtum.wallet.model.contract.ContractCreationStatus;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.ArrayList;
import java.util.List;

public class SubscribeTokensPresenterImpl extends BaseFragmentPresenterImpl implements AddressesListTokenPresenter {

    private SubscribeTokensView mSubscribeTokensFragmentView;
    private SubscribeTokensInteractor mSubscribeTokensInteractorImpl;

    public SubscribeTokensPresenterImpl(SubscribeTokensView subscribeTokensFragmentView, SubscribeTokensInteractor subscribeTokensInteractor) {
        mSubscribeTokensFragmentView = subscribeTokensFragmentView;
        mSubscribeTokensInteractorImpl = subscribeTokensInteractor;
    }

    @Override
    public SubscribeTokensView getView() {
        return mSubscribeTokensFragmentView;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        List<Token> confirmedTokens = new ArrayList<>();
        for (Token token : getInteractor().getTokenList()) {
            if (token.getCreationStatus().equals(ContractCreationStatus.Created)) {
                confirmedTokens.add(token);
            }
        }
        if (confirmedTokens.size() != 0) {
            getView().setTokenList(confirmedTokens);
        } else {
            getView().setPlaceHolder();
        }
    }

    @Override
    public void saveTokens(List<Token> tokens) {
        if (tokens != null) {
            getInteractor().saveTokenList(tokens);
        }
    }

    private SubscribeTokensInteractor getInteractor() {
        return mSubscribeTokensInteractorImpl;
    }
}
