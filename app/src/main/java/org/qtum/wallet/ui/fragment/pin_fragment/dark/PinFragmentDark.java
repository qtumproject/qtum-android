package org.qtum.wallet.ui.fragment.pin_fragment.dark;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.ui.fragment.pin_fragment.PinFragment;


public class PinFragmentDark extends PinFragment {
    @Override
    protected int getLayout() {
        return R.layout.fragment_pin;
    }

    boolean isBottomNavigationViewVisible;

    @Override
    public void initializeViews() {
        super.initializeViews();
        isBottomNavigationViewVisible = ((MainActivity)getActivity()).isBottomNavigationViewVisible();
        ((MainActivity)getActivity()).hideBottomNavigationView(R.color.background);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(isBottomNavigationViewVisible){
            ((MainActivity)getActivity()).showBottomNavigationView(true);
        }else{
            ((MainActivity)getActivity()).hideBottomNavigationView(R.color.background);
        }
    }
}
