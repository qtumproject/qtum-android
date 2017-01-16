package org.qtum.mromanovsky.qtum.ui.fragment.PinFragment;

import android.content.Context;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.params.MainNetParams;
import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;


public class PinFragmentInteractorImpl implements PinFragmentInteractor {

    private Context mContext;

    public PinFragmentInteractorImpl(Context context) {
        mContext = context;
    }

    @Override
    public int getPassword() {
        return QtumSharedPreference.getInstance().getWalletPassword(mContext);
    }

    @Override
    public void savePassword(int password) {
        QtumSharedPreference.getInstance().saveWalletPassword(mContext, password);
    }

    @Override
    public void generateAndSavePubKey() {
        ECKey ecKey = new ECKey();
        Address address = ecKey.toAddress(MainNetParams.get());
        QtumSharedPreference.getInstance().savePubKey(mContext, address.toString());
    }
}
