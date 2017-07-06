package com.pixelplex.qtum.ui.fragment.BackUpWalletFragment;


import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.utils.FontButton;
import com.pixelplex.qtum.utils.FontEditText;
import com.pixelplex.qtum.utils.FontTextView;

import butterknife.BindView;
import butterknife.OnClick;

public class BackUpWalletFragment extends BaseFragment implements BackUpWalletFragmentView {

    private BackUpWalletFragmentPresenterImpl mBackUpWalletFragmentPresenter;

    public static final String IS_WALLET_CREATING = "is_wallet_creating";

    @BindView(R.id.bt_copy)
    FontButton mButtonCopy;
    @BindView(R.id.bt_continue)
    FontButton mButtonContinue;
    @BindView(R.id.tv_toolbar_title)
    FontTextView mTextViewToolbarTitle;
    @BindView(R.id.tv_you_can_skip)
    FontTextView mTextViewYouCanSkip;
    @BindView(R.id.cl_back_up_wallet)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.tv_brain_code)
    FontTextView mTextViewBrainCode;
    @BindView(R.id.tv_copy_brain_code_to_use)
    FontTextView mTextViewCopyBrainCodeToUse;
    @BindView(R.id.bt_copy_brain_code)
    FontButton mButtonCopyBrainCode;


    @OnClick({R.id.bt_copy,R.id.bt_continue, R.id.ibt_back,R.id.bt_copy_brain_code})
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

    public static BackUpWalletFragment newInstance(boolean isWalletCreating) {
        BackUpWalletFragment backUpWalletFragment = new BackUpWalletFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_WALLET_CREATING,isWalletCreating);
        backUpWalletFragment.setArguments(args);
        return backUpWalletFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void createPresenter() {
        mBackUpWalletFragmentPresenter = new BackUpWalletFragmentPresenterImpl(this);
    }

    @Override
    protected BackUpWalletFragmentPresenterImpl getPresenter() {
        return mBackUpWalletFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_back_up_wallet;
    }

    @Override
    public void initializeViews() {
        if(getArguments().getBoolean(IS_WALLET_CREATING)){
            mTextViewToolbarTitle.setText(R.string.copy_brain_code);
            mTextViewYouCanSkip.setVisibility(View.VISIBLE);
            mTextViewCopyBrainCodeToUse.setVisibility(View.GONE);
            mButtonCopyBrainCode.setVisibility(View.GONE);
            mButtonContinue.setVisibility(View.VISIBLE);
            mButtonCopy.setVisibility(View.VISIBLE);
        }else {
            mTextViewToolbarTitle.setText(R.string.wallet_back_up);
            mTextViewYouCanSkip.setVisibility(View.GONE);
            mButtonCopyBrainCode.setVisibility(View.VISIBLE);
            mTextViewCopyBrainCodeToUse.setVisibility(View.VISIBLE);
            mButtonContinue.setVisibility(View.GONE);
            mButtonCopy.setVisibility(View.GONE);
        }
    }

    @Override
    public void setBrainCode(String seed) {
        mTextViewBrainCode.setText(seed);
    }

    @Override
    public void showToast() {
        Snackbar.make(mCoordinatorLayout, getString(R.string.coped), Snackbar.LENGTH_SHORT).show();
    }
}
