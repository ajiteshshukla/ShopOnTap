package com.lphaindia.dodapp.dodapp;

import java.util.concurrent.TimeUnit;

/**
 * Created by ajitesh.shukla on 9/15/15.
 */
public final class AppConstants {
    public static final String TAG = "DodApp";
    public static final long TIME_DELAY = TimeUnit.MINUTES.toMillis(10);

    public static final String AFFILIATE_FLIPKART = "flipkart";
    public static final String AFFILIATE_SNAPDEAL = "snapdeal";
    public static final String AFFILIATE_AMAZON = "amazon";

    public static final String REQUEST_CATEGORY = "categorylist";
    public static final String REQUEST_PRODUCTS = "productlist";
    public static final String REQUEST_SEARCH = "keywordsearch";
    public static final String REQUEST_URL = "http://shopontapserver.bitnamiapp.com/Tap/ShopOnTap";

    public static final int AFFILIATE_COLLECTION_VALUE_FLIPKART = 0;
    public static final int AFFILIATE_COLLECTION_VALUE_SNAPDEAL = 1;
    public static final int AFFILIATE_COLLECTION_VALUE_AMAZON = 2;

    public static final int KEYWORD_DEPTH = 3;

    public static final String myntrapkg = "com.myntra.android ";
    public static final String flipkartpkg = "com.flipkart.android ";
    public static final String jabongpkg = "com.jabong.android ";
    public static final String snapdealpkg = "com.snapdeal.main ";
    public static final String quikrpkg = "com.quikr ";
    public static final String olxpkg = "com.olx.southasia ";
}
