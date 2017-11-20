package org.qtum.wallet.ui.fragment.source_code.light;

import android.text.SpannableString;
import android.widget.TextView;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.fragment.source_code.SourceCodeFragment;

public class SourceCodeFragmentLight extends SourceCodeFragment {

    @Override
    protected int getLayout() {
        return R.layout.fragment_source_code_light;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        String sourceCode = getArguments().getString(SOURCE_CODE);
        SpannableString spannableSourceCode = formatCode(sourceCode, R.color.code_reserve_word_light, R.color.code_type_light, R.color.code_comment_light);
        mTextView.setText(spannableSourceCode, TextView.BufferType.SPANNABLE);
    }
}
