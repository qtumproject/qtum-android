package com.pixelplex.qtum.ui.fragment.ProfileFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.FragmentFactory.Factory;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import butterknife.BindView;

/**
 * Created by kirillvolkov on 05.07.17.
 */

public abstract class ProfileFragment extends BaseFragment implements ProfileFragmentView, LogOutDialogFragment.OnYesClickListener, OnSettingClickListener {

    public static BaseFragment newInstance(Context context) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, ProfileFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    protected ProfileFragmentPresenterImpl mProfileFragmentPresenter;

    protected DividerItemDecoration dividerItemDecoration;

    @BindView(R.id.pref_list)
    protected RecyclerView prefList;

    @Override
    protected void createPresenter() {
        mProfileFragmentPresenter = new ProfileFragmentPresenterImpl(this);
    }

    @Override
    protected ProfileFragmentPresenterImpl getPresenter() {
        return mProfileFragmentPresenter;
    }

    @Override
    public void onSettingClick(int key) {
        switch (key){
            case R.string.language:
                getPresenter().onLanguageClick();
                break;
            case R.string.change_pin:
                getPresenter().onChangePinClick();
                break;
            case R.string.wallet_back_up:
                getPresenter().onWalletBackUpClick();
                break;
            case R.string.smart_contracts:
                getPresenter().onSmartContractsClick();
                break;
            case R.string.subscribe_tokens:
                getPresenter().onSubscribeTokensClick();
                break;
            case R.string.about:
                // getPresenter().onAboutClick();
                break;
            case R.string.log_out:
                getPresenter().onLogOutClick();
                break;
            case R.string.switch_theme:
                getPresenter().switchTheme();
        }
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        prefList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onSwitchChange(int key, boolean isChecked) {
        switch (key){
            case R.string.touch_id:{
                getPresenter().onTouchIdSwitched(isChecked);
            }
        }
    }

    @Override
    public void onClick() {
        getPresenter().onLogOutYesClick();
    }

    @Override
    public void startDialogFragmentForResult() {
        LogOutDialogFragment logOutDialogFragment = new LogOutDialogFragment();
        logOutDialogFragment.setTargetFragment(this, 200);
        logOutDialogFragment.show(getFragmentManager(), LogOutDialogFragment.class.getCanonicalName());
    }
}
