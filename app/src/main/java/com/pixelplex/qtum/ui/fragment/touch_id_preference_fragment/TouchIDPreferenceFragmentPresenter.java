package com.pixelplex.qtum.ui.fragment.touch_id_preference_fragment;


import com.pixelplex.qtum.datastorage.QtumSharedPreference;
import com.pixelplex.qtum.ui.fragment.backup_wallet_fragment.BackUpWalletFragment;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragment;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.wallet_main_fragment.WalletMainFragment;

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
