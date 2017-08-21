package com.pixelplex.qtum.ui.fragment.BackUpWalletFragment;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.FragmentFactory.Factory;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.utils.FontButton;
import com.pixelplex.qtum.utils.FontTextView;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class BackUpWalletFragment extends BaseFragment implements BackUpWalletFragmentView {

    private BackUpWalletFragmentPresenterImpl mBackUpWalletFragmentPresenter;

    private static final String IS_WALLET_CREATING = "is_wallet_creating";
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
    @BindView(R.id.tv_you_can_skip)
    FontTextView youCanSkip;

    @OnClick(R.id.bt_share)
    public void onShareClick(){
        getPresenter().chooseShareMethod();
    }

    @OnClick({R.id.bt_copy,R.id.bt_continue, R.id.ibt_back, R.id.bt_copy_brain_code})
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

    public static BaseFragment newInstance(Context context, boolean isWalletCreating, String pin) {
        BaseFragment backUpWalletFragment = Factory.instantiateFragment(context, BackUpWalletFragment.class);
        Bundle args = new Bundle();
        args.putBoolean(IS_WALLET_CREATING,isWalletCreating);
        args.putString(PIN, pin);
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
    public void initializeViews() {
        if(getArguments().getBoolean(IS_WALLET_CREATING)){
            mTextViewToolbarTitle.setText(R.string.copy_brain_code);
            mTextViewCopyBrainCodeToUse.setVisibility(View.GONE);
            youCanSkip.setVisibility(View.VISIBLE);
            mButtonContinue.setVisibility(View.VISIBLE);
            mButtonCopy.setVisibility(View.VISIBLE);
            copyPassphare.setVisibility(View.GONE);
        }else {
            mTextViewToolbarTitle.setText(R.string.wallet_back_up);
            mTextViewCopyBrainCodeToUse.setVisibility(View.VISIBLE);
            youCanSkip.setVisibility(View.GONE);
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
    public String getBrainCode() {
        return mTextViewBrainCode.getText().toString();
    }


    @Override
    public void showToast() {
        Toast.makeText(getContext(),getString(R.string.coped),Toast.LENGTH_SHORT).show();
        //Snackbar.make(mCoordinatorLayout, getString(R.string.coped), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public String getPin() {
        return getArguments().getString(PIN);
    }
}
