package com.pixelplex.qtum.ui.fragment.MyContractsFragment;

import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentView;

/**
 * Created by max-v on 6/2/2017.
 */

public class MyContractsFragmentPresenter extends BaseFragmentPresenterImpl {

    MyContractsFragmentView mMyContractsFragmentView;

    MyContractsFragmentPresenter(MyContractsFragmentView myContractsFragmentView){
        mMyContractsFragmentView = myContractsFragmentView;
    }

    @Override
    public BaseFragmentView getView() {
        return mMyContractsFragmentView;
    }
}
