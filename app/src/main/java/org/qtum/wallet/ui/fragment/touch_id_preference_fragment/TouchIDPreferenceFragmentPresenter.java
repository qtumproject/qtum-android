package org.qtum.wallet.ui.fragment.touch_id_preference_fragment;


import org.qtum.wallet.datastorage.QtumSharedPreference;
import org.qtum.wallet.ui.fragment.backup_wallet_fragment.BackUpWalletFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.ui.fragment.wallet_main_fragment.WalletMainFragment;

class TouchIDPreferenceFragmentPresenter extends BaseFragmentPresenterImpl{

    private TouchIDPreferenceFragmentView mTouchIDPreferenceFragmentView;

    TouchIDPreferenceFragmentPresenter(TouchIDPreferenceFragmentView touchIDPreferenceFragmentView){
        mTouchIDPreferenceFragmentView = touchIDPreferenceFragmentView;
    }

    void onNextScreen(boolean isImporting){
        if(isImporting){
            WalletMainFragment walletFragment = WalletMainFragment.newInstance(getView().getContext());
            getView().getMainActivity().setRootFragment(walletFragment);
            getView().openRootFragment(walletFragment);
        }else{
            BaseFragment backUpWalletFragment = BackUpWalletFragment.newInstance(getView().getContext(), true, getView().getPin());
            getView().openFragment(backUpWalletFragment);
        }
    }

    void onEnableTouchIdClick(){
        QtumSharedPreference.getInstance().saveTouchIdEnable(getView().getContext(), true);
    }

    @Override
    public TouchIDPreferenceFragmentView getView() {
        return mTouchIDPreferenceFragmentView;
    }
}
