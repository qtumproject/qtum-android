package org.qtum.mromanovsky.qtum.ui.fragment.WalletNameFragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
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

public class WalletNameFragment extends BaseFragment implements WalletNameFragmentView {

    public static final int  LAYOUT = R.layout.fragment_wallet_name;
    public static final String TAG = "WalletNameFragment";

    WalletNameFragmentPresenterImpl mCreateWalletFragmentPresenter;

    @BindView(R.id.bt_confirm)
    Button mButtonConfirm;
    @BindView(R.id.bt_cancel)
    Button mButtonCancel;
    @BindView(R.id.et_wallet_name)
    TextInputEditText mTextInputEditTextWalletName;



    @OnClick({R.id.bt_confirm,R.id.bt_cancel})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.bt_confirm:
                mCreateWalletFragmentPresenter.confirm(mTextInputEditTextWalletName.getText().toString());

                break;
            case R.id.bt_cancel:
                //TODO : stub
                break;
        }
    }

    public static WalletNameFragment newInstance(){
        WalletNameFragment walletNameFragment = new WalletNameFragment();
        return walletNameFragment;
    }

    @Override
    protected void createPresenter() {
        mCreateWalletFragmentPresenter = new WalletNameFragmentPresenterImpl(this);
    }

    @Override
    protected BaseFragmentPresenterImpl getPresenter() {
        return mCreateWalletFragmentPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(LAYOUT,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (null != toolbar) {
            activity.setSupportActionBar(toolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            actionBar.setTitle(R.string.wallet_name);
            //actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public void confirm() {
        PinFragment pinFragment = PinFragment.newInstance(true);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,pinFragment,pinFragment.getClass().getCanonicalName())
                .commit();
    }
}
