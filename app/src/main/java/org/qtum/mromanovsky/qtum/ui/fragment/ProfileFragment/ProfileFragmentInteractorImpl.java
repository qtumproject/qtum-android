package org.qtum.mromanovsky.qtum.ui.fragment.ProfileFragment;

import android.content.Context;

import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;


public class ProfileFragmentInteractorImpl implements ProfileFragmentInteractor {

    Context mContext;

    public ProfileFragmentInteractorImpl(Context context){
        mContext = context;;
    }

    @Override
    public void clearSharedPreference() {
        QtumSharedPreference.getInstance().clear(mContext);
    }
}
