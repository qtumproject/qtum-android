package com.pixelplex.qtum.ui.fragment.SubscribeTokensFragment;

import android.content.Context;

import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

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
