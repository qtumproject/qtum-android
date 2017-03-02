package org.qtum.mromanovsky.qtum.dataprovider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by max-v on 3/1/2017.
 */

public class BootReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentService = new Intent(context, UpdateService.class);
        context.startService(intentService);
    }
}
