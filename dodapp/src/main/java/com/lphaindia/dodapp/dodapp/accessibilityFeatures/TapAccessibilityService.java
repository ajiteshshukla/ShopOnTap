package com.lphaindia.dodapp.dodapp.accessibilityFeatures;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.analytics.Tracker;
import com.lphaindia.dodapp.dodapp.Analytics.AnalyticsHelper;
import com.lphaindia.dodapp.dodapp.AppConstants;
import com.lphaindia.dodapp.dodapp.overlays.CarouselOverlay;
import com.lphaindia.dodapp.dodapp.overlays.IconOverlay;
import com.lphaindia.dodapp.dodapp.overlays.LoadingOverlay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajitesh.shukla on 9/17/15.
 */
public class TapAccessibilityService extends AccessibilityService {

    public static String pkgName = null;
    public static String className = null;
    public static String contentDescription = null;
    public static String text = null;

    public static List<String> whiteListedPkgNames = new ArrayList<String>();
    public static List<String> whiteListedClassNames = new ArrayList<String>();
    public static String whiteListedText = null;

    private AnalyticsHelper analyticsHelper = new AnalyticsHelper();
    public static Tracker mTracker;

    public static Context mContext;

    public static List<String> activityDataList = new ArrayList<String>();

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        //Check API level
        if(Build.VERSION.SDK_INT < 18) {
            //Log.d("dodapp ", "build version: " + Build.VERSION.SDK_INT );
            //Log.d("dodapp", " " + event.getPackageName());
            return;
        } else if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                return;
            }
        }

        //if a new view is clicked or the window state is changed clear the old list
        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED ||
                event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            //Log.d("dodapp", " " + event.getPackageName());
            //Log.d(AppConstants.TAG, " " + event.getContentDescription());
            //Log.d(AppConstants.TAG, " " + event.toString());
            updateOverlayOnWindowChange(event);
        }

        //if the window content is updated or window is updated or the view is scrolled collect data
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED ||
                event.getEventType() == AccessibilityEvent.TYPE_WINDOWS_CHANGED ||
                event.getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED ||
                event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            fetchScreenData(event);
            //Log.d(AppConstants.TAG, " " + activityDataList.toString());
        }
    }

    public void updateOverlayOnWindowChange(AccessibilityEvent event) {
        if (event.getPackageName() == null) {
            IconOverlay.getInstance(mContext).removeOverlay();
            if (LoadingOverlay.getInstance(mContext).isOverlayShown()) {
                LoadingOverlay.getInstance(mContext).removeOverlay();
            }
            if (CarouselOverlay.getInstance(mContext).isOverlayShown()) {
                CarouselOverlay.getInstance(mContext).removeOverlay();
            }
            return;
        }

        if (event.getPackageName().toString().contains(mContext.getPackageName())) {
            if (LoadingOverlay.getInstance(mContext).isOverlayShown()) {
                LoadingOverlay.getInstance(mContext).removeOverlay();
            }
            return;
        }
        clearList();
        //Log.d(AppConstants.TAG, String.valueOf(event.getPackageName()));
        pkgName = String.valueOf(event.getPackageName());
        className = String.valueOf(event.getClassName());
        contentDescription = String.valueOf(event.getContentDescription());
        text = String.valueOf(event.getText());

        boolean matchPackage = (pkgName != null && pkgName != "" && whiteListedPkgNames.contains(pkgName));
        boolean matchClassName = (className != null && className != "" && whiteListedClassNames.contains(className));
        boolean matchContentDescription = (contentDescription != null && contentDescription != ""
                && whiteListedClassNames.contains(contentDescription));
        boolean matchText = (text != null && text != "" && whiteListedText.contains(text));

        //if(pkgName != null && pkgName != "" && whiteListedPkgNames.contains(pkgName)) {
            //We will get that pullable overlay only on the whitelisted packages this way
        if(matchPackage && (matchClassName || matchContentDescription || matchText)) {
            IconOverlay.getInstance(mContext).showOverlay();
            //Log.d("dodapp", pkgName);
            //Log.d("dodapp", "from position 3");
            if (LoadingOverlay.getInstance(mContext).isOverlayShown()) {
                LoadingOverlay.getInstance(mContext).removeOverlay();
            }
            if (CarouselOverlay.getInstance(mContext).isOverlayShown()) {
                CarouselOverlay.getInstance(mContext).removeOverlay();
            }
        } else {
            IconOverlay.getInstance(mContext).removeOverlay();
            if (LoadingOverlay.getInstance(mContext).isOverlayShown()) {
                LoadingOverlay.getInstance(mContext).removeOverlay();
            }
            if (CarouselOverlay.getInstance(mContext).isOverlayShown()) {
                CarouselOverlay.getInstance(mContext).removeOverlay();
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
            //nodeInfo.getContentDescription().toString();
        }
    }

    //utility function for fetching the root container of the view in focus
    public AccessibilityNodeInfo getRoot(AccessibilityNodeInfo nodeInfo) {
        while(nodeInfo.getParent() != null) {
            nodeInfo = nodeInfo.getParent();
        }
        return nodeInfo;
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

        whiteListedPkgNames.add("com.quikr");
        whiteListedPkgNames.add("com.olx.southasia");
        whiteListedPkgNames.add("com.snapdeal.main");
        whiteListedPkgNames.add("com.flipkart.android");
        whiteListedPkgNames.add("com.myntra.android");
        whiteListedPkgNames.add("com.jabong.android");
        whiteListedPkgNames.add("in.amazon.mShop.android.shopping");

        whiteListedClassNames.add("com.jabong.android.view.activity.ProductDetailsActivity");
        whiteListedClassNames.add("com.myntra.android.activities.PDPActivity");
        whiteListedClassNames.add("com.amazon.mShop.details.web.WebProductDetailsActivity");
        whiteListedClassNames.add("product_grid");
        whiteListedClassNames.add("pl.tablica2.activities.AdActivity");
        whiteListedClassNames.add("com.quikr.ui.vapv2.VAPActivity");

        whiteListedText = "% OFF,";

        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.notificationTimeout = 100;
        info.feedbackType = AccessibilityEvent.TYPES_ALL_MASK;
        info.flags = AccessibilityServiceInfo.CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT
                | AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
                | AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;
        setServiceInfo(info);
        if(AppConstants.isFrescoInitialized == false){
            Fresco.initialize(this);
            AppConstants.isFrescoInitialized = true;
        }
    }

    @Override
    public void onInterrupt() {

    }
}
