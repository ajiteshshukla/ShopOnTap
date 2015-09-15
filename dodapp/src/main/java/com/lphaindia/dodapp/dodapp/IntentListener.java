package com.lphaindia.dodapp.dodapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ajitesh.shukla on 9/13/15.
 */
public class IntentListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == Intent.ACTION_BOOT_COMPLETED) {
            Intent dodIntent = new Intent(context, DodIntentService.class);
            context.startService(dodIntent);
        }
    }
}
