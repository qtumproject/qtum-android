package org.qtum.mromanovsky.qtum.ui.fragment.ProfileFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

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

    @OnClick({R.id.ll_change_pin, R.id.ll_wallet_back_up, R.id.ll_log_out, R.id.ll_create_token, R.id.ll_subscribe_tokens})
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
    public void onClick() {
        getPresenter().onLogOutYesClick();
    }
}