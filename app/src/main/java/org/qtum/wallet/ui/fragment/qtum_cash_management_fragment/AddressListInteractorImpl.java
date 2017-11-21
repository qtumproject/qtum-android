package org.qtum.wallet.ui.fragment.qtum_cash_management_fragment;

import android.content.Context;

import org.qtum.wallet.dataprovider.rest_api.QtumService;
import org.qtum.wallet.datastorage.KeyStorage;

import org.qtum.wallet.model.gson.UnspentOutput;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.Observable;

public class AddressListInteractorImpl implements AddressListInteractor {

    private WeakReference<Context> mContext;

    AddressListInteractorImpl(Context context) {
        mContext = new WeakReference<Context>(context);
    }

    @Override
    public List<String> getAddresses() {
        return KeyStorage.getInstance().getAddresses();
    }

    @Override
    public Observable<List<UnspentOutput>> getUnspentOutputs(List<String> addresses) {
        return QtumService.newInstance().getUnspentOutputsForSeveralAddresses(addresses);
    }
}
