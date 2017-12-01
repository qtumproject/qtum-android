package org.qtum.wallet.ui.fragment.contract_function_fragment.contract_default_function_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.qtum.wallet.R;
import org.qtum.wallet.model.AddressWithBalance;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.contract_function_fragment.ParameterAdapter;
import org.qtum.wallet.ui.fragment.pin_fragment.PinDialogFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.utils.FontButton;
import org.qtum.wallet.utils.FontTextView;
import org.qtum.wallet.utils.ResizeHeightAnimation;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class ContractFunctionDefaultFragment extends BaseFragment implements ContractFunctionDefaultView {

    private ContractFunctionDefaultPresenter mContractFunctionPresenterImpl;
    private final static String CONTRACT_TEMPLATE_UIID = "contract_template_uiid";
    private final static String METHOD_NAME = "method_name";
    private final static String CONTRACT_ADDRESS = "contract_address";

    @BindView(org.qtum.wallet.R.id.recycler_view)
    protected RecyclerView mParameterList;
    protected ParameterAdapter mParameterAdapter;
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

    @BindView(R.id.til_send_to_contract)
    TextInputLayout mTilSendToContract;
    @BindView(R.id.et_send_to_contract)
    EditText mEtSendToContract;

    @BindView(R.id.bt_edit_close)
    FontButton mFontButtonEditClose;
    @BindView(R.id.seek_bar_container)
    LinearLayout mLinearLayoutSeekBarContainer;

    @BindView(R.id.spinner_default_address)
    protected Spinner mSpinner;

    @BindView(R.id.tv_address)
    TextView mTextViewAddress;

    @BindView(R.id.nested_scroll_view)
    NestedScrollView mNestedScrollView;

    int mMinFee;
    int mMaxFee;
    int stepFee = 1;

    boolean seekBarChangeValue = false;
    boolean textViewChangeValue = false;

    int mMinGasPrice;
    int mMaxGasPrice;
    int stepGasPrice = 5;

    int mMinGasLimit;
    int mMaxGasLimit;
    int stepGasLimit = 100000;

    private int appLogoHeight = 0;
    private ResizeHeightAnimation mAnimForward;
    private ResizeHeightAnimation mAnimBackward;
    boolean showing = false;

    @OnClick({R.id.ibt_back, R.id.call, R.id.bt_edit_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
            case R.id.call:
                showPinDialog();
                break;
            case R.id.bt_edit_close:
                if (showing) {
                    mLinearLayoutSeekBarContainer.startAnimation(mAnimBackward);
                    mFontButtonEditClose.setText(R.string.edit);
                    showing = !showing;
                } else {
                    mLinearLayoutSeekBarContainer.startAnimation(mAnimForward);
                    mFontButtonEditClose.setText(R.string.close);
                    showing = !showing;
                }
                break;
        }
    }

    private void initializeAnim() {
        mAnimForward = new ResizeHeightAnimation(mLinearLayoutSeekBarContainer, 0, appLogoHeight);
        mAnimForward.setDuration(300);
        mAnimForward.setFillEnabled(true);
        mAnimForward.setFillAfter(true);

        mAnimBackward = new ResizeHeightAnimation(mLinearLayoutSeekBarContainer, appLogoHeight, 0);
        mAnimBackward.setDuration(300);
        mAnimBackward.setFillEnabled(true);
        mAnimBackward.setFillAfter(true);
    }

    public static BaseFragment newInstance(Context context, String methodName, String uiid, String contractAddress) {
        Bundle args = new Bundle();
        args.putString(CONTRACT_TEMPLATE_UIID, uiid);
        args.putString(METHOD_NAME, methodName);
        args.putString(CONTRACT_ADDRESS, contractAddress);
        BaseFragment fragment = Factory.instantiateFragment(context, ContractFunctionDefaultFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mContractFunctionPresenterImpl = new ContractFunctionDefaultPresenterImpl(this, new ContractFunctionDefaultInteractorImpl(getContext()));
    }

    @Override
    protected ContractFunctionDefaultPresenter getPresenter() {
        return mContractFunctionPresenterImpl;
    }

    @Override
    public void showEtSendToContract() {
        mTilSendToContract.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEtSendToContract() {
        mTilSendToContract.setVisibility(View.GONE);
    }

    private void showPinDialog() {
        PinDialogFragment pinDialogFragment = new PinDialogFragment();
        pinDialogFragment.setTouchIdFlag(getMainActivity().checkTouchIdEnable());
        pinDialogFragment.addPinCallBack(callback);
        pinDialogFragment.show(getFragmentManager(), pinDialogFragment.getClass().getCanonicalName());
    }

    PinDialogFragment.PinCallBack callback = new PinDialogFragment.PinCallBack() {
        @Override
        public void onSuccess() {
            getPresenter().onCallClick(mParameterAdapter.getParams(), getArguments().getString(CONTRACT_ADDRESS), mTextInputEditTextFee.getText().toString(),
                    Integer.valueOf(mFontTextViewGasLimit.getText().toString()), Integer.valueOf(mFontTextViewGasPrice.getText().toString()), getArguments().getString(METHOD_NAME),mTextViewAddress.getText().toString(), mEtSendToContract.getText().toString());
        }

        @Override
        public void onError(String error) {
            hideKeyBoard();
            setAlertDialog(R.string.warning, error, R.string.cancel, PopUpType.error);
        }
    };

    @Override
    public void initializeViews() {
        super.initializeViews();
        mParameterList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSeekBarFee.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (textViewChangeValue) {
                    textViewChangeValue = false;
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

        if (mLinearLayoutSeekBarContainer.getHeight() == 0) {
            mLinearLayoutSeekBarContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mLinearLayoutSeekBarContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    appLogoHeight = (appLogoHeight == 0) ? mLinearLayoutSeekBarContainer.getHeight() : appLogoHeight;
                    initializeAnim();
                    mLinearLayoutSeekBarContainer.getLayoutParams().height = 0;
                    mLinearLayoutSeekBarContainer.requestLayout();
                }
            });
        } else {
            appLogoHeight = (appLogoHeight == 0) ? mLinearLayoutSeekBarContainer.getHeight() : appLogoHeight;
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
                if (seekBarChangeValue) {
                    seekBarChangeValue = false;
                    return;
                }
                if (!s.toString().isEmpty()) {
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
                if (!hasFocus) {
                    if (mSeekBarFee != null) {
                        textViewChangeValue = true;
                        double value = (mMinFee + (mSeekBarFee.getProgress() * stepFee)) / 100000000.;
                        seekBarChangeValue = true;
                        mTextInputEditTextFee.setText(new DecimalFormat("#.########").format(value));
                    }
                }
            }
        });

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mTextViewAddress.setText(((AddressWithBalance)mSpinner.getItemAtPosition(i)).getAddress());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mLinearLayoutSeekBarContainer.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, final int i3, final int i4, int i5, int i6, final int i7) {
                mNestedScrollView.post(new Runnable() {

                    @Override
                    public void run() {
                        if(i3>i7)
                        mNestedScrollView.scrollTo(0,mNestedScrollView.getScrollY()+i3);
                    }
                });
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
        mSeekBarFee.setProgress(10000000 - mMinFee);
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
        mSeekBarGasLimit.setProgress(1);
    }

    @Override
    public String getContractTemplateUiid() {
        return getArguments().getString(CONTRACT_TEMPLATE_UIID);
    }

    @Override
    public String getMethodName() {
        return getArguments().getString(METHOD_NAME);
    }

    @Override
    public void setSoftMode() {
        super.setSoftMode();
        getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }
}
