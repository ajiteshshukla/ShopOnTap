package com.lphaindia.dodapp.dodapp;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.lphaindia.dodapp.dodapp.affiliates.Flipkart;
import com.lphaindia.dodapp.dodapp.affiliates.Snapdeal;
import com.lphaindia.dodapp.dodapp.injectors.Injectors;
import com.lphaindia.dodapp.dodapp.intentScheduler.Scheduler;
import org.json.JSONException;

import javax.inject.Inject;

/**
 * Created by ajitesh.shukla on 9/13/15.
 */
public class DodIntentService extends IntentService {

    @Inject
    Flipkart flipkart;

    @Inject
    Snapdeal snapdeal;

    @Inject
    Scheduler scheduler;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DodIntentService() {
        super(AppConstants.TAG);
        Injectors.initialize(this);
        Injectors.serviceInjector.injectDodIntentService(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(AppConstants.TAG, "Intent Handler initiated");
        //invoked on boot completed....fetch data from the flipkart affiliate-api
        Log.d(AppConstants.TAG, "boot completed initiate JSON fetch if possible");
                //check if some data has expired or is about to expire
        try {
            checkForAffiliates();
            populateCategoryUrlLists();
            scheduler.cancelNextSchedule();
            scheduler.schedule(getNextExpiry());
        } catch (Exception e) {
            scheduler.cancelNextSchedule();
            scheduler.schedule(0);
            e.printStackTrace();
        }
    }

    public void checkForAffiliates() throws JSONException {
        if(snapdeal.hasDataExpired())
            snapdeal.pushCategoryUrlList();
        if(flipkart.hasDataExpired())
            flipkart.pushCategoryUrlList();
    }

    public void populateCategoryUrlLists() {
        flipkart.getCategoryUrlList();
        snapdeal.getCategoryUrlList();
    }

    public long getNextExpiry() {
        long expiryFlipkart = flipkart.getNextExpiry();
        long expirySnapdeal = snapdeal.getNextExpiry();
        return expiryFlipkart < expirySnapdeal ? expiryFlipkart : expirySnapdeal;
    }
}
