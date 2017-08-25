package com.pixelplex.qtum.ui.fragment.backup_wallet_fragment;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;

import com.pixelplex.qtum.utils.crypto.AESUtil;
import com.pixelplex.qtum.utils.crypto.KeyStoreHelper;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.ui.fragment.wallet_main_fragment.WalletMainFragment;

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

    public void chooseShareMethod(){
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        intentShareFile.setType("text/plain");
        intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                "Qtum Wallet Backup");
        intentShareFile.putExtra(Intent.EXTRA_TEXT, getView().getBrainCode());
        getView().getMainActivity().startActivity(Intent.createChooser(intentShareFile, "Qtum Wallet Backup"));

    }

    @Override
    public void onContinueClick() {
        final WalletMainFragment walletFragment = WalletMainFragment.newInstance(getView().getContext());
        getView().getMainActivity().setRootFragment(walletFragment);
        getView().openRootFragment(walletFragment);
    }
}
