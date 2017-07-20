package com.pixelplex.qtum.ui.fragment.BackUpWalletFragment;


import android.content.ClipData;
import android.content.ClipboardManager;

import com.pixelplex.qtum.crypto.AESUtil;
import com.pixelplex.qtum.crypto.KeyStoreHelper;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.WalletMainFragment.WalletMainFragment;

import static android.content.Context.CLIPBOARD_SERVICE;

class BackUpWalletFragmentPresenterImpl extends BaseFragmentPresenterImpl implements BackUpWalletFragmentPresenter {

    private BackUpWalletFragmentView mBackUpWalletFragmentView;
    private BackUpWalletInteractorImpl mBackUpWalletInteractor;
    private String passphrase;

    private final String QTUM_PIN_ALIAS = "qtum_alias";

    BackUpWalletFragmentPresenterImpl(BackUpWalletFragmentView backUpWalletFragmentView) {
        mBackUpWalletFragmentView = backUpWalletFragmentView;
        mBackUpWalletInteractor = new BackUpWalletInteractorImpl(getView().getContext());
    }

    private BackUpWalletInteractorImpl getInteractor() {
        return mBackUpWalletInteractor;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        getView().getMainActivity().recolorStatusBarBlue();
        String pin = getView().getPin();
        String cryptoSaltPassphrase = getInteractor().getSeed();

        byte[] saltPassphrase = KeyStoreHelper.decryptToBytes(QTUM_PIN_ALIAS,cryptoSaltPassphrase);

        passphrase = AESUtil.decryptBytes(pin,saltPassphrase);
        getView().setBrainCode(passphrase);
    }

    @Override
    public BackUpWalletFragmentView getView() {
        return mBackUpWalletFragmentView;
    }

    @Override
    public void onCopyBrainCodeClick() {
        ClipboardManager clipboard = (ClipboardManager) getView().getMainActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", passphrase);
        clipboard.setPrimaryClip(clip);
        getView().showToast();
    }

    @Override
    public void onContinueClick() {
        final WalletMainFragment walletFragment = WalletMainFragment.newInstance();
        getView().getMainActivity().setRootFragment(walletFragment);
        getView().openRootFragment(walletFragment);
    }
}
