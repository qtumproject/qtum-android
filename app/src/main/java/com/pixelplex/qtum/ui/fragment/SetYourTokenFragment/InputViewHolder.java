package com.pixelplex.qtum.ui.fragment.SetYourTokenFragment;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.contract.ContractMethodParameter;
import com.pixelplex.qtum.utils.FontManager;
import com.pixelplex.qtum.utils.FontTextView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class InputViewHolder extends RecyclerView.ViewHolder {

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

    private ContractMethodParameter parameter;

    private InputFilter filter = new InputFilter() {
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

    @BindView(R.id.til_param)
    TextInputLayout tilParam;

    @BindView(R.id.et_param)
    TextInputEditText etParam;

    @BindView(R.id.checkbox)
    AppCompatCheckBox checkBox;

    public InputViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        tilParam.setTypeface(FontManager.getInstance().getFont(tilParam.getContext().getString(R.string.simplonMonoRegular)));
        etParam.setTypeface(FontManager.getInstance().getFont(etParam.getContext().getString(R.string.simplonMonoRegular)));
        etParam.setFilters(new InputFilter[]{filter});
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
                parameter.setValue(String.valueOf(num));
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
                parameter.setValue(String.valueOf(num));
                return ALLOW;
            }
        } catch (Exception e) {
            return DENY;
        }
        return DENY;
    }


}
