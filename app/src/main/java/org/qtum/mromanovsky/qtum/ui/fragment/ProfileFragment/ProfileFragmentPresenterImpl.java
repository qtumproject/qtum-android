package org.qtum.mromanovsky.qtum.ui.fragment.ProfileFragment;

import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;


public class ProfileFragmentPresenterImpl extends BaseFragmentPresenterImpl implements ProfileFragmentPresenter {

    ProfileFragmentView mProfileFragmentView;

    public ProfileFragmentPresenterImpl(ProfileFragmentView profileFragmentView){
        mProfileFragmentView = profileFragmentView;
    }

    @Override
    public ProfileFragmentView getView() {
        return mProfileFragmentView;
    }
}
