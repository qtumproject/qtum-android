package org.qtum.mromanovsky.qtum.ui.fragment.BalanceFragment;


import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;

public class BalanceFragmentPresenterImpl extends BaseFragmentPresenterImpl implements BalanceFragmentPresenter{

    private BalanceFragmentView mBalanceFragmentView;

    public BalanceFragmentPresenterImpl(BalanceFragmentView balanceFragmentView){
        mBalanceFragmentView = balanceFragmentView;
    }
}
