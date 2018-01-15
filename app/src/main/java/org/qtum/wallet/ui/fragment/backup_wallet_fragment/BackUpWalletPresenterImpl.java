package org.qtum.wallet.ui.fragment.backup_wallet_fragment;

import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.ui.fragment.confirm_passphrase_fragment.ConfirmPassphraseFragment;
import org.qtum.wallet.ui.fragment.wallet_main_fragment.WalletMainFragment;

public class BackUpWalletPresenterImpl extends BaseFragmentPresenterImpl implements BackUpWalletPresenter {

    private BackUpWalletView mBackUpWalletFragmentView;
    private BackUpWalletInteractor mBackUpWalletInteractor;
    private String passphrase;

    public BackUpWalletPresenterImpl(BackUpWalletView backUpWalletFragmentView, BackUpWalletInteractor backUpWalletInteractor) {
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
        passphrase = getInteractor().getPassphrase(pin);
        getView().setBrainCode(passphrase);
    }

    @Override
    public BackUpWalletView getView() {
        return mBackUpWalletFragmentView;
    }

    @Override
    public void onCopyBrainCodeClick() {
        getView().copyToClipboard(passphrase);
        getView().showToast();
    }

    @Override
    public void onShareClick() {
        getView().chooseShareMethod(passphrase);
    }

    @Override
    public void onContinueClick() {
        BaseFragment fragment = ConfirmPassphraseFragment.newInstance(getView().getContext(),passphrase);
        getView().openFragment(fragment);
    }
}
