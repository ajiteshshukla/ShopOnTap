package com.lphaindia.dodapp.dodapp.affiliates;


/**
 * Created by ajitesh.shukla on 9/17/15.
 */
public class AffiliateCollection {

    public Flipkart flipkart;
    public Snapdeal snapdeal;
    public Amazon amazon;
    public static int size = 3;

    public AffiliateCollection(Flipkart flipkart, Snapdeal snapdeal, Amazon amazon) {
        this.flipkart = flipkart;
        this.snapdeal = snapdeal;
        this.amazon = amazon;
    }
}
