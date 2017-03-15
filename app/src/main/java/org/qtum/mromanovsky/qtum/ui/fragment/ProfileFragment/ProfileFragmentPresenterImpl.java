package org.qtum.mromanovsky.qtum.ui.fragment.ProfileFragment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import org.qtum.mromanovsky.qtum.dataprovider.UpdateService;
import org.qtum.mromanovsky.qtum.ui.activity.MainActivity.MainActivity;
import org.qtum.mromanovsky.qtum.ui.fragment.BackUpWalletFragment.BackUpWalletFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.CurrencyFragment.CurrencyFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.PinFragment.PinFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.SetTokenNameFragment.SetTokenNameFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.StartPageFragment.StartPageFragment;


class ProfileFragmentPresenterImpl extends BaseFragmentPresenterImpl implements ProfileFragmentPresenter {

    Intent mIntent;
    UpdateService mUpdateService;

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
        getView().openFragment(pinFragment);
    }

    @Override
    public void logOut() {
        getView().startDialogFragmentForResult();
    }



    @Override
    public void walletBackUp() {
        BackUpWalletFragment backUpWalletFragment = BackUpWalletFragment.newInstance(false);
        getView().openFragment(backUpWalletFragment);
    }

    @Override
    public void onLogOutYesClick() {
        mIntent = new Intent(getView().getContext(), UpdateService.class);

        //TODO : service
        //getView().getContext().bindService(mIntent,mServiceConnection,0);

        getInteractor().clearWallet();

        StartPageFragment startPageFragment = StartPageFragment.newInstance();
        ((MainActivity)getView().getFragmentActivity()).openRootFragment(startPageFragment);
        ((MainActivity)getView().getFragmentActivity()).getBottomNavigationView().getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onCreateTokenClick() {
        SetTokenNameFragment setTokenNameFragment = SetTokenNameFragment.newInstance();
        getView().openFragment(setTokenNameFragment);
    }

    @Override
    public void onSubscribeTokensClick() {
        CurrencyFragment currencyFragment = CurrencyFragment.newInstance(false);
        getView().openFragment(currencyFragment);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mUpdateService = ((UpdateService.UpdateBinder) iBinder).getService();
            mUpdateService.unsubscribe();
            getView().getContext().unbindService(mServiceConnection);
            mUpdateService.stopSelf();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

}
