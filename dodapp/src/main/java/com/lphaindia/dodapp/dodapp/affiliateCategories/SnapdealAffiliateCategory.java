package com.lphaindia.dodapp.dodapp.affiliateCategories;

import android.util.Log;
import com.lphaindia.dodapp.dodapp.AppConstants;
import com.lphaindia.dodapp.dodapp.Product.Product;
import com.lphaindia.dodapp.dodapp.affiliateCategoryAdapter.SnapdealAffiliateCategoryAdapter;
import com.lphaindia.dodapp.dodapp.affiliates.Snapdeal;
import com.lphaindia.dodapp.dodapp.network.NetworkTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajitesh.shukla on 9/15/15.
 */
public class SnapdealAffiliateCategory implements IfaceAffiliateCategory{

    public Category category;

    public long categoryProductListExpiry;

    public List<Product> products = new ArrayList<Product>();

    public SnapdealAffiliateCategory(Category category) {
        this.category = category;
    }

    public void fetchProducts() throws JSONException {
        fetchProducts(category.categoryUrl);
    }


    public void fetchProducts(String categoryUrl) throws JSONException {
        String datafromServer = null;
        NetworkTask networkTask = new NetworkTask(AppConstants.AFFILIATE_COLLECTION_VALUE_SNAPDEAL);
        datafromServer = networkTask.fetchDataFromUrl(categoryUrl);
        Log.d(AppConstants.TAG, "Product Category: " + category.categoryName);
        Log.d(AppConstants.TAG, "" + datafromServer);
        if (datafromServer != null) {
            JSONObject productJsonObject = new JSONObject(datafromServer);
            categoryProductListExpiry = Long.valueOf(productJsonObject.getString("validTill"));
            products = SnapdealAffiliateCategoryAdapter.fetchProductsFromJson(productJsonObject, category.categoryName);
        }
    }
}
