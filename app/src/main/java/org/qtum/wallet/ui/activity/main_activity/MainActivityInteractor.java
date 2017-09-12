package org.qtum.wallet.ui.activity.main_activity;

import android.content.Context;


interface MainActivityInteractor {
    boolean getKeyGeneratedInstance(Context context) ;
    void clearStatic();
}
