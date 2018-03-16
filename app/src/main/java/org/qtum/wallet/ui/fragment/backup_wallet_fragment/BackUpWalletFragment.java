package org.qtum.wallet.ui.fragment.backup_wallet_fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.Toast;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.fragment.start_page_fragment.StartPageFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.utils.FontButton;
import org.qtum.wallet.utils.FontTextView;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Context.CLIPBOARD_SERVICE;

public abstract class BackUpWalletFragment extends BaseFragment implements BackUpWalletView {

    private BackUpWalletPresenter mBackUpWalletFragmentPresenter;

    protected static final String IS_WALLET_CREATING = "is_wallet_creating";
    private static final String PIN = "pin";

    @BindView(R.id.bt_copy)
    FontButton mButtonCopy;
    @BindView(R.id.bt_continue)
    FontButton mButtonContinue;
    @BindView(R.id.bt_copy_brain_code)
    FontButton copyPassphare;
    @BindView(R.id.tv_toolbar_title)
    FontTextView mTextViewToolbarTitle;
    @BindView(R.id.cl_back_up_wallet)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.tv_brain_code)
    FontTextView mTextViewBrainCode;
    @BindView(R.id.tv_copy_brain_code_to_use)
    FontTextView mTextViewCopyBrainCodeToUse;

    @OnClick(R.id.bt_share)
    public void onShareClick() {
        getPresenter().onShareClick();
    }

    @OnClick({R.id.bt_copy, R.id.bt_continue, R.id.ibt_back, R.id.bt_copy_brain_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_copy_brain_code:
            case R.id.bt_copy:
                getPresenter().onCopyBrainCodeClick();
                break;
            case R.id.bt_continue:
                getPresenter().onContinueClick();
                break;
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
        }
    }

    @Override
    public void onLogin() {
        getMainActivity().onLogin();
    }

    private void onBack() {
        if (getArguments().getBoolean(IS_WALLET_CREATING)) {
            openRootFragment(StartPageFragment.newInstance(getContext()));
        } else {
            getActivity().onBackPressed();
        }
    }

    public static BaseFragment newInstance(Context context, boolean isWalletCreating, String pin) {
        BaseFragment backUpWalletFragment = Factory.instantiateFragment(context, BackUpWalletFragment.class);
        Bundle args = new Bundle();
        args.putBoolean(IS_WALLET_CREATING, isWalletCreating);
        args.putString(PIN, pin);
        backUpWalletFragment.setArguments(args);
        return backUpWalletFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments().getBoolean(IS_WALLET_CREATING)) {
            hideBottomNavView(true);
        }
    }

    @Override
    protected void createPresenter() {
        mBackUpWalletFragmentPresenter = new BackUpWalletPresenterImpl(this, new BackUpWalletInteractorImpl(getContext()));
    }

    @Override
    protected BackUpWalletPresenter getPresenter() {
        return mBackUpWalletFragmentPresenter;
    }

    @Override
    public void initializeViews() {
        if (getArguments().getBoolean(IS_WALLET_CREATING)) {
            mTextViewToolbarTitle.setText(R.string.copy_brain_code);
            mTextViewCopyBrainCodeToUse.setVisibility(View.GONE);
            mButtonContinue.setVisibility(View.VISIBLE);
            mButtonCopy.setVisibility(View.VISIBLE);
            copyPassphare.setVisibility(View.GONE);
        } else {
            mTextViewToolbarTitle.setText(R.string.export_passphrase);
            mTextViewCopyBrainCodeToUse.setVisibility(View.VISIBLE);
            mButtonContinue.setVisibility(View.GONE);
            mButtonCopy.setVisibility(View.GONE);
            copyPassphare.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setBrainCode(String seed) {
        mTextViewBrainCode.setText(seed);
    }

    @Override
    public void showToast() {
        Toast.makeText(getContext(), getString(R.string.copied), Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getPin() {
        return getArguments().getString(PIN);
    }

    @Override
    public void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getMainActivity().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", text);
        clipboard.setPrimaryClip(clip);
    }

    @Override
    public void chooseShareMethod(String text) {
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        intentShareFile.setType("text/plain");
        intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                "Qtum Wallet Backup");
        intentShareFile.putExtra(Intent.EXTRA_TEXT, text);
        getMainActivity().startActivity(Intent.createChooser(intentShareFile, "Qtum Wallet Backup"));
    }
}
