package org.qtum.wallet.ui.fragment.touch_id_preference_fragment;

import android.content.Context;

import org.qtum.wallet.datastorage.QtumSharedPreference;

import java.lang.ref.WeakReference;

public class TouchIDInterractorImpl implements TouchIDInterractor {
    private WeakReference<Context> mContext;

    public TouchIDInterractorImpl(Context context) {
        this.mContext = new WeakReference<>(context);
    }

    @Override
    public void saveTouchIDEnabled() {
        QtumSharedPreference.getInstance().saveTouchIdEnable(mContext.get(), true);
    }
}
