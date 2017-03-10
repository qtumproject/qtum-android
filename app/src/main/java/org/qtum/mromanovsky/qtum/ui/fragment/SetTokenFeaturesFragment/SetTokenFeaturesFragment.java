package org.qtum.mromanovsky.qtum.ui.fragment.SetTokenFeaturesFragment;


import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

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

    private final int LAYOUT = R.layout.fragment_set_token_features;
    private SetTokenFeaturesFragmentPresenterImpl mSetTokenFeaturesFragmentPresenter;

    public static SetTokenFeaturesFragment newInstance(){
        SetTokenFeaturesFragment setTokenFeaturesFragment = new SetTokenFeaturesFragment();
        return setTokenFeaturesFragment;
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
        return LAYOUT;
    }
}
