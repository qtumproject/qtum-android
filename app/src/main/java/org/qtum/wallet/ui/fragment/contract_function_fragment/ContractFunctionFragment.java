package org.qtum.wallet.ui.fragment.contract_function_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import org.qtum.wallet.R;
import org.qtum.wallet.model.contract.ContractMethodParameter;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.utils.FontButton;
import org.qtum.wallet.utils.FontManager;
import org.qtum.wallet.utils.FontTextView;
import org.qtum.wallet.utils.ResizeHeightAnimation;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public abstract class ContractFunctionFragment extends BaseFragment implements ContractFunctionView {

    private ContractFunctionPresenter mContractFunctionPresenterImpl;
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

    @BindView(R.id.bt_edit_close)
    FontButton mFontButtonEditClose;
    @BindView(R.id.seek_bar_container)
    LinearLayout mLinearLayoutSeekBarContainer;

    int mMinFee;
    int mMaxFee;
    int stepFee = 100;

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

    @OnClick({org.qtum.wallet.R.id.ibt_back, org.qtum.wallet.R.id.cancel, org.qtum.wallet.R.id.call, R.id.bt_edit_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case org.qtum.wallet.R.id.cancel:
            case org.qtum.wallet.R.id.ibt_back:
                getActivity().onBackPressed();
                break;
            case org.qtum.wallet.R.id.call:
                getPresenter().onCallClick(mParameterAdapter.getParams(), getArguments().getString(CONTRACT_ADDRESS), mTextInputEditTextFee.getText().toString(),
                        Integer.valueOf(mFontTextViewGasLimit.getText().toString()), Integer.valueOf(mFontTextViewGasPrice.getText().toString()), getArguments().getString(METHOD_NAME));
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
        //mAnimForward.setAnimationListener(this);

        mAnimBackward = new ResizeHeightAnimation(mLinearLayoutSeekBarContainer, appLogoHeight, 0);
        mAnimBackward.setDuration(300);
        mAnimBackward.setFillEnabled(true);
        mAnimBackward.setFillAfter(true);
        //mAnimBackward.setAnimationListener(this);
    }

    public static BaseFragment newInstance(Context context, String methodName, String uiid, String contractAddress) {

        Bundle args = new Bundle();
        args.putString(CONTRACT_TEMPLATE_UIID, uiid);
        args.putString(METHOD_NAME, methodName);
        args.putString(CONTRACT_ADDRESS, contractAddress);
        BaseFragment fragment = Factory.instantiateFragment(context, ContractFunctionFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mContractFunctionPresenterImpl = new ContractFunctionPresenterImpl(this, new ContractFunctionInteractorImpl(getContext()));
    }

    @Override
    protected ContractFunctionPresenter getPresenter() {
        return mContractFunctionPresenterImpl;
    }

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

    class ParameterViewHolder extends RecyclerView.ViewHolder {

        private String TYPE_BOOL = "bool";

        private final String TYPE_UINT = "uint";
        private final String TYPE_UINT8 = "uint8";
        private final String TYPE_UINT16 = "uint16";
        private final String TYPE_UINT32 = "uint32";
        private final String TYPE_UINT64 = "uint64";
        private final String TYPE_UINT128 = "uint128";
        private final String TYPE_UINT256 = "uint256";

        private String DENY = "";
        private String ALLOW = null;

        private final String TYPE_INT = "int";

        private int uint8 = (int) Math.pow(2, 8);
        private int uint16 = (int) Math.pow(2, 16);
        private long uint32 = (long) Math.pow(2, 32);
        private long uint64 = (long) Math.pow(2, 64);

        private ContractMethodParameter parameter;

        public InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String content = etParam.getText().toString() + source;
                if (!TextUtils.isEmpty(content)) {
                    switch (parameter.getType()) {
                        case TYPE_INT:
                            return validateINT(content);
                        case TYPE_UINT8:
                            return validateUINT(content, uint8);
                        case TYPE_UINT16:
                            return validateUINT(content, uint16);
                        case TYPE_UINT32:
                            return validateUINT(content, uint32);
                        case TYPE_UINT64:
                        case TYPE_UINT128:
                        case TYPE_UINT256:
                            return validateUINT(content, uint64);

                        default:
                            parameter.setValue(content);
                            return ALLOW;
                    }
                } else {
                    return ALLOW;
                }
            }
        };

        @BindView(org.qtum.wallet.R.id.til_param)
        TextInputLayout tilParam;

        @BindView(org.qtum.wallet.R.id.et_param)
        TextInputEditText etParam;

        @BindView(org.qtum.wallet.R.id.checkbox)
        AppCompatCheckBox checkBox;

        public ParameterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            tilParam.setTypeface(FontManager.getInstance().getFont(tilParam.getContext().getString(org.qtum.wallet.R.string.simplonMonoRegular)));
            etParam.setTypeface(FontManager.getInstance().getFont(etParam.getContext().getString(org.qtum.wallet.R.string.simplonMonoRegular)));
            etParam.setFilters(new InputFilter[]{filter});
        }

        public void bind(ContractMethodParameter parameter, boolean isLast) {
            this.parameter = parameter;

            tilParam.setHint(fromCamelCase(parameter.getName()));
            setInputType(parameter.getType());
            if (isLast) {
                etParam.setImeOptions(EditorInfo.IME_ACTION_DONE);
            } else {
                etParam.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            }
        }

        private String fromCamelCase(String cCase) {
            if (TextUtils.isEmpty(parameter.getDisplayName())) {
                StringBuilder builder = new StringBuilder(cCase);
                for (int i = builder.length() - 1; i > 0; i--) {
                    char ch = builder.charAt(i);
                    if (Character.isUpperCase(ch)) {
                        builder = builder.insert(i, ' ');
                    }
                }

                String value = builder.toString().replace("_", "");
                parameter.setDisplayName(value.substring(0, 1).toUpperCase() + value.substring(1));
            }
            return parameter.getDisplayName();
        }

        private void setInputType(String type) {

            int inputType = InputType.TYPE_CLASS_TEXT;

            if (type.contains(TYPE_BOOL)) {

                tilParam.setVisibility(View.INVISIBLE);
                checkBox.setVisibility(View.VISIBLE);

            } else {

                tilParam.setVisibility(View.VISIBLE);
                checkBox.setVisibility(View.INVISIBLE);

                if (type.contains(TYPE_UINT)) {

                    inputType = InputType.TYPE_CLASS_NUMBER;

                } else if (type.contains(TYPE_INT)) {

                    inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED;
                }

                etParam.setInputType(inputType);

            }
        }

        private String validateINT(String content) {
            try {
                int num = Integer.parseInt(content);
                if (num > Integer.MIN_VALUE && num < Integer.MAX_VALUE) {
                    parameter.setValue(String.valueOf(num));
                    return ALLOW;
                }
            } catch (Exception e) {
                return DENY;
            }
            return DENY;
        }

        private String validateUINT(String content, long uint) {
            try {
                long num = Long.parseLong(content);
                if (num > 0 && num < uint) {
                    parameter.setValue(String.valueOf(num));
                    return ALLOW;
                }
            } catch (Exception e) {
                return DENY;
            }
            return DENY;
        }
    }

    public class ParameterAdapter extends RecyclerView.Adapter<ParameterViewHolder> {

        List<ContractMethodParameter> params;
        int mResId;

        public List<ContractMethodParameter> getParams() {
            return params;
        }

        public ParameterAdapter(List<ContractMethodParameter> params, int resId) {
            this.params = params;
            mResId = resId;
        }

        @Override
        public ParameterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ParameterViewHolder(LayoutInflater.from(parent.getContext()).inflate(mResId, parent, false));
        }

        @Override
        public void onBindViewHolder(ParameterViewHolder holder, int position) {
            holder.bind(params.get(position), position == getItemCount() - 1);
        }

        @Override
        public int getItemCount() {
            return params.size();
        }
    }
}
