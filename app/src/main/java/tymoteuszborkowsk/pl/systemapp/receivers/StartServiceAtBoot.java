package tymoteuszborkowsk.pl.systemapp.receivers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import tymoteuszborkowsk.pl.systemapp.services.GPSTrackerService;

public class StartServiceAtBoot extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, GPSTrackerService.class);
            context.startService(serviceIntent);
        }
    }
}
