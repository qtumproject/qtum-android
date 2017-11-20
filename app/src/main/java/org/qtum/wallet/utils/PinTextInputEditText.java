package org.qtum.wallet.utils;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.View;

public class PinTextInputEditText extends TextInputEditText {

    private char defaultHintChar = '■';

    public PinTextInputEditText(Context context) {
        super(context);
    }

    public PinTextInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTransformationMethod(new AsteriskPasswordTransformationMethod());
    }

    public PinTextInputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;

            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }

            public char charAt(int index) {
                return defaultHintChar; // This is the important part
            }

            public int length() {
                return mSource.length(); // Return default
            }

            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    }
}
