package com.pixelplex.qtum.ui.fragment.WatchContractFragment;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.EditText;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.utils.FontTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by max-v on 6/13/2017.
 */

public class WatchContractFragment extends BaseFragment implements WatchContractFragmentView {

    private static final String IS_TOKEN = "is_token";

    WatchContractFragmentPresenter mWatchContractFragmentPresenter;

    private boolean mIsToken;

    @BindView(R.id.et_contract_name)
    TextInputEditText mEditTextContractName;
    @BindView(R.id.et_contract_address)
    TextInputEditText mEditTextContractAddress;
    @BindView(R.id.et_json_interface)
    EditText mEditTextJsonInterface;
    @BindView(R.id.tv_toolbar_watch)
    FontTextView mTextViewToolbar;

    @OnClick({R.id.ibt_back,R.id.bt_ok,R.id.bt_cancel})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.bt_cancel:
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
            case R.id.bt_ok:
                String name = mEditTextContractName.getText().toString();
                String address = mEditTextContractAddress.getText().toString();
                String jsonInterface = mEditTextJsonInterface.getText().toString();
                getPresenter().onOkClick(name,address,jsonInterface, mIsToken);
                break;
        }
    }

    public static WatchContractFragment newInstance(boolean isToken) {
        
        Bundle args = new Bundle();
        args.putBoolean(IS_TOKEN,isToken);
        WatchContractFragment fragment = new WatchContractFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        mIsToken = getArguments().getBoolean(IS_TOKEN);
        if(mIsToken){
            mTextViewToolbar.setText("Watch Token");
        } else {
            mTextViewToolbar.setText("Watch Contract");
        }
    }

    @Override
    protected void createPresenter() {
        mWatchContractFragmentPresenter = new WatchContractFragmentPresenter(this);
    }

    @Override
    protected WatchContractFragmentPresenter getPresenter() {
        return mWatchContractFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_watch_contract;
    }

    @Override
    public void onResume() {
        hideBottomNavView(false);
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        showBottomNavView(false);
    }
}
