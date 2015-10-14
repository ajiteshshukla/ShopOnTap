package com.lphaindia.dodapp.dodapp.accessibilityFeatures;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.google.android.gms.analytics.Tracker;
import com.lphaindia.dodapp.dodapp.Analytics.AnalyticsHelper;
import com.lphaindia.dodapp.dodapp.overlays.CarouselOverlay;
import com.lphaindia.dodapp.dodapp.overlays.FullScreenOverlay;
import com.lphaindia.dodapp.dodapp.overlays.IconOverlay;
import com.lphaindia.dodapp.dodapp.overlays.LoadingOverlay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajitesh.shukla on 9/17/15.
 */
public class TapAccessibilityService extends AccessibilityService {

    public static String pkgName = null;
    public static String whiteListedPkgNames = "com.quikr com.olx.southasia com.snapdeal.main com.flipkart.android " +
            "com.myntra.android com.jabong.android ";

    private AnalyticsHelper analyticsHelper = new AnalyticsHelper();
    public static Tracker mTracker;

    public static Context mContext;

    public static List<String> activityDataList = new ArrayList<String>();

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        //if a new view is clicked or the window state is changed clear the old list
        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED ||
                event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            //Log.d(AppConstants.TAG, event.getClassName().toString());
            updateOverlayOnWindowChange(event);
        }

        //if the window content is updated or window is updated or the view is scrolled collect data
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED ||
                event.getEventType() == AccessibilityEvent.TYPE_WINDOWS_CHANGED ||
                event.getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED ||
                event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            fetchScreenData(event);
        }
    }

    public void updateOverlayOnWindowChange(AccessibilityEvent event) {
        if (event.getPackageName().toString().contains(mContext.getPackageName())) {
            if (LoadingOverlay.getInstance(mContext).isOverlayShown()) {
                LoadingOverlay.getInstance(mContext).removeOverlay();
            }
            return;
        }
        clearList();
        //Log.d(AppConstants.TAG, String.valueOf(event.getPackageName()));
        pkgName = String.valueOf(event.getPackageName());
        if(pkgName != null && pkgName != "" && whiteListedPkgNames.contains(pkgName)) {
            //We will get that pullable overlay only on the whitelisted packages this way
            IconOverlay.getInstance(mContext).showOverlay();
            if (LoadingOverlay.getInstance(mContext).isOverlayShown()) {
                LoadingOverlay.getInstance(mContext).removeOverlay();
            }
        } else {
            IconOverlay.getInstance(mContext).removeOverlay();
            if (LoadingOverlay.getInstance(mContext).isOverlayShown()) {
                LoadingOverlay.getInstance(mContext).removeOverlay();
            }
            if (CarouselOverlay.getInstance(mContext).isOverlayShown()) {
                CarouselOverlay.getInstance(mContext).removeOverlay();
            }
            if (FullScreenOverlay.getInstance(mContext).isOverlayShown()) {
                FullScreenOverlay.getInstance(mContext).removeOverlay();
            }
        }
    }

    public void fetchScreenData(AccessibilityEvent event) {
        AccessibilityNodeInfo nodeInfo = event.getSource();
        if(nodeInfo != null) {
            nodeInfo = getRoot(nodeInfo);
            //collect data only if the app in front is white listed
            if(nodeInfo.getPackageName() != null
                    && nodeInfo.getPackageName() != ""
                    && whiteListedPkgNames.contains(nodeInfo.getPackageName())) {
                pkgName = nodeInfo.getPackageName().toString();
                clearList();
                fetchCompleteHeirarchy(nodeInfo);
            }
        }
    }

    //utility function for fetching screen data using dfs on root node
    public void fetchCompleteHeirarchy(AccessibilityNodeInfo nodeInfo) {
        if(nodeInfo == null) {
            return;
        }
        if(nodeInfo.getChildCount() == 0 && nodeInfo.getText() != null) {
            //Log.d(tag, "info " + nodeInfo.getText());
            activityDataList.add(nodeInfo.getText().toString());
            return;
        }
        for(int i=0;i<nodeInfo.getChildCount(); i++)
        {
            fetchCompleteHeirarchy(nodeInfo.getChild(i));
        }
        if(nodeInfo!= null && nodeInfo.getText() != null)
        {
            activityDataList.add(nodeInfo.getText().toString());
            //Log.d(tag, "info " + nodeInfo.getText());
        }
    }

    //utility function for fetching the root container of the view in focus
    public AccessibilityNodeInfo getRoot(AccessibilityNodeInfo nodeInfo) {
        while(nodeInfo.getParent() != null) {
            nodeInfo = nodeInfo.getParent();
        }
        return nodeInfo;
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            boolean status;
            if(FullScreenOverlay.getInstance(mContext).isOverlayShown()){
                return FullScreenOverlay.getInstance(mContext).removeOverlay();
            }
            if(CarouselOverlay.getInstance(mContext).isOverlayShown()) {
                status =  CarouselOverlay.getInstance(mContext).removeOverlay();
                IconOverlay.getInstance(mContext).showOverlay();
                return  status;
            }
            if(LoadingOverlay.getInstance(mContext).isOverlayShown()) {
                status =  LoadingOverlay.getInstance(mContext).removeOverlay();
                IconOverlay.getInstance(mContext).showOverlay();
                return  status;
            }
        }
        return super.onKeyEvent(event);
    }

    //function to clear the keyword list
    public void clearList() {
        activityDataList.clear();
    }

    public static boolean isPackageWhiteListed() {
        return whiteListedPkgNames.contains(pkgName);
    }

    @Override
    protected void onServiceConnected() {
        //Log.d(AppConstants.TAG, " ServiceConnected");
        mContext = this;

        mTracker = analyticsHelper.getTracker(AnalyticsHelper.TrackerName.APP_TRACKER, mContext);
        //send to analytics - acessibility enabled
        TapAnalytics.sendAnalyticsAccessibilityEnabled(mTracker);

        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.notificationTimeout = 100;
        info.feedbackType = AccessibilityEvent.TYPES_ALL_MASK;
        info.flags = AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS
                | AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
                | AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;
        setServiceInfo(info);
    }

    @Override
    public void onInterrupt() {

    }
}
