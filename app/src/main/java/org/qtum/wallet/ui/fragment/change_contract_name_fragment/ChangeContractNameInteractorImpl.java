package org.qtum.wallet.ui.fragment.change_contract_name_fragment;


import android.content.Context;

import java.lang.ref.WeakReference;

public class ChangeContractNameInteractorImpl implements ChangeContractNameInteractor{

    WeakReference<Context> mContext;

    ChangeContractNameInteractorImpl(Context context){
        mContext = new WeakReference<Context>(context);
    }

}
