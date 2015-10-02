package com.lphaindia.dodapp.dodapp.Analytics;

import android.app.Application;
import android.content.Context;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;


/**
 * Created by ajitesh.shukla on 9/7/15.
 */
public class AnalyticsHelper extends Application {

    private static final String PROPERTY_ID = "UA-68342400-1"; // My Property id.

    private Tracker mTracker;

    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

    public AnalyticsHelper() {
        super();
    }

    public synchronized Tracker getTracker(TrackerName trackerId, Context mContext) {
        if (!mTrackers.containsKey(trackerId)) {

            GoogleAnalytics analytics = GoogleAnalytics.getInstance(mContext);
            Tracker t = analytics.newTracker(PROPERTY_ID);

            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }
}
