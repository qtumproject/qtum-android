package org.qtum.wallet.ui.fragment.confirm_passphrase_fragment;


import android.content.Context;

import java.lang.ref.WeakReference;

public class ConfirmPassphraseInteractorImpl implements ConfirmPassphraseInteractor {

    WeakReference<Context> mContext;

    ConfirmPassphraseInteractorImpl(Context context){
        mContext = new WeakReference<Context>(context);
    }

}
