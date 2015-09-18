package com.lphaindia.dodapp.dodapp.injectors;

import android.content.Context;
import android.util.Log;
import com.lphaindia.dodapp.dodapp.AppConstants;
import com.lphaindia.dodapp.dodapp.DodIntentService;
import com.lphaindia.dodapp.dodapp.affiliateAdapters.FlipkartJsonAdapter;
import com.lphaindia.dodapp.dodapp.affiliateAdapters.SnapdealJsonAdapter;
import com.lphaindia.dodapp.dodapp.affiliateCategories.FlipkartAffiliateCategory;
import com.lphaindia.dodapp.dodapp.affiliateCategories.SnapdealAffiliateCategory;
import com.lphaindia.dodapp.dodapp.affiliates.AffiliateCollection;
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
    private Flipkart flipkart;
    private Snapdeal snapdeal;
    private DodDbHelper dodDbHelper;
    private Scheduler scheduler;
    private AffiliateCollection affiliateCollection;

    public DodInjectorModule(Context context) {
        try {
            this.context = context;
            dodDbHelper = new DodDbHelper(context, null, null, 1);
            flipkartJsonAdapter = new FlipkartJsonAdapter(dodDbHelper);
            snapdealJsonAdapter = new SnapdealJsonAdapter(dodDbHelper);
            List<SnapdealAffiliateCategory> snapdealAffiliateCategoryList = new ArrayList<SnapdealAffiliateCategory>();
            List<FlipkartAffiliateCategory> flipkartAffiliateCategoryList = new ArrayList<FlipkartAffiliateCategory>();
            flipkart = new Flipkart(flipkartJsonAdapter, flipkartAffiliateCategoryList);
            snapdeal = new Snapdeal(snapdealJsonAdapter, snapdealAffiliateCategoryList);
            scheduler = new Scheduler(context);
            affiliateCollection = new AffiliateCollection(flipkart, snapdeal);

        } catch (Exception e) {
            Log.d(AppConstants.TAG, e.getMessage());
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
