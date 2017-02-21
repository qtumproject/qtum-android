package org.qtum.mromanovsky.qtum.ui.fragment.ProfileFragment;

import android.content.Context;

import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;


class ProfileFragmentInteractorImpl implements ProfileFragmentInteractor {

    private Context mContext;

    ProfileFragmentInteractorImpl(Context context){
        mContext = context;
    }

    @Override
    public void clearSharedPreference() {
        QtumSharedPreference.getInstance().clear(mContext);
    }
}
