package org.qtum.mromanovsky.qtum.ui.fragment.ProfileFragment;

import org.qtum.mromanovsky.qtum.ui.activity.MainActivity.MainActivity;
import org.qtum.mromanovsky.qtum.ui.fragment.BackUpWalletFragment.BackUpWalletFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.PinFragment.PinFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.StartPageFragment.StartPageFragment;


public class ProfileFragmentPresenterImpl extends BaseFragmentPresenterImpl implements ProfileFragmentPresenter {

    private ProfileFragmentView mProfileFragmentView;
    private ProfileFragmentInteractorImpl mProfileFragmentInteractor;

    public ProfileFragmentPresenterImpl(ProfileFragmentView profileFragmentView) {
        mProfileFragmentView = profileFragmentView;
        mProfileFragmentInteractor = new ProfileFragmentInteractorImpl(getView().getContext());
    }

    @Override
    public ProfileFragmentView getView() {
        return mProfileFragmentView;
    }

    public ProfileFragmentInteractorImpl getInteractor() {
        return mProfileFragmentInteractor;
    }

    @Override
    public void changePin() {
        PinFragment pinFragment = PinFragment.newInstance(PinFragment.CHANGING);
        getView().openFragmentAndAddToBackStack(pinFragment);
    }

    @Override
    public void logOut() {
        getView().startDialogFragmentForResult();
    }



    @Override
    public void walletBackUp() {
        BackUpWalletFragment backUpWalletFragment = BackUpWalletFragment.newInstance();
        getView().openFragmentAndAddToBackStack(backUpWalletFragment);
    }

    @Override
    public void onLogOutYesClick() {
        getInteractor().clearSharedPreference();
        StartPageFragment startPageFragment = StartPageFragment.newInstance();
        ((MainActivity)getView().getFragmentActivity()).openFragment(startPageFragment,null);
    }

}
