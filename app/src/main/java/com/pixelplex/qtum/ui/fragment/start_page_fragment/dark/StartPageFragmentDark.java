package com.pixelplex.qtum.ui.fragment.start_page_fragment.dark;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.fragment.start_page_fragment.StartPageFragment;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class StartPageFragmentDark extends StartPageFragment {
    @Override
    protected int getLayout() {
        return R.layout.fragment_start_page;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        hideBottomNavView(R.color.background);
    }

    @Override
    public void hideLoginButton() {
        mButtonLogin.setVisibility(View.GONE);
        mButtonCreateNew.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.accent_red_color));
        mButtonCreateNew.setTextColor(ContextCompat.getColor(getContext(),R.color.background));
    }

}
