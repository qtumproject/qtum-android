package com.pixelplex.qtum.ui.fragment.MyContractsFragment;

import android.os.Bundle;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

/**
 * Created by max-v on 6/2/2017.
 */

public class MyContractsFragment extends BaseFragment implements MyContractsFragmentView {

    MyContractsFragmentPresenter mMyContractsFragmentPresenter;

    public static MyContractsFragment newInstance() {

        Bundle args = new Bundle();

        MyContractsFragment fragment = new MyContractsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mMyContractsFragmentPresenter = new MyContractsFragmentPresenter(this);
    }

    @Override
    protected BaseFragmentPresenterImpl getPresenter() {
        return mMyContractsFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_my_contracts;
    }
}
