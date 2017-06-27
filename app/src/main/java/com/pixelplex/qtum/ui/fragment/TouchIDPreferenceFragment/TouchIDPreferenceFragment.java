package com.pixelplex.qtum.ui.fragment.TouchIDPreferenceFragment;

import android.os.Bundle;
import android.view.View;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.OnClick;


public class TouchIDPreferenceFragment extends BaseFragment implements TouchIDPreferenceFragmentView {

    private static final String IS_IMPORTING = "is_importing";
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

    public static TouchIDPreferenceFragment newInstance(boolean isImporting) {
        
        Bundle args = new Bundle();
        args.putBoolean(IS_IMPORTING,isImporting);
        TouchIDPreferenceFragment fragment = new TouchIDPreferenceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    TouchIDPreferenceFragmentPresenter mTouchIDPreferenceFragmentPresenter;

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
    protected int getLayout() {
        return R.layout.fragment_touch_id_preference;
    }
}