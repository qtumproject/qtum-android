package org.qtum.mromanovsky.qtum.ui.fragment.ProfileFragment;

import org.qtum.mromanovsky.qtum.ui.fragment.BackUpWalletFragment.BackUpWalletFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.PinFragment.PinFragment;


public class ProfileFragmentPresenterImpl extends BaseFragmentPresenterImpl implements ProfileFragmentPresenter {

    ProfileFragmentView mProfileFragmentView;

    public ProfileFragmentPresenterImpl(ProfileFragmentView profileFragmentView) {
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

    @Override
    public void walletBackUp() {
        BackUpWalletFragment backUpWalletFragment = BackUpWalletFragment.newInstance();
        getView().openFragmentAndAddToBackStack(backUpWalletFragment);
    }

}
