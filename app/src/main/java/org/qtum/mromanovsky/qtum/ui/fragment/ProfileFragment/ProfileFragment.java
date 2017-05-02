package org.qtum.mromanovsky.qtum.ui.fragment.ProfileFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;


public class ProfileFragment extends BaseFragment implements ProfileFragmentView, LogOutDialogFragment.OnYesClickListener {

    ProfileFragmentPresenterImpl mProfileFragmentPresenter;

    @BindView(R.id.ll_change_pin)
    LinearLayout mLinearLayoutChangePin;
    @BindView(R.id.ll_wallet_back_up)
    LinearLayout mLinearLayoutWalletBackUp;
    @BindView(R.id.ll_log_out)
    LinearLayout mLinearLayoutLogOut;
    @BindView(R.id.ll_create_token)
    LinearLayout mLinearLayoutCreateToken;
    @BindView(R.id.tv_profile_language)
    TextView mTextViewLanguage;
    @BindView(R.id.tv_profile_change_pin)
    TextView mTextViewChangePin;
    @BindView(R.id.tv_profile_wallet_back_up)
    TextView mTextViewWalletBackUp;
    @BindView(R.id.tv_profile_log_out)
    TextView mTextViewLogOut;
    @BindView(R.id.tv_profile_create_token)
    TextView mTextViewCreateToken;
    @BindView(R.id.tv_profile_subscribe_tokens)
    TextView mTextViewSubscribeTokens;
    @BindView(R.id.tv_profile_about)
    TextView mTextViewAbout;

    @OnClick({R.id.ll_change_pin, R.id.ll_wallet_back_up, R.id.ll_log_out, R.id.ll_create_token, R.id.ll_subscribe_tokens, R.id.ll_language})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_change_pin:
                getPresenter().onChangePinClick();
                break;
            case R.id.ll_wallet_back_up:
                getPresenter().onWalletBackUpClick();
                break;
            case R.id.ll_log_out:
                getPresenter().onLogOutClick();
                break;
            case R.id.ll_create_token:
                getPresenter().onCreateTokenClick();
                break;
            case R.id.ll_subscribe_tokens:
                getPresenter().onSubscribeTokensClick();
                break;
            case R.id.ll_language:
                getPresenter().onLanguageClick();
        }
    }

    public static ProfileFragment newInstance() {

        Bundle args = new Bundle();

        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mProfileFragmentPresenter = new ProfileFragmentPresenterImpl(this);
    }

    @Override
    protected ProfileFragmentPresenterImpl getPresenter() {
        return mProfileFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_profile;
    }

    @Override
    public void initializeViews() {

    }

    @Override
    public void startDialogFragmentForResult() {
        LogOutDialogFragment logOutDialogFragment = new LogOutDialogFragment();
        logOutDialogFragment.setTargetFragment(this, 200);
        logOutDialogFragment.show(getFragmentManager(), LogOutDialogFragment.class.getCanonicalName());
    }

    @Override
    public void resetText() {
        mTextViewLanguage.setText(R.string.language);
        mTextViewChangePin.setText(R.string.change_pin);
        mTextViewWalletBackUp.setText(R.string.wallet_back_up);
        mTextViewLogOut.setText(R.string.log_out);
        mTextViewCreateToken.setText(R.string.create_token);
        mTextViewSubscribeTokens.setText(R.string.subscribe_tokens);
        mTextViewAbout.setText(R.string.about);
    }

    @Override
    public void onClick() {
        getPresenter().onLogOutYesClick();
    }
}