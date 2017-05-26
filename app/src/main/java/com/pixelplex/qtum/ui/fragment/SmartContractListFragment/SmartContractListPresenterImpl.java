package com.pixelplex.qtum.ui.fragment.SmartContractListFragment;

import android.content.Context;

import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.SmartContractConstructorFragment.SmartContractConstructorFragment;

/**
 * Created by kirillvolkov on 25.05.17.
 */

public class SmartContractListPresenterImpl extends BaseFragmentPresenterImpl implements SmartContractListPresenter{

    private final SmartContractListView view;
    private final Context mContext;
    private final SmartContractListInteractorImpl interactor;

    SmartContractListPresenterImpl(SmartContractListView view) {
        this.view = view;
        mContext = getView().getContext();
        interactor = new SmartContractListInteractorImpl();
    }

    @Override
    public SmartContractListView getView() {
        return view;
    }

    public void openConstructorByName(String constructorName) {
        SmartContractConstructorFragment fragment = SmartContractConstructorFragment.newInstance(constructorName);
        getView().openFragment(fragment);
    }
}
