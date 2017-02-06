package org.qtum.mromanovsky.qtum.ui.fragment.CreateWalletNameFragment;

import android.content.Context;

import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;

/**
 * Created by max-v on 2/6/2017.
 */

public class CreateWalletNameFragmentInteractorImpl implements CreateWalletNameFragmentInteractor {

    Context mContext;

    public CreateWalletNameFragmentInteractorImpl(Context context){
        mContext = context;
    }

    @Override
    public void saveWalletName(String name) {
        QtumSharedPreference.getInstance().saveWalletName(mContext,name);
    }
}
