package org.qtum.wallet.ui.fragment.start_page_fragment.dark;

import android.support.v4.content.ContextCompat;
import android.view.View;

import org.qtum.wallet.ui.fragment.start_page_fragment.StartPageFragment;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class StartPageFragmentDark extends StartPageFragment {
    @Override
    protected int getLayout() {
        return org.qtum.wallet.R.layout.fragment_start_page;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        hideBottomNavView(org.qtum.wallet.R.color.background);
    }

    @Override
    public void hideLoginButton() {
        mButtonLogin.setVisibility(View.GONE);
        mButtonCreateNew.setBackgroundColor(ContextCompat.getColor(getContext(), org.qtum.wallet.R.color.accent_red_color));
        mButtonCreateNew.setTextColor(ContextCompat.getColor(getContext(), org.qtum.wallet.R.color.background));
    }

}
