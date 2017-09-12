package org.qtum.wallet.ui.fragment.about_fragment;


import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class AboutFragmentPresenter extends BaseFragmentPresenterImpl {

    private AboutFragmentView mAboutFragmentView;

    public AboutFragmentPresenter(AboutFragmentView aboutFragmentView){
        mAboutFragmentView = aboutFragmentView;
    }

    @Override
    public AboutFragmentView getView() {
        return mAboutFragmentView;
    }
}
