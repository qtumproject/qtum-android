package org.qtum.mromanovsky.qtum.ui.fragment.ProfileFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;


public class ProfileFragment extends BaseFragment implements ProfileFragmentView {

    public static final int  LAYOUT = R.layout.fragment_profile;

    ProfileFragmentPresenterImpl mProfileFragmentPresenter;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.ll_change_pin) LinearLayout mLinearLayoutChangePin;

    @OnClick({R.id.ll_change_pin})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_change_pin:
                getPresenter().changePin();
        }
    }

    public static ProfileFragment newInstance(){
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
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (null != mToolbar) {
            activity.setSupportActionBar(mToolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }
}
