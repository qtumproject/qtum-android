package org.qtum.mromanovsky.qtum.ui.fragment.BackUpWalletFragment;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class BackUpWalletFragment extends BaseFragment implements BackUpWalletFragmentView {

    public final int LAYOUT = R.layout.fragment_back_up_wallet;
    BackUpWalletFragmentPresenterImpl mBackUpWalletFragmentPresenter;

    public static final String IS_WALLET_CREATING = "is_wallet_creating";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
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

    @OnClick({R.id.bt_copy,R.id.bt_continue,R.id.bt_copy_brain_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_copy_brain_code:
            case R.id.bt_copy:
                getPresenter().onCopyBrainCodeClick();
                break;
            case R.id.bt_continue:
                getPresenter().onContinueClick();
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
        return LAYOUT;
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
            final AppCompatActivity activity = (AppCompatActivity) getActivity();
            if (null != mToolbar) {
                activity.setSupportActionBar(mToolbar);
                ActionBar actionBar = activity.getSupportActionBar();
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_back_indicator);
            }
        }
    }

    @Override
    public void setBrainCode(String seed) {
        mTextViewBrainCode.setText(seed);
    }
}
