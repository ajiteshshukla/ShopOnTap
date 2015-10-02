package com.lphaindia.dodapp.dodapp.affiliates;


/**
 * Created by ajitesh.shukla on 9/17/15.
 */
public class AffiliateCollection {

    public Flipkart flipkart;
    public Snapdeal snapdeal;
    public static int size = 2;

    public AffiliateCollection(Flipkart flipkart, Snapdeal snapdeal) {
        this.flipkart = flipkart;
        this.snapdeal = snapdeal;
    }
}
