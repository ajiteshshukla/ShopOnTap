package com.lphaindia.dodapp.dodapp.affiliateCategories;

import android.util.Log;
import com.lphaindia.dodapp.dodapp.AppConstants;
import com.lphaindia.dodapp.dodapp.Product.Product;
import com.lphaindia.dodapp.dodapp.affiliateCategoryAdapter.FlipkartAffiliateCategoryAdapter;
import com.lphaindia.dodapp.dodapp.network.NetworkTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ajitesh.shukla on 9/15/15.
 */
public class FlipkartAffiliateCategory implements IfaceAffiliateCategory{

    public Category category;

    public long categoryProductListExpiry = 0;

    public List<Product> products = new ArrayList<Product>();

    public FlipkartAffiliateCategory(Category category) {
        this.category = category;
    }

    public void fetchProducts() throws JSONException {
        fetchProducts(category.categoryUrl);
    }


    public void fetchProducts(String categoryUrl) throws JSONException {
        String datafromServer = null;
        NetworkTask networkTask = new NetworkTask(AppConstants.AFFILIATE_COLLECTION_VALUE_FLIPKART);
        datafromServer = networkTask.fetchDataFromUrl(categoryUrl);
        //Log.d(AppConstants.TAG, "Product Category: " + category.categoryName);
        //Log.d(AppConstants.TAG, "" + datafromServer);
        JSONObject productJsonObject = new JSONObject(datafromServer);
        categoryProductListExpiry = Long.valueOf(productJsonObject.getString("validTill"));
        products = FlipkartAffiliateCategoryAdapter.fetchProductsFromJson(productJsonObject, category.categoryName);
    }
}
