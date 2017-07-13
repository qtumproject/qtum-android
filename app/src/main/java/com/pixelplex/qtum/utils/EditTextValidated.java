package com.pixelplex.qtum.utils;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;

/**
 * Created by kirillvolkov on 12.07.17.
 */

public class EditTextValidated extends TextInputEditText {

    private EditTextValidateListener mOnImeBack;

    public EditTextValidated(Context context) {
        super(context);
    }

    public EditTextValidated(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextValidated(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if(mOnImeBack != null){
            mOnImeBack.onValidate(text.toString());
        }
    }

    public void setOnEditTextValidateListener(EditTextValidateListener listener) {
        mOnImeBack = listener;
    }

    public interface EditTextValidateListener {
       void onValidate(String text);
    }

}
