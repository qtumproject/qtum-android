package org.qtum.mromanovsky.qtum.ui.fragment.StartPageFragment;

import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;


public class StartPageFragmentPresenterImpl extends BaseFragmentPresenterImpl implements StartPageFragmentPresenter {

    StartPageFragmentView mStartPageFragmentView;

    public StartPageFragmentPresenterImpl(StartPageFragmentView startPageFragmentView){
        mStartPageFragmentView = startPageFragmentView;
    }
}
