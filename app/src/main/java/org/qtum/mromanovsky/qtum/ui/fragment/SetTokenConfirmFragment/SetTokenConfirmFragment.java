package org.qtum.mromanovsky.qtum.ui.fragment.SetTokenConfirmFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.qtum.mromanovsky.qtum.R;
import org.qtum.mromanovsky.qtum.datastorage.QtumToken;
import org.qtum.mromanovsky.qtum.ui.fragment.BaseFragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class SetTokenConfirmFragment extends BaseFragment implements SetTokenConfirmFragmentView{

    @BindView(R.id.bt_back)
    Button mButtonBack;
    @BindView(R.id.bt_confirm)
    Button mButtonFinish;

    @BindView(R.id.tv_token_name)
    TextView mTextViewTokenName;
    @BindView(R.id.tv_token_symbol)
    TextView mTextViewTokenSymbol;
    @BindView(R.id.tv_initial_supply)
    TextView mTextViewInitialSupply;
    @BindView(R.id.tv_decimal_units)
    TextView mTextViewDecimalUnits;
    @BindView(R.id.tv_freezing_of_assets)
    TextView mTextViewFreezingOfAssets;
    @BindView(R.id.tv_automatic_selling_and_buying)
    TextView mTextViewAutomaticSellingAndBuying;
    @BindView(R.id.tv_autorefill)
    TextView mTextViewAutorefill;
    @BindView(R.id.tv_proof_of_work)
    TextView mTextViewProofOfWork;

    @OnClick({R.id.bt_back,R.id.bt_confirm})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_back:
                getPresenter().onBackClick();
                break;
            case R.id.bt_confirm:
                getPresenter().onConfirmClick();
                break;
        }
    }

    private SetTokenConfirmFragmentPresenterImpl mSetTokenConfirmFragmentPresenter;

    public static SetTokenConfirmFragment newInstance() {

        Bundle args = new Bundle();

        SetTokenConfirmFragment fragment = new SetTokenConfirmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        mTextViewTokenName.setText(QtumToken.getInstance().getTokenName());
        mTextViewTokenSymbol.setText(QtumToken.getInstance().getTokenSymbol());
        mTextViewInitialSupply.setText(String.valueOf(QtumToken.getInstance().getInitialSupply()));
        mTextViewDecimalUnits.setText(String.valueOf(QtumToken.getInstance().getDecimalUnits()));
        mTextViewFreezingOfAssets.setText(QtumToken.getInstance().isFreezingOfAssets() ? "Yes" : "No");
        mTextViewAutomaticSellingAndBuying.setText(QtumToken.getInstance().isAutomaticSellingAndBuying() ? "Yes" : "No");
        mTextViewAutorefill.setText(QtumToken.getInstance().isAutorefill() ? "Yes" : "No");
        mTextViewProofOfWork.setText(QtumToken.getInstance().isProofOfWork() ? "Yes" : "No");
    }

    @Override
    protected void createPresenter() {
        mSetTokenConfirmFragmentPresenter = new SetTokenConfirmFragmentPresenterImpl(this);
    }

    @Override
    protected SetTokenConfirmFragmentPresenterImpl getPresenter() {
        return mSetTokenConfirmFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_set_token_confirm;
    }
}