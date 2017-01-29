package org.qtum.mromanovsky.qtum.ui.fragment.ProfileFragment;

import android.view.View;
import android.widget.LinearLayout;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;


public class ProfileFragment extends BaseFragment implements ProfileFragmentView {

    public static final int LAYOUT = R.layout.fragment_profile;

    ProfileFragmentPresenterImpl mProfileFragmentPresenter;

    @BindView(R.id.ll_change_pin)
    LinearLayout mLinearLayoutChangePin;
    @BindView(R.id.ll_wallet_back_up)
    LinearLayout mLinearLayoutWalletBackUp;
    @BindView(R.id.ll_log_out)
    LinearLayout mLinearLayoutLogOut;

    @OnClick({R.id.ll_change_pin, R.id.ll_wallet_back_up,R.id.ll_log_out})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_change_pin:
                getPresenter().changePin();
                break;
            case R.id.ll_wallet_back_up:
                getPresenter().walletBackUp();
                break;
            case R.id.ll_log_out:
                getPresenter().logOut();
                break;
        }
    }

    public static ProfileFragment newInstance() {
        ProfileFragment profileFragment = new ProfileFragment();
        return profileFragment;
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
        return LAYOUT;
    }

    @Override
    public void initializeViews() {

    }
}
