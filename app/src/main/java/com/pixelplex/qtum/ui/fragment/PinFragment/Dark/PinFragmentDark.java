package com.pixelplex.qtum.ui.fragment.PinFragment.Dark;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.activity.main_activity.MainActivity;
import com.pixelplex.qtum.ui.fragment.PinFragment.PinFragment;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class PinFragmentDark extends PinFragment {
    @Override
    protected int getLayout() {
        return R.layout.fragment_pin;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        ((MainActivity)getActivity()).hideBottomNavigationView(R.color.background);
    }
}
