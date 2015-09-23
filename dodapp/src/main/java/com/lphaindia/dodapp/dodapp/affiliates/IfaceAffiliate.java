package com.lphaindia.dodapp.dodapp.affiliates;

import com.lphaindia.dodapp.dodapp.Product.Product;
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
    List<Category> getCategoryUrlList();
    long getCategoryExpiry(String category);
    int getCategoryIndex(Category category);
    List<Product> getProductListFromCategory(String category);
    Product getProductById(String category, String productId);
    long getNextExpiry();
    boolean hasDataExpired();
    void populateCategoriesWithData() throws JSONException;
    void populateCategorywithData(String category) throws JSONException;
    String fetchCategoryDataInBackground();
    void removeAllCategories();
}
