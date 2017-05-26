package com.pixelplex.qtum.ui.fragment.WalletMainFragment;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

/**
 * Created by kirillvolkov on 25.05.17.
 */

public class WalletMainFragment extends BaseFragment implements WalletMainFragmentView {

    public final int LAYOUT = R.layout.fragment_wallet_main;

    private WalletMainFragmentPresenterImpl mWalletMainFragmentPresenter;

    @Override
    protected void createPresenter() {
        mWalletMainFragmentPresenter = new WalletMainFragmentPresenterImpl(this);
    }

    @Override
    protected BaseFragmentPresenterImpl getPresenter() {
        return mWalletMainFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return LAYOUT;
    }
}
