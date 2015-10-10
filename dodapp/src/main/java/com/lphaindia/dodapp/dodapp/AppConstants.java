package com.lphaindia.dodapp.dodapp;

import java.util.concurrent.TimeUnit;

/**
 * Created by ajitesh.shukla on 9/15/15.
 */
public final class AppConstants {
    public static final String TAG = "DodApp";
    public static final long TIME_DELAY = TimeUnit.MINUTES.toMillis(10);
    public static final String KEY_CATEGORY = "category";
    public static final String REQUEST_CATEGORY = "categorylist";
    public static final String REQUEST_PRODUCTS = "productlist";
    public static final String REQUEST_SEARCH = "keywordsearch";
    public static final String REQUEST_URL = "http://192.168.1.2:8080/Tap/ShopOnTap";

    public static final int KEYWORD_DEPTH = 3;

    public static final String myntrapkg = "com.myntra.android ";
    public static final String flipkartpkg = "com.flipkart.android ";
    public static final String jabongpkg = "com.jabong.android ";
    public static final String snapdealpkg = "com.snapdeal.main ";
    public static final String quikrpkg = "com.quikr ";
    public static final String olxpkg = "com.olx.southasia ";
}
