package org.qtum.wallet.utils;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.view.View;

public class EditTextValidated extends TextInputEditText {

    private EditTextValidateListener mOnImeBack;

    private String hint;
    private String topHint;

    public EditTextValidated(Context context) {
        super(context);
        init();
    }

    public EditTextValidated(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextValidated(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (mOnImeBack != null) {
            mOnImeBack.onValidate(text.toString());
        }
    }

    public void setOnEditTextValidateListener(EditTextValidateListener listener) {
        mOnImeBack = listener;
    }

    public interface EditTextValidateListener {
        void onValidate(String text);
    }

    public void setTopHint(String topHint){
        this.topHint = topHint;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        hint = ((TextInputLayout)getParent().getParent()).getHint().toString();
    }

    private void init(){
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    ((TextInputLayout)getParent().getParent()).setHint(topHint);
                } else {
                    ((TextInputLayout)getParent().getParent()).setHint(hint);
                }
            }
        });
    }
}
