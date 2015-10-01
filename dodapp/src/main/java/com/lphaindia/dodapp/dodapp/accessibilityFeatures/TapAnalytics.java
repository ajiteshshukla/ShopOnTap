package com.lphaindia.dodapp.dodapp.accessibilityFeatures;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by ajitesh.shukla on 9/8/15.
 */
public class TapAnalytics {

    private final static String ACTION_CATEGORY = "Action";
    private final static String ACTION_ACCESSIBILITY_ENABLED = "Accessibility Enabled";
    private final static String ACTION_SWIPE_ON_ICON = "Icon Swiped";
    private final static String ACTION_NULL_LIST = "Null List on Fetch";
    private final static String BUY_PRODUCT = "Buy Product";
    //private final static String CAROUSEL_DISPLAYED = "Carousel Displayed";

    public static void sendAnalyticsAccessibilityEnabled(Tracker mTracker) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(ACTION_CATEGORY)
                .setAction(ACTION_ACCESSIBILITY_ENABLED)
                .build());
    }

    public static void sendAnalyticsSwipeOnIcon(Tracker mTracker) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(ACTION_CATEGORY)
                .setAction(ACTION_SWIPE_ON_ICON)
                .build());
    }

    public static void sendAnalyticsNullList(Tracker mTracker) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(ACTION_CATEGORY)
                .setAction(ACTION_NULL_LIST)
                .build());
    }

    public static void sendAnalyticsBuyProduct(Tracker mTracker) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(ACTION_CATEGORY)
                .setAction(BUY_PRODUCT)
                .build());
    }

    /*public static void sendAnalyticsCarouselDisplayed(Tracker mTracker) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(ACTION_CATEGORY)
                .setAction(CAROUSEL_DISPLAYED)
                .build());
    }*/
}
