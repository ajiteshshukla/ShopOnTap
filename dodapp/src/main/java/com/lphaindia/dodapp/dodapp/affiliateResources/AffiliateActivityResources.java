package com.lphaindia.dodapp.dodapp.affiliateResources;

import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * Created by ajitesh.shukla on 9/17/15.
 */
public class AffiliateActivityResources {
    public Button backToCategoryButton;
    public LinearLayout linearLayout;
    public HorizontalScrollView horizontalScrollView;

    public AffiliateActivityResources(Button backToCategoryButton, LinearLayout linearLayout,
                                      HorizontalScrollView  horizontalScrollView) {
        this.backToCategoryButton = backToCategoryButton;
        this.linearLayout = linearLayout;
        this.horizontalScrollView = horizontalScrollView;
    }
}
