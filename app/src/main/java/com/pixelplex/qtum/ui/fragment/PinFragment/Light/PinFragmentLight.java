package com.pixelplex.qtum.ui.fragment.PinFragment.Light;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.ui.activity.main_activity.MainActivity;
import com.pixelplex.qtum.ui.fragment.PinFragment.PinFragment;

/**
 * Created by kirillvolkov on 06.07.17.
 */

public class PinFragmentLight extends PinFragment {
    @Override
    protected int getLayout() {
        return R.layout.fragment_pin_light;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).hideBottomNavigationView(R.color.title_color_light);
    }
}
