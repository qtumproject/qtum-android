package com.pixelplex.qtum.utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pixelplex.qtum.R;


public class SearchBar extends RelativeLayout implements View.OnClickListener{

    View view;
    LinearLayout placeholder;
    ImageView placeholderIcon;
    TextView placeholderText;
    EditText input;
    Button cancel;

    SearchBarListener listener;

    boolean isActive;
    int initialInputWidth;

    ResizeAnimation resizeAnimation;

    public SearchBar(Context context) {
        super(context);
    }

    public SearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = LayoutInflater.from(getContext()).inflate(R.layout.lyt_search_view, this, false);
        addView(view);
        build();
    }

    public SearchBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SearchBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setListener(SearchBarListener listener){
        this.listener = listener;
    }

    private void build(){
        setClickable(true);
        placeholderIcon = (ImageView) findViewById(R.id.icon);
        placeholderText = (TextView) findViewById(R.id.text);
        input = (EditText) findViewById(R.id.input);
        placeholder = (LinearLayout) findViewById(R.id.placeholder);
        cancel = (Button) findViewById(R.id.cancel);

        resizeAnimation = new ResizeAnimation(input);
        resizeAnimation.setDuration(300);

        setOnClickListener(this);

        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                input.clearFocus();
            }
        });

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(listener != null){
                    listener.onRequestSearch(s.toString());
                }
            }
        });

        input.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                input.setText("");
                OnFocusChange(hasFocus);
                if(hasFocus) {
                    initialInputWidth = (initialInputWidth == 0)? input.getWidth() : initialInputWidth;
                    placeholderText.animate().alpha(0).setDuration(300).start();
                    placeholder.animate().x(input.getX()).setDuration(300).start();
                    cancel.setVisibility(VISIBLE);
                    cancel.animate().alpha(1).setDuration(300).start();
                    resizeAnimation.setParams(initialInputWidth, initialInputWidth - cancel.getWidth());
                } else {
                    placeholderText.animate().alpha(1).setDuration(300).start();
                    placeholder.animate().x(getWidth()/2 - placeholder.getWidth()/2).setDuration(300).start();

                    cancel.animate().alpha(0).setDuration(300).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            cancel.setVisibility(INVISIBLE);
                        }
                    }).start();

                    hideKeyboard();
                    resizeAnimation.setParams(initialInputWidth - cancel.getWidth(), initialInputWidth);
                }
                input.startAnimation(resizeAnimation);
            }
        });
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }

    private void OnFocusChange(boolean focus){
        if(listener != null){
            if(focus){
                listener.onActivate();
            } else {
                listener.onDeactivate();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(!isActive) {

        }
    }
}
