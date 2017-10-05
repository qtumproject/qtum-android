package org.qtum.wallet.ui.fragment.about_fragment;


import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class AboutPresenterImpl extends BaseFragmentPresenterImpl implements AboutPresenter{

    private AboutView mAboutFragmentView;
    private AboutInteractor mAboutInteractor;

    public AboutPresenterImpl(AboutView aboutFragmentView, AboutInteractor aboutInteractor){
        mAboutFragmentView = aboutFragmentView;
        mAboutInteractor = aboutInteractor;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

    }

    @Override
    public AboutView getView() {
        return mAboutFragmentView;
    }
}
