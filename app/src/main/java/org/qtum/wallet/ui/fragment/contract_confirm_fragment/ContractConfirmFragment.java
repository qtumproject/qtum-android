package org.qtum.wallet.ui.fragment.contract_confirm_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import org.qtum.wallet.QtumApplication;
import org.qtum.wallet.R;
import org.qtum.wallet.model.contract.ContractMethodParameter;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.utils.FontButton;
import org.qtum.wallet.utils.FontTextView;
import org.qtum.wallet.utils.ResizeHeightAnimation;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public abstract class ContractConfirmFragment extends BaseFragment implements  ContractConfirmView, OnValueClick{

    protected static final String paramsKey = "params";
    private static final String CONTRACT_TEMPLATE_UIID = "mUiid";
    private static final String CONTRACT_NAME = "name";

    @BindView(R.id.et_fee)
    protected TextInputEditText mTextInputEditTextFee;
    @BindView(R.id.til_fee)
    protected TextInputLayout tilFee;
    @BindView(R.id.seekBar)
    SeekBar mSeekBarFee;
    @BindView(R.id.seekBar_gas_limit)
    SeekBar mSeekBarGasLimit;
    @BindView(R.id.seekBar_gas_price)
    SeekBar mSeekBarGasPrice;
    @BindView(R.id.tv_max_fee)
    FontTextView mFontTextViewMaxFee;
    @BindView(R.id.tv_min_fee)
    FontTextView mFontTextViewMinFee;
    @BindView(R.id.tv_max_gas_price)
    FontTextView mFontTextViewMaxGasPrice;
    @BindView(R.id.tv_min_gas_price)
    FontTextView mFontTextViewMinGasPrice;

    @BindView(R.id.tv_max_gas_limit)
    FontTextView mFontTextViewMaxGasLimit;
    @BindView(R.id.tv_min_gas_limit)
    FontTextView mFontTextViewMinGasLimit;

    @BindView(R.id.tv_gas_price)
    FontTextView mFontTextViewGasPrice;

    @BindView(R.id.tv_gas_limit)
    FontTextView mFontTextViewGasLimit;

    @BindView(R.id.bt_edit_close)
    FontButton mFontButtonEditClose;
    @BindView(R.id.seek_bar_container)
    LinearLayout mLinearLayoutSeekBarContainer;

    int mMinFee;
    int mMaxFee;
    int stepFee = 100;

    int mMinGasPrice;
    int mMaxGasPrice;
    int stepGasPrice = 5;

    boolean seekBarChangeValue = false;
    boolean textViewChangeValue = false;

    int mMinGasLimit;
    int mMaxGasLimit;
    int stepGasLimit = 100000;

    private int appLogoHeight = 0;
    private ResizeHeightAnimation mAnimForward;
    private ResizeHeightAnimation mAnimBackward;
    boolean showing = false;

    public static BaseFragment newInstance(Context context, List<ContractMethodParameter> params, String uiid, String name) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, ContractConfirmFragment.class);
        args.putSerializable(paramsKey,(ArrayList)params);
        args.putString(CONTRACT_TEMPLATE_UIID, uiid);
        args.putString(CONTRACT_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    protected ContractConfirmPresenterImpl presenter;
    protected ContractConfirmAdapter adapter;

    @BindView(R.id.recycler_view)
    protected
    RecyclerView confirmList;

    @OnClick(R.id.bt_edit_close)
    public void onEditCloseClick(){
        if(showing) {
            mLinearLayoutSeekBarContainer.startAnimation(mAnimBackward);
            mFontButtonEditClose.setText(R.string.edit);
            showing = !showing;
        } else {
            mLinearLayoutSeekBarContainer.startAnimation(mAnimForward);
            mFontButtonEditClose.setText(R.string.close);
            showing = !showing;
        }
    }

    private void initializeAnim(){
        mAnimForward = new ResizeHeightAnimation(mLinearLayoutSeekBarContainer, 0, appLogoHeight);
        mAnimForward.setDuration(300);
        mAnimForward.setFillEnabled(true);
        mAnimForward.setFillAfter(true);
        //mAnimForward.setAnimationListener(this);

        mAnimBackward = new ResizeHeightAnimation(mLinearLayoutSeekBarContainer, appLogoHeight, 0);
        mAnimBackward.setDuration(300);
        mAnimBackward.setFillEnabled(true);
        mAnimBackward.setFillAfter(true);
        //mAnimBackward.setAnimationListener(this);
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        mSeekBarFee.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(textViewChangeValue){
                    textViewChangeValue=false;
                    return;
                }
                double value = (mMinFee + (progress * stepFee)) / 100000000.;
                mTextInputEditTextFee.setText(new DecimalFormat("#.########").format(value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekBarGasPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = (mMinGasPrice + (progress * stepGasPrice));
                mFontTextViewGasPrice.setText(String.valueOf(value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekBarGasLimit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = (mMinGasLimit + (progress * stepGasLimit));
                mFontTextViewGasLimit.setText(String.valueOf(value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if(mLinearLayoutSeekBarContainer.getHeight()==0) {
            mLinearLayoutSeekBarContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mLinearLayoutSeekBarContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    appLogoHeight = (appLogoHeight == 0) ?  mLinearLayoutSeekBarContainer.getHeight() : appLogoHeight;
                    initializeAnim();
                    mLinearLayoutSeekBarContainer.getLayoutParams().height = 0;
                    mLinearLayoutSeekBarContainer.requestLayout();
                }
            });
        } else {
            appLogoHeight = (appLogoHeight == 0) ?  mLinearLayoutSeekBarContainer.getHeight() : appLogoHeight;
            initializeAnim();
            mLinearLayoutSeekBarContainer.getLayoutParams().height = 0;
            mLinearLayoutSeekBarContainer.requestLayout();
        }

        mTextInputEditTextFee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(seekBarChangeValue){
                    seekBarChangeValue = false;
                    return;
                }
                if(!s.toString().isEmpty()) {
                    Double fee = Double.valueOf(s.toString()) * 100000000;
                    textViewChangeValue = true;
                    int progress;
                    if (fee < mMinFee) {
                        progress = mMinFee / stepFee;
                    } else if (fee > mMaxFee) {
                        progress = mMaxFee / stepFee;
                    } else {
                        progress = fee.intValue() / stepFee;
                    }
                    mSeekBarFee.setProgress(progress);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTextInputEditTextFee.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(mSeekBarFee!=null) {
                        textViewChangeValue = true;
                        double value = (mMinFee + (mSeekBarFee.getProgress() * stepFee)) / 100000000.;
                        seekBarChangeValue = true;
                        mTextInputEditTextFee.setText(new DecimalFormat("#.########").format(value));
                    }
                }
            }
        });
    }

    @Override
    public void updateFee(double minFee, double maxFee) {
        mFontTextViewMaxFee.setText(new DecimalFormat("#.########").format(maxFee));
        mFontTextViewMinFee.setText(new DecimalFormat("#.########").format(minFee));
        mMinFee = Double.valueOf(minFee * 100000000).intValue();
        mMaxFee = Double.valueOf(maxFee * 100000000).intValue();
        mSeekBarFee.setMax((mMaxFee - mMinFee) / stepFee);
        //mSeekBarFee.setProgress((int)(INITIAL_FEE*100000000));
    }

    @Override
    public void updateGasPrice(int minGasPrice, int maxGasPrice) {
        mFontTextViewMaxGasPrice.setText(String.valueOf(maxGasPrice));
        mFontTextViewMinGasPrice.setText(String.valueOf(minGasPrice));
        mMinGasPrice = minGasPrice;
        mMaxGasPrice = maxGasPrice;
        mSeekBarGasPrice.setMax((mMaxGasPrice - mMinGasPrice) / stepGasPrice);
    }

    @Override
    public void updateGasLimit(int minGasLimit, int maxGasLimit) {
        mFontTextViewMaxGasLimit.setText(String.valueOf(maxGasLimit));
        mFontTextViewMinGasLimit.setText(String.valueOf(minGasLimit));
        mMinGasLimit = minGasLimit;
        mMaxGasLimit = maxGasLimit;
        mSeekBarGasLimit.setMax((mMaxGasLimit - mMinGasLimit) / stepGasLimit);
        mSeekBarGasLimit.setProgress(19);
    }

    @OnClick(R.id.ibt_back)
    public void onBackClick() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.confirm)
    public void onConfirmClick(){
        int gasLimit = Integer.valueOf(mFontTextViewGasLimit.getText().toString());
        int gasPrice = Integer.valueOf(mFontTextViewGasPrice.getText().toString());
        String fee = mTextInputEditTextFee.getText().toString();
        presenter.confirmContract(getArguments().getString(CONTRACT_TEMPLATE_UIID),gasLimit,gasPrice,fee);
    }

    @Override
    protected void createPresenter() {
        presenter = new ContractConfirmPresenterImpl(this);
    }

    @Override
    protected BaseFragmentPresenterImpl getPresenter() {
        return presenter;
    }

    @Override
    public String getContractName() {
        return getArguments().getString(CONTRACT_NAME);
    }

    @Override
    public void onClick(int adapterPosition) {
    }

    @Override
    public void makeToast(String s) {
        Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();
    }

    @Override
    public QtumApplication getApplication() {
        return getMainActivity().getQtumApplication();
    }

}
