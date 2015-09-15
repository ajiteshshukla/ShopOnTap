package com.lphaindia.dodapp.dodapp.affiliates;

import com.lphaindia.dodapp.dodapp.affiliateCategories.Category;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by ajitesh.shukla on 9/14/15.
 */
public interface IfaceAffiliate {
    String getPlanUrl();
    String getTokenId();
    String getAffiliateId();
    void pushCategoryUrlList() throws JSONException;
    void getCategoryUrlList();
    long getNextExpiry();
    boolean hasDataExpired();
    void populateCategoriesWithData() throws JSONException ;
}
