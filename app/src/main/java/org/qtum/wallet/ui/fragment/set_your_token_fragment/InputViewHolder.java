package org.qtum.wallet.ui.fragment.set_your_token_fragment;

import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import org.qtum.wallet.model.contract.ContractMethodParameter;
import org.qtum.wallet.utils.EditTextValidated;
import org.qtum.wallet.utils.FontManager;

import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class InputViewHolder extends RecyclerView.ViewHolder implements EditTextValidated.EditTextValidateListener {

    private static String TYPE_BOOL = "bool";

    private static final String TYPE_UINT = "uint";
    private static final String TYPE_UINT8 = "uint8";
    private static final String TYPE_UINT16 = "uint16";
    private static final String TYPE_UINT32 = "uint32";
    private static final String TYPE_UINT64 = "uint64";
    private static final String TYPE_UINT128 = "uint128";
    private static final String TYPE_UINT256 = "uint256";

    private static String DENY = "";
    private static String ALLOW = null;

    private static final String TYPE_INT = "int";

    private static int uint8 = (int) Math.pow(2,8);
    private static int uint16 = (int) Math.pow(2,16);
    private static long uint32 = (long) Math.pow(2, 32);
    private static long uint64 = (long) Math.pow(2, 64);
    private static BigInteger uint128 = new BigInteger("2").pow(128);
    private static BigInteger uint256 = new BigInteger("2").pow(256);

    private ContractMethodParameter parameter;

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            for (int i = start;i < end;i++) {
                if (!Character.isLetterOrDigit(source.charAt(i)) && !Character.toString(source.charAt(i)).equals("_") && !Character.toString(source.charAt(i)).equals("-")) {
                    return "";
                }
            }

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
                        return validateUINT(content, uint64);
                    case TYPE_UINT128:
                        return validateUINT(content, uint128);
                    case TYPE_UINT256:
                        return validateUINT(content, uint256);

                    default:
                        return ALLOW;
                }
            } else {
                return ALLOW;
            }
        }
    };

    @BindView(org.qtum.wallet.R.id.til_param)
    protected
    TextInputLayout tilParam;

    @BindView(org.qtum.wallet.R.id.et_param)
    protected
    EditTextValidated etParam;

    @BindView(org.qtum.wallet.R.id.checkbox)
    AppCompatCheckBox checkBox;

    OnValidateParamsListener listener;

    public InputViewHolder(View itemView, OnValidateParamsListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.listener = listener;

        tilParam.setTypeface(FontManager.getInstance().getFont(tilParam.getContext().getString(org.qtum.wallet.R.string.simplonMonoRegular)));
        etParam.setTypeface(FontManager.getInstance().getFont(etParam.getContext().getString(org.qtum.wallet.R.string.simplonMonoRegular)));
        etParam.setFilters(new InputFilter[]{filter});
        etParam.setOnEditTextValidateListener(this);
    }

    public void bind (ContractMethodParameter parameter, boolean isLast) {
        this.parameter = parameter;
        tilParam.setHint(fromCamelCase(parameter.getName()));
        setInputType(parameter.getType());
        if(isLast){
            etParam.setImeOptions(EditorInfo.IME_ACTION_DONE);
        }else {
            etParam.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        }
    }

    private String fromCamelCase(String cCase) {
        if(TextUtils.isEmpty(parameter.getDisplayName())) {
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

    private void setInputType(String type){
        int inputType = InputType.TYPE_CLASS_TEXT;
        if(type.contains(TYPE_BOOL)){
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

    private String validateINT(String content){
        try {
            int num = Integer.parseInt(content);
            if (num > Integer.MIN_VALUE && num < Integer.MAX_VALUE) {
                return ALLOW;
            }
        } catch (Exception e) {
            return DENY;
        }
        return DENY;
    }

    private String validateUINT(String content, long uint){
        try {
            long num = Long.parseLong(content);
            if (num > 0 && num < uint) {
                return ALLOW;
            }
        } catch (Exception e) {
            return DENY;
        }
        return DENY;
    }

    private String validateUINT(String content, BigInteger uint){
        try {
            BigInteger num = new BigInteger(content);
            if ((num.compareTo(BigInteger.ZERO) > 0) && (num.compareTo(uint) < 0)) {
                return ALLOW;
            }
        } catch (Exception e) {
            return DENY;
        }
        return DENY;
    }

    @Override
    public void onValidate(String text) {
        parameter.setValue(text);
        listener.onValidate();
    }
}
