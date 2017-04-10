package org.qtum.mromanovsky.qtum.ui.activity.MainActivity;

import android.content.Context;


interface MainActivityInteractor {
    boolean getKeyGeneratedInstance(Context context) ;
    void clearStatic();
}
