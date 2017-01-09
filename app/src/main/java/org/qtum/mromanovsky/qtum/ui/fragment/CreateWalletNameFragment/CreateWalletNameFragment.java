package org.qtum.mromanovsky.qtum.ui.fragment.CreateWalletNameFragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import org.qtum.mromanovsky.qtum.ui.fragment.PinFragment.PinFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class CreateWalletNameFragment extends BaseFragment implements CreateWalletNameFragmentView {

    public static final int  LAYOUT = R.layout.fragment_create_wallet_name;
    public static final String TAG = "CreateWalletNameFragment";

    CreateWalletNameFragmentPresenterImpl mCreateWalletFragmentPresenter;

    @BindView(R.id.bt_confirm) Button mButtonConfirm;
    @BindView(R.id.bt_cancel) Button mButtonCancel;
    @BindView(R.id.et_wallet_name) TextInputEditText mTextInputEditTextWalletName;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @OnClick({R.id.bt_confirm,R.id.bt_cancel})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.bt_confirm:
                getPresenter().confirm(mTextInputEditTextWalletName.getText().toString());
                break;
            case R.id.bt_cancel:
                getPresenter().cancel();
                break;
        }
    }

    public static CreateWalletNameFragment newInstance(){
        CreateWalletNameFragment createWalletNameFragment = new CreateWalletNameFragment();
        return createWalletNameFragment;
    }

    @Override
    protected void createPresenter() {
        mCreateWalletFragmentPresenter = new CreateWalletNameFragmentPresenterImpl(this);
    }

    @Override
    protected CreateWalletNameFragmentPresenterImpl getPresenter() {
        return mCreateWalletFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return LAYOUT;
    }

    @Override
    public void initializeViews() {
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (null != mToolbar) {
            activity.setSupportActionBar(mToolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }
}
