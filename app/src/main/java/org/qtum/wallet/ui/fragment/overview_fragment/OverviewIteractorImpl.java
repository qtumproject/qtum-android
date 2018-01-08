package org.qtum.wallet.ui.fragment.overview_fragment;


import android.content.Context;

import java.lang.ref.WeakReference;

public class OverviewIteractorImpl implements OverviewIteractor{

    WeakReference<Context> mContext;

    OverviewIteractorImpl(Context context){
        mContext = new WeakReference<Context>(context);
    }

}
