package org.qtum.mromanovsky.qtum.dataprovider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //TODO: uncomment
//        if(QtumSharedPreference.getInstance().getKeyGeneratedInstance(context)) {
//            Intent intentService = new Intent(context, UpdateService.class);
//            context.startService(intentService);
//        }
    }
}
