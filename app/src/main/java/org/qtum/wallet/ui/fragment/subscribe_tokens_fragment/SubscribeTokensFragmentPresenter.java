package org.qtum.wallet.ui.fragment.subscribe_tokens_fragment;

import android.content.Context;

import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.ArrayList;
import java.util.List;


public class SubscribeTokensFragmentPresenter extends BaseFragmentPresenterImpl {

    private SubscribeTokensFragmentView mSubscribeTokensFragmentView;
    private SubscribeTokensFragmentInteractor mSubscribeTokensFragmentInteractor;

    SubscribeTokensFragmentPresenter(SubscribeTokensFragmentView subscribeTokensFragmentView) {
        mSubscribeTokensFragmentView = subscribeTokensFragmentView;
        mSubscribeTokensFragmentInteractor = new SubscribeTokensFragmentInteractor(getView().getContext());
    }

    @Override
    public SubscribeTokensFragmentView getView() {
        return mSubscribeTokensFragmentView;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        List<Token> confirmedTokens = new ArrayList<>();
        for (Token token : getInteractor().getTokenList()) {
            if (token.isHasBeenCreated()) confirmedTokens.add(token);
        }
        getView().setTokenList(confirmedTokens);
    }

    @Override
    public void onPause(Context context) {
        super.onPause(context);
        getInteractor().saveTokenList(getView().getTokenList());
    }

    private SubscribeTokensFragmentInteractor getInteractor() {
        return mSubscribeTokensFragmentInteractor;
    }
}
