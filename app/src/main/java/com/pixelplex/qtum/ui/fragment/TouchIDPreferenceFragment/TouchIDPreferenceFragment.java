package com.pixelplex.qtum.ui.fragment.TouchIDPreferenceFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.FragmentFactory.Factory;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.OnClick;


public abstract class TouchIDPreferenceFragment extends BaseFragment implements TouchIDPreferenceFragmentView {

    private static final String IS_IMPORTING = "is_importing";
    private static final String PIN = "pin";
    private boolean mIsImporting;

    @OnClick({R.id.bt_enable_touch_id, R.id.bt_not_now})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_enable_touch_id:
                getPresenter().onEnableTouchIdClick();
            case R.id.bt_not_now:
                getPresenter().onNextScreen(mIsImporting);
                break;
        }
    }

    public static BaseFragment newInstance(Context context, boolean isImporting, String pin) {
        
        Bundle args = new Bundle();
        args.putBoolean(IS_IMPORTING,isImporting);
        args.putString(PIN,pin);
        BaseFragment fragment = Factory.instantiateFragment(context, TouchIDPreferenceFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    private TouchIDPreferenceFragmentPresenter mTouchIDPreferenceFragmentPresenter;

    @Override
    public void initializeViews() {
        super.initializeViews();
        mIsImporting = getArguments().getBoolean(IS_IMPORTING);
    }

    @Override
    protected void createPresenter() {
        mTouchIDPreferenceFragmentPresenter = new TouchIDPreferenceFragmentPresenter(this);
    }

    @Override
    protected TouchIDPreferenceFragmentPresenter getPresenter() {
        return mTouchIDPreferenceFragmentPresenter;
    }

    @Override
    public String getPin() {
        return getArguments().getString(PIN);
    }
}