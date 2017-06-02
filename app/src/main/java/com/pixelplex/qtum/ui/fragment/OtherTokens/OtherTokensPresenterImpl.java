package com.pixelplex.qtum.ui.fragment.OtherTokens;

import android.content.Context;

import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

/**
 * Created by kirillvolkov on 01.06.17.
 */

public class OtherTokensPresenterImpl extends BaseFragmentPresenterImpl implements OtherTokensPresenter {

    private Context mContext;
    OtherTokensView view;
    OtherTokensInteractorImpl interactor;

    public OtherTokensPresenterImpl (OtherTokensView view) {
        this.view = view;
        mContext = getView().getContext();
        this.interactor = new OtherTokensInteractorImpl();
    }

    @Override
    public OtherTokensView getView() {
        return view;
    }
}
