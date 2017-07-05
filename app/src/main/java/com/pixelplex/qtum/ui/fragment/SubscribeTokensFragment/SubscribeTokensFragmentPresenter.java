package com.pixelplex.qtum.ui.fragment.SubscribeTokensFragment;

import android.content.Context;

import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;


public class SubscribeTokensFragmentPresenter extends BaseFragmentPresenterImpl {

    private SubscribeTokensFragmentView mSubscribeTokensFragmentView;
    private SubscribeTokensFragmentInteractor mSubscribeTokensFragmentInteractor;

    SubscribeTokensFragmentPresenter(SubscribeTokensFragmentView subscribeTokensFragmentView){
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
        getView().setTokenList(getInteractor().getTokenList());
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
