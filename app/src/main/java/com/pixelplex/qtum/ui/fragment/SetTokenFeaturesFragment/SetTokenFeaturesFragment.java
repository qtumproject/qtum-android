package com.pixelplex.qtum.ui.fragment.SetTokenFeaturesFragment;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class SetTokenFeaturesFragment extends BaseFragment implements SetTokenFeaturesFragmentView{

    @BindView(R.id.bt_back)
    Button mButtonBack;
    @BindView(R.id.bt_next)
    Button mButtonNext;

    @BindView(R.id.switch_automatic_selling_and_buying)
    Switch mSwitchAutomaticSellingAndBuying;
    @BindView(R.id.switch_freezing_of_assets)
    Switch mSwitchFreezingOfAssets;
    @BindView(R.id.switch_autorefill)
    Switch mSwitchAutorefill;
    @BindView(R.id.switch_proof_of_work)
    Switch mSwitchProofOfWork;

    @OnClick({R.id.bt_back,R.id.bt_next})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_back:
                getPresenter().onBackClick();
                break;
            case R.id.bt_next:
                getPresenter().onNextClick(mSwitchAutomaticSellingAndBuying.isChecked(),mSwitchFreezingOfAssets.isChecked(),
                        mSwitchAutorefill.isChecked(),mSwitchProofOfWork.isChecked());
                break;
        }
    }

    private SetTokenFeaturesFragmentPresenterImpl mSetTokenFeaturesFragmentPresenter;

    public static SetTokenFeaturesFragment newInstance() {

        Bundle args = new Bundle();

        SetTokenFeaturesFragment fragment = new SetTokenFeaturesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mSetTokenFeaturesFragmentPresenter = new SetTokenFeaturesFragmentPresenterImpl(this);
    }

    @Override
    protected SetTokenFeaturesFragmentPresenterImpl getPresenter() {
        return mSetTokenFeaturesFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_set_token_features;
    }

    @Override
    public void setData(boolean freezingOfAssets, boolean automaticSellingAndBuying, boolean autorefill, boolean proofOfWork) {
        mSwitchFreezingOfAssets.setChecked(freezingOfAssets);
        mSwitchAutomaticSellingAndBuying.setChecked(automaticSellingAndBuying);
        mSwitchAutorefill.setChecked(autorefill);
        mSwitchProofOfWork.setChecked(proofOfWork);
    }
}
