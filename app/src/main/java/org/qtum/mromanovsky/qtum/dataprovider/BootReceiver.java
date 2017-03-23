package org.qtum.mromanovsky.qtum.dataprovider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.qtum.mromanovsky.qtum.datastorage.QtumSharedPreference;


public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(QtumSharedPreference.getInstance().getKeyGeneratedInstance(context)) {
            Intent intentService = new Intent(context, UpdateService.class);
            context.startService(intentService);
        }
    }
}
