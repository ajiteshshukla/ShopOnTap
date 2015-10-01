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
    void pushCategoryList() throws JSONException;
    List<Category> getCategoryList();
    int getCategoryIndex(Category category);
    List<Product> getProductListFromCategory(String category);
    void populateCategorywithData(String category) throws JSONException;
    String fetchCategoryListInBackground();
    void removeAllCategories();
}
