package org.qtum.wallet.ui.fragment.source_code;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.LeadingMarginSpan;
import android.view.View;

import org.qtum.wallet.R;
import org.qtum.wallet.model.Location;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.utils.FontTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;


public abstract class SourceCodeFragment extends BaseFragment implements SourceCodeView{

    protected final static String SOURCE_CODE = "source_code";

    @BindView(R.id.tv_source_code)
    protected FontTextView mTextView;

    SourceCodePresenter mSourceCodePresenter;

    @OnClick({R.id.ibt_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case org.qtum.wallet.R.id.ibt_back:
                getActivity().onBackPressed();
                break;
        }
    }

    public static BaseFragment newInstance(Context context, String sourceCode) {

        Bundle args = new Bundle();
        args.putString(SOURCE_CODE, sourceCode);
        BaseFragment fragment = Factory.instantiateFragment(context, SourceCodeFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mSourceCodePresenter = new SourceCodePresenterImpl(this, new SourceCodeInteractorImpl(getContext()));
    }

    @Override
    protected SourceCodePresenter getPresenter() {
        return mSourceCodePresenter;
    }

    protected SpannableString formatCode(String sourceCode, @ColorRes int codeReserveWord, @ColorRes int codeType, @ColorRes int codeComment){
        Pattern r = Pattern.compile("\\n(\\s+)");
        Matcher m = r.matcher(sourceCode);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, m.group(0).replaceFirst(Pattern.quote(m.group(1)), ""));
        }
        m.appendTail(sb);
        sourceCode = sb.toString();



        SpannableString spannableSourceCode = new SpannableString(sourceCode);

        Pattern pattern = Pattern.compile("[-+]?\\b\\d+\\b");
        Matcher matcher = pattern.matcher(spannableSourceCode.toString());
        int start = 0;
        while (matcher.find(start)) {
            spannableSourceCode.setSpan(new ForegroundColorSpan(getResources().getColor(codeReserveWord)), matcher.start(), matcher.end(), Spanned.SPAN_PRIORITY);
            start = matcher.end();
        }

        pattern = Pattern.compile("\\b(uint[0-9]{0,3}|bool|int[0-9]{0,3}|address|string)\\b");
        matcher = pattern.matcher(spannableSourceCode.toString());
        start = 0;
        while (matcher.find(start)) {
            spannableSourceCode.setSpan(new ForegroundColorSpan(getResources().getColor(codeType)), matcher.start(), matcher.end(), Spanned.SPAN_PRIORITY);
            start = matcher.end();
        }

        pattern = Pattern.compile("\\b(struct|public|return|if|else|event|contract|returns)\\b");
        matcher = pattern.matcher(spannableSourceCode.toString());
        start = 0;
        while (matcher.find(start)) {
            spannableSourceCode.setSpan(new ForegroundColorSpan(getResources().getColor(codeReserveWord)), matcher.start(), matcher.end(), Spanned.SPAN_PRIORITY);
            start = matcher.end();
        }

        pattern = Pattern.compile("(function)[ ]+(\\w+)(\\(([^)]*)\\))");
        matcher = pattern.matcher(spannableSourceCode.toString());
        start = 0;
        while (matcher.find(start)) {
            spannableSourceCode.setSpan(new ForegroundColorSpan(getResources().getColor(codeReserveWord)), matcher.start(1), matcher.end(1), 0);
            spannableSourceCode.setSpan(new ForegroundColorSpan(getResources().getColor(codeReserveWord)), matcher.start(2), matcher.end(2), 0);
            spannableSourceCode.setSpan(new ForegroundColorSpan(getResources().getColor(codeType)), matcher.start(3), matcher.end(3), 0);
            start = matcher.end();
        }

        pattern = Pattern.compile("(\\/\\*([^*]|[\\r\\n]|(\\*+([^*\\/]|[\\r\\n])))*\\*+\\/)|(\\/\\/.*)");
        matcher = pattern.matcher(spannableSourceCode.toString());
        start = 0;
        while (matcher.find(start)) {
            spannableSourceCode.setSpan(new ForegroundColorSpan(getResources().getColor(codeComment)), matcher.start(), matcher.end(), Spanned.SPAN_PRIORITY_SHIFT);
            start = matcher.end();
        }


        int len = sourceCode.length();
        char[] buffer = sourceCode.toCharArray();


        ArrayList<Integer> openningBracersPositions = new ArrayList<>();
        HashMap<Integer, HashSet<Location>> threeOfFunctionDict = new HashMap<>();


        int closureBracersCount = 0;

        for (int i = 0; i < len; i++) {

            if (buffer[i] == '{') {

                openningBracersPositions.add(i);
            } else if (buffer[i] == '}') {

                closureBracersCount++;
                int openingBracerIndex = openningBracersPositions.size() - closureBracersCount;

                if (openingBracerIndex < openningBracersPositions.size() - 1 || openingBracerIndex >= 0) {

                    int location = openningBracersPositions.get(openingBracerIndex);

                    HashSet<Location> set = threeOfFunctionDict.get(openingBracerIndex);

                    if (set == null) {
                        set = new HashSet<>();
                        threeOfFunctionDict.put(openingBracerIndex, set);
                    }

                    set.add(new Location(location, i));
                    closureBracersCount--;
                    openningBracersPositions.remove(openingBracerIndex);
                }
            }
        }

        for(HashMap.Entry<Integer, HashSet<Location>> integerHashSetEntry : threeOfFunctionDict.entrySet()){
            for(Location location : integerHashSetEntry.getValue()) {
                spannableSourceCode.setSpan(new LeadingMarginSpan.Standard(64 * (integerHashSetEntry.getKey()+1)), location.getLocationStart(), location.getLocationEnd(), 0);
            }
        }

        return spannableSourceCode;
    }
}
