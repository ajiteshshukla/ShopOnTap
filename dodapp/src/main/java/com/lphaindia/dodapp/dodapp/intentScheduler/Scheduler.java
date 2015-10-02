package com.lphaindia.dodapp.dodapp.intentScheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.lphaindia.dodapp.dodapp.AppConstants;
import com.lphaindia.dodapp.dodapp.DodIntentService;

import java.util.Calendar;

/**
 * Created by ajitesh.shukla on 9/15/15.
 */
public class Scheduler {

    private Context context;

    public Scheduler(Context context) {
        this.context = context;
    }

    public void schedule(long scheduleTime) {
        if (scheduleTime > Calendar.getInstance().getTimeInMillis()) {
            Intent dodIntentService = new Intent(context, DodIntentService.class);
            PendingIntent pi = PendingIntent.getService(context, 1, dodIntentService, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager dodAlarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            dodAlarm.set(AlarmManager.RTC_WAKEUP, scheduleTime, pi);
            //Log.d(AppConstants.TAG, "Alarm set for time: " + scheduleTime);
        } else {
            Intent dodIntentService = new Intent(context, DodIntentService.class);
            PendingIntent pi = PendingIntent.getService(context, 1, dodIntentService, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager dodAlarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            dodAlarm.set(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + AppConstants.TIME_DELAY, pi);
            //Log.d(AppConstants.TAG, "Schedule time less then current: " + scheduleTime);
        }
    }

    public void cancelNextSchedule() {
        Intent dodServiceIntent = new Intent(context, DodIntentService.class);
        PendingIntent pi = PendingIntent.getService(context, 1, dodServiceIntent,
                PendingIntent.FLAG_NO_CREATE);
        if (pi != null) {
            pi.cancel();
            AlarmManager dodAlarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            dodAlarm.cancel(pi);
        }
    }
}
