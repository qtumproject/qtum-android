package org.qtum.mromanovsky.qtum.ui.fragment.ProfileFragment;

import android.content.Context;

import org.qtum.mromanovsky.qtum.ui.activity.MainActivity.MainActivity;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.PinFragment.PinFragment;


public class ProfileFragmentPresenterImpl extends BaseFragmentPresenterImpl implements ProfileFragmentPresenter {

    ProfileFragmentView mProfileFragmentView;

    public ProfileFragmentPresenterImpl(ProfileFragmentView profileFragmentView){
        mProfileFragmentView = profileFragmentView;
    }

    @Override
    public ProfileFragmentView getView() {
        return mProfileFragmentView;
    }

    @Override
    public void changePin() {
        PinFragment pinFragment = PinFragment.newInstance(PinFragment.CHANGING);
        getView().openFragmentAndAddToBackStack(pinFragment);
    }

}
