package com.lphaindia.dodapp.dodapp.affiliateResources;

import android.support.v7.widget.RecyclerView;
import android.widget.Spinner;

/**
 * Created by ajitesh.shukla on 9/17/15.
 */
public class AffiliateActivityResources {
    public Spinner spinner;
    public Spinner discountSpinner;
    public RecyclerView recyclerView;

    public AffiliateActivityResources(Spinner spinner, Spinner discountSpinner, RecyclerView recyclerView) {
        this.spinner = spinner;
        this.discountSpinner = discountSpinner;
        this.recyclerView = recyclerView;
    }
}
