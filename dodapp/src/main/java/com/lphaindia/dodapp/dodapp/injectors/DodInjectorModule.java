package com.lphaindia.dodapp.dodapp.injectors;

import android.content.Context;
import com.lphaindia.dodapp.dodapp.AppConstants;
import com.lphaindia.dodapp.dodapp.affiliateAdapters.AmazonJsonAdapter;
import com.lphaindia.dodapp.dodapp.affiliateAdapters.FlipkartJsonAdapter;
import com.lphaindia.dodapp.dodapp.affiliateAdapters.SnapdealJsonAdapter;
import com.lphaindia.dodapp.dodapp.affiliateCategories.AmazonAffiliateCategory;
import com.lphaindia.dodapp.dodapp.affiliateCategories.FlipkartAffiliateCategory;
import com.lphaindia.dodapp.dodapp.affiliateCategories.SnapdealAffiliateCategory;
import com.lphaindia.dodapp.dodapp.affiliates.AffiliateCollection;
import com.lphaindia.dodapp.dodapp.affiliates.Amazon;
import com.lphaindia.dodapp.dodapp.affiliates.Flipkart;
import com.lphaindia.dodapp.dodapp.affiliates.Snapdeal;
import com.lphaindia.dodapp.dodapp.data.DodDbHelper;
import com.lphaindia.dodapp.dodapp.intentScheduler.Scheduler;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajitesh.shukla on 9/15/15.
 */
@Module
public final class DodInjectorModule {

    private Context context;
    private FlipkartJsonAdapter flipkartJsonAdapter;
    private SnapdealJsonAdapter snapdealJsonAdapter;
    private AmazonJsonAdapter amazonJsonAdapter;
    private Flipkart flipkart;
    private Snapdeal snapdeal;
    private Amazon amazon;
    private DodDbHelper dodDbHelper;
    private Scheduler scheduler;
    private AffiliateCollection affiliateCollection;

    public DodInjectorModule(Context context) {
        try {
            this.context = context;
            dodDbHelper = new DodDbHelper(context, null, null, 1);
            flipkartJsonAdapter = new FlipkartJsonAdapter(dodDbHelper);
            snapdealJsonAdapter = new SnapdealJsonAdapter(dodDbHelper);
            amazonJsonAdapter = new AmazonJsonAdapter(dodDbHelper);
            List<SnapdealAffiliateCategory> snapdealAffiliateCategoryList = new ArrayList<SnapdealAffiliateCategory>();
            List<FlipkartAffiliateCategory> flipkartAffiliateCategoryList = new ArrayList<FlipkartAffiliateCategory>();
            List<AmazonAffiliateCategory> amazonAffiliateCategoryList = new ArrayList<AmazonAffiliateCategory>();
            flipkart = new Flipkart(flipkartJsonAdapter, flipkartAffiliateCategoryList);
            snapdeal = new Snapdeal(snapdealJsonAdapter, snapdealAffiliateCategoryList);
            amazon = new Amazon(amazonJsonAdapter, amazonAffiliateCategoryList);
            scheduler = new Scheduler(context);
            affiliateCollection = new AffiliateCollection(flipkart, snapdeal, amazon);

        } catch (Exception e) {
            //Log.d(AppConstants.TAG, e.getMessage());
        }
    }

    @Singleton
    @Provides
    public Context providesContext() {
        return context;
    }

    @Singleton
    @Provides
    public FlipkartJsonAdapter providesFlipkartJsonAdapter() {
        return flipkartJsonAdapter;
    }

    @Singleton
    @Provides
    public SnapdealJsonAdapter providesSnapdealJsonAdapter() {
        return snapdealJsonAdapter;
    }

    @Singleton
    @Provides
    public AmazonJsonAdapter providesAmazonJsonAdapter() {
        return amazonJsonAdapter;
    }

    @Singleton
    @Provides
    public Snapdeal providesSnapdeal() {
        return snapdeal;
    }

    @Singleton
    @Provides
    public Flipkart providesFlipkart() {
        return flipkart;
    }

    @Singleton
    @Provides
    public Amazon providesAmazon() {
        return amazon;
    }

    @Singleton
    @Provides
    public DodDbHelper providesDodDbHelper() {
        return dodDbHelper;
    }

    @Singleton
    @Provides
    public Scheduler providesScheduler() {
        return scheduler;
    }

    @Singleton
    @Provides
    public AffiliateCollection providesAffiliateCollection() {
        return affiliateCollection;
    }
}
