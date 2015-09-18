package com.lphaindia.dodapp.dodapp;

import java.util.concurrent.TimeUnit;

/**
 * Created by ajitesh.shukla on 9/15/15.
 */
public final class AppConstants {
    public static final String TAG = "DodApp";
    public static final long TIME_DELAY = TimeUnit.MINUTES.toMillis(10);
    public static final int FLIPKART_CATEGORY_ID_OFFSET = 1000; //offset is inclusive
    public static final int FLIPKART_CATEGORY_ID_LIMIT = 2000; // limit is exclusive
    public static final int FLIPKART_PRODUCT_ID_OFFSET = 2000;
    public static final int FLIPKART_PRODUCT_ID_LIMIT = 5000;
    public static final int SNAPDEAL_CATEGORY_ID_OFFSET = 5000;
    public static final int SNAPDEAL_CATEGORY_ID_LIMIT = 6000;
    public static final int SNAPDEAL_PRODUCT_ID_OFFSET  = 6000;
    public static final int SNAPDEAL_PRODUCT_ID_LIMIT  = 9000;

    public static final int PRODUCT_CLUSTER_SIZE = 50;

    public static final int AFFILIATE_COLLECTION_VALUE_FLIPKART = 0;
    public static final int AFFILIATE_COLLECTION_VALUE_SNAPDEAL = 1;

    public static final int ACCESSIBILITY_MAX_RESULT_COUNT = 20;
    public static final int KEYWORD_DEPTH = 2;

    public static final String myntrapkg = "com.myntra.android ";
    public static final String flipkartpkg = "com.flipkart.android ";
    public static final String jabongpkg = "com.jabong.android ";
    public static final String snapdealpkg = "com.snapdeal.main ";
    public static final String quikrpkg = "com.quikr ";
    public static final String olxpkg = "com.olx.southasia ";
}
