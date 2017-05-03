package org.qtum.mromanovsky.qtum.ui.fragment.BackUpWalletFragment;


import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class BackUpWalletFragment extends BaseFragment implements BackUpWalletFragmentView {

    private BackUpWalletFragmentPresenterImpl mBackUpWalletFragmentPresenter;

    public static final String IS_WALLET_CREATING = "is_wallet_creating";

    @BindView(R.id.tv_brain_code)
    TextView mTextViewBrainCode;
    @BindView(R.id.bt_copy)
    Button mButtonCopy;
    @BindView(R.id.bt_continue)
    Button mButtonContinue;
    @BindView(R.id.tv_toolbar_title)
    TextView mTextViewToolbarTitle;
    @BindView(R.id.tv_you_can_skip)
    TextView mTextViewYouCanSkip;
    @BindView(R.id.bt_copy_brain_code)
    Button mButtonCopyBrainCode;
    @BindView(R.id.cl_back_up_wallet)
    CoordinatorLayout mCoordinatorLayout;

    @OnClick({R.id.bt_copy,R.id.bt_continue,R.id.bt_copy_brain_code,R.id.ibt_back})
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

            mButtonContinue.setVisibility(View.VISIBLE);
            mButtonCopy.setVisibility(View.VISIBLE);
            mButtonCopyBrainCode.setVisibility(View.GONE);
        }else {
            mTextViewToolbarTitle.setText(R.string.wallet_back_up);
            mTextViewYouCanSkip.setVisibility(View.GONE);

            mButtonContinue.setVisibility(View.GONE);
            mButtonCopy.setVisibility(View.GONE);
            mButtonCopyBrainCode.setVisibility(View.VISIBLE);
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
