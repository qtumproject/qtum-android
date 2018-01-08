package org.qtum.wallet.ui.fragment.event_log_fragment;


import android.content.Context;

import java.lang.ref.WeakReference;

public class EventLogInteractorImpl implements EventLogInteractor{

    WeakReference<Context> mContext;

    EventLogInteractorImpl(Context context){
        mContext = new WeakReference<Context>(context);
    }

}
