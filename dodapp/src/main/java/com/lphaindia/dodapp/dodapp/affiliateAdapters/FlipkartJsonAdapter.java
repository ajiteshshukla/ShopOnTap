package com.lphaindia.dodapp.dodapp.affiliateAdapters;

import android.util.Log;
import com.lphaindia.dodapp.dodapp.AppConstants;
import com.lphaindia.dodapp.dodapp.affiliateCategories.Category;
import com.lphaindia.dodapp.dodapp.data.DodDbHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by ajitesh.shukla on 9/15/15.
 */
public class FlipkartJsonAdapter implements IfaceAffiliateAdapter{

    private DodDbHelper dodDbHelper;

    public FlipkartJsonAdapter(DodDbHelper dodDbHelper) {
        this.dodDbHelper = dodDbHelper;
    }

    @Override
    public void insertJsonToDbHelper(JSONObject jsonObject) throws JSONException {

        JSONObject jApiGroups = jsonObject.getJSONObject("apiGroups");
        JSONObject jAffiliate = jApiGroups.getJSONObject("affiliate");
        JSONObject jApiListings = jAffiliate.getJSONObject("apiListings");

        for(int i=0; i<jApiListings.names().length(); i++) {
            JSONObject jCategoryObject = jApiListings.getJSONObject(jApiListings.names().getString(i));
            JSONObject jProductObject = jCategoryObject.getJSONObject(jCategoryObject.names().getString(0));
            JSONObject jVersionObject = jProductObject.getJSONObject(jProductObject.names().getString(0));

            String categoryName = jVersionObject.getString("resourceName");
            String categoryUrl = jVersionObject.getString("get");
            String expiry = categoryUrl.substring(categoryUrl.indexOf("expiresAt=") + 10,
                    categoryUrl.indexOf("&sig"));
            Log.d(AppConstants.TAG, "Category: " + categoryName);
            Log.d(AppConstants.TAG, "Url: " + categoryUrl);
            Log.d(AppConstants.TAG, "expiry: " + expiry);

            dodDbHelper.insertOrUpdateCategories(categoryName, categoryUrl, expiry, DodDbHelper.TABLE_FLIPKART);
        }
    }

    @Override
    public void removeCompleteList() {
        dodDbHelper.removeAllCategories(DodDbHelper.TABLE_FLIPKART);
    }

    @Override
    public long getNextExpiryFromDbHelper() {
        return dodDbHelper.getNextExpiry(DodDbHelper.TABLE_FLIPKART);
    }

    @Override
    public boolean hasDataInDbExpired() {
        return dodDbHelper.hasDataExpired(DodDbHelper.TABLE_FLIPKART);
    }

    @Override
    public List<Category> getCategoryList() {
        return dodDbHelper.getCategoryList(DodDbHelper.TABLE_FLIPKART);
    }
}
