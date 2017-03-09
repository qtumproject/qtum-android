package org.qtum.mromanovsky.qtum.ui.fragment.BackUpWalletFragment;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.widget.Toast;

import org.qtum.mromanovsky.qtum.ui.activity.MainActivity.MainActivity;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.WalletFragment.WalletFragment;

import static android.content.Context.CLIPBOARD_SERVICE;

class BackUpWalletFragmentPresenterImpl extends BaseFragmentPresenterImpl implements BackUpWalletFragmentPresenter {

    private BackUpWalletFragmentView mBackUpWalletFragmentView;
    private BackUpWalletInteractorImpl mBackUpWalletInteractor;

    BackUpWalletFragmentPresenterImpl(BackUpWalletFragmentView backUpWalletFragmentView) {
        mBackUpWalletFragmentView = backUpWalletFragmentView;
        mBackUpWalletInteractor = new BackUpWalletInteractorImpl(getView().getContext());
    }

    public BackUpWalletInteractorImpl getInteractor() {
        return mBackUpWalletInteractor;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        getView().setBrainCode(getInteractor().getSeed());
    }

    @Override
    public BackUpWalletFragmentView getView() {
        return mBackUpWalletFragmentView;
    }

    @Override
    public void onCopyBrainCodeClick() {
        ClipboardManager clipboard = (ClipboardManager) getView().getFragmentActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", getInteractor().getSeed());
        clipboard.setPrimaryClip(clip);
        //TODO : change notification type
        Toast.makeText(getView().getContext(),"Coped",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onContinueClick() {
        final WalletFragment walletFragment = WalletFragment.newInstance();
        ((MainActivity)getView().getFragmentActivity()).setRootFragment(walletFragment);
        getView().openRootFragment(walletFragment);
    }
}
