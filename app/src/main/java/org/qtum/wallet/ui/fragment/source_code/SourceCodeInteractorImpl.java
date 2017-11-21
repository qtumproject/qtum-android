package org.qtum.wallet.ui.fragment.source_code;

import android.content.Context;

import java.lang.ref.WeakReference;

public class SourceCodeInteractorImpl implements SourceCodeInteractor {
    private WeakReference<Context> mContext;

    SourceCodeInteractorImpl(Context context) {
        mContext = new WeakReference<Context>(context);
    }
}
