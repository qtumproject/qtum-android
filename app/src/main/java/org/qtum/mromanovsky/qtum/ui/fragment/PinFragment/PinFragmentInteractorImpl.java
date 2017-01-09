package org.qtum.mromanovsky.qtum.ui.fragment.PinFragment;

import android.content.Context;

import org.qtum.mromanovsky.qtum.utils.QtumSharedPreference;


public class PinFragmentInteractorImpl implements PinFragmentInteractor {

    private Context mContext;

    public PinFragmentInteractorImpl(Context context){
        mContext = context;
    }

    @Override
    public int getPassword() {
        return QtumSharedPreference.getInstance().getWalletPassword(mContext);
    }

    @Override
    public void savePassword(int password) {
        QtumSharedPreference.getInstance().saveWalletPassword(mContext,password);
    }
}
