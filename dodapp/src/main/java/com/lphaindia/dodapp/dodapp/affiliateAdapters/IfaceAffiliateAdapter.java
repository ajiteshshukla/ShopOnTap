package com.lphaindia.dodapp.dodapp.affiliateAdapters;

import com.lphaindia.dodapp.dodapp.affiliateCategories.Category;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by ajitesh.shukla on 9/15/15.
 */
public interface IfaceAffiliateAdapter {
    void insertJsonToDbHelper(JSONObject jsonObject) throws JSONException;
    long getNextExpiryFromDbHelper();
    boolean hasDataInDbExpired();
    public List<Category> getCategoryList();
}
