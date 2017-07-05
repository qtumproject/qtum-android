package com.pixelplex.qtum.ui.fragment.ProfileFragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;


import butterknife.BindView;


public class ProfileFragment extends BaseFragment implements ProfileFragmentView, LogOutDialogFragment.OnYesClickListener, OnSettingClickListener {

    private ProfileFragmentPresenterImpl mProfileFragmentPresenter;

    @BindView(R.id.pref_list)
    RecyclerView prefList;

    private PrefAdapter adapter;

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
        return R.layout.lyt_profile_preference;
    }

    @Override
    public void initializeViews() {
        prefList.setLayoutManager(new LinearLayoutManager(getContext()));
        prefList.addItemDecoration(new DividerItemDecoration(getContext(), R.drawable.color_primary_divider, R.drawable.section_setting_divider, mProfileFragmentPresenter.getSettingsData()));
        adapter = new PrefAdapter(mProfileFragmentPresenter.getSettingsData(), this);
        prefList.setAdapter(adapter);
    }

    @Override
    public void startDialogFragmentForResult() {
        LogOutDialogFragment logOutDialogFragment = new LogOutDialogFragment();
        logOutDialogFragment.setTargetFragment(this, 200);
        logOutDialogFragment.show(getFragmentManager(), LogOutDialogFragment.class.getCanonicalName());
    }

    @Override
    public void resetText() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick() {
        getPresenter().onLogOutYesClick();
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
        }
    }

    @Override
    public void onSwitchChange(int key, boolean isChecked) {
        switch (key){
            case R.string.touch_id:{
                getPresenter().onTouchIdSwitched(isChecked);
            }
        }
    }
}