package org.qtum.wallet.ui.fragment.backup_wallet_fragment;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;

import org.qtum.wallet.utils.crypto.AESUtil;
import org.qtum.wallet.utils.crypto.KeyStoreHelper;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.ui.fragment.wallet_main_fragment.WalletMainFragment;

import static android.content.Context.CLIPBOARD_SERVICE;

class BackUpWalletPresenterImpl extends BaseFragmentPresenterImpl implements BackUpWalletPresenter {

    private BackUpWalletView mBackUpWalletFragmentView;
    private BackUpWalletInteractor mBackUpWalletInteractor;
    private String passphrase;

    private final String QTUM_PIN_ALIAS = "qtum_alias";

    BackUpWalletPresenterImpl(BackUpWalletView backUpWalletFragmentView, BackUpWalletInteractor backUpWalletInteractor) {
        mBackUpWalletFragmentView = backUpWalletFragmentView;
        mBackUpWalletInteractor = backUpWalletInteractor;
    }

    private BackUpWalletInteractor getInteractor() {
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
    public BackUpWalletView getView() {
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
