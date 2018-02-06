package org.qtum.wallet.ui.fragment.watch_token_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;

import org.qtum.wallet.dataprovider.services.update_service.UpdateService;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.utils.FontButton;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class WatchTokenFragment extends BaseFragment implements WatchTokenView {

    private WatchTokenPresenter mWatchContractFragmentPresenter;

    @BindView(org.qtum.wallet.R.id.et_contract_name)
    protected TextInputEditText mEditTextContractName;

    @BindView(org.qtum.wallet.R.id.et_contract_address)
    protected TextInputEditText mEditTextContractAddress;

    @BindView(org.qtum.wallet.R.id.bt_ok)
    FontButton mButtonConfirm;

    private boolean isEmptyContractName = true;
    private boolean isEmptyContractAddress = true;
    private UpdateService mUpdateService;

    public static BaseFragment newInstance(Context context) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, WatchTokenFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        mEditTextContractAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                isEmptyContractAddress = editable.toString().isEmpty();
                checkForNoEmpty(isEmptyContractAddress, isEmptyContractName);
                if(editable.toString().length()==40){
                    getPresenter().onContractAddressEntered(editable.toString());
                }
            }
        });

        mEditTextContractName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                isEmptyContractName = editable.toString().isEmpty();
                checkForNoEmpty(isEmptyContractAddress, isEmptyContractName);
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUpdateService = getMainActivity().getUpdateService();
    }

    @Override
    protected void createPresenter() {
        mWatchContractFragmentPresenter = new WatchTokenPresenterImpl(this, new WatchTokenInteractorImpl(getContext()));
    }

    @Override
    protected WatchTokenPresenter getPresenter() {
        return mWatchContractFragmentPresenter;
    }

    @Override
    public void onResume() {
        //hideBottomNavView(false);
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //showBottomNavView(false);
    }

    @OnClick({org.qtum.wallet.R.id.ibt_back, org.qtum.wallet.R.id.bt_ok, org.qtum.wallet.R.id.bt_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case org.qtum.wallet.R.id.bt_cancel:
            case org.qtum.wallet.R.id.ibt_back:
                getActivity().onBackPressed();
                break;
            case org.qtum.wallet.R.id.bt_ok:
                String name = mEditTextContractName.getText().toString();
                String address = mEditTextContractAddress.getText().toString();
                getPresenter().onOkClick(name, address);
                break;
        }
    }

    private void checkForNoEmpty(boolean... isEmptyParams) {
        for (boolean isEmpty : isEmptyParams) {
            if (isEmpty) {
                mButtonConfirm.setEnabled(false);
                return;
            }
        }
        mButtonConfirm.setEnabled(true);
    }

    @Override
    public void subscribeTokenBalanceChanges(String contractAddress) {
        mUpdateService.subscribeTokenBalanceChange(contractAddress);
    }

    @Override
    public AlertDialogCallBack getAlertCallback() {
        return new BaseFragment.AlertDialogCallBack() {
            @Override
            public void onButtonClick() {
                FragmentManager fm = getFragmentManager();
                int count = fm.getBackStackEntryCount() - 2;
                for (int i = 0; i < count; ++i) {
                    fm.popBackStack();
                }
            }

            @Override
            public void onButton2Click() {
            }
        };
    }

    @Override
    public void setSoftMode() {
        super.setSoftMode();
        //getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void setContractName(String contractName) {
        if(mEditTextContractName.getText().toString().isEmpty()){
            mEditTextContractName.setText(contractName);
            mEditTextContractName.setSelection(contractName.length());
        }
    }
}