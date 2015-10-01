package com.lphaindia.dodapp.dodapp.affiliates;

import android.util.Log;
import com.lphaindia.dodapp.dodapp.AppConstants;
import com.lphaindia.dodapp.dodapp.Product.Product;
import com.lphaindia.dodapp.dodapp.affiliateAdapters.FlipkartJsonAdapter;
import com.lphaindia.dodapp.dodapp.affiliateCategories.Category;
import com.lphaindia.dodapp.dodapp.affiliateCategories.FlipkartAffiliateCategory;
import com.lphaindia.dodapp.dodapp.network.NetworkTask;
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
import java.util.Calendar;
import java.util.List;

/**
 * Created by ajitesh.shukla on 9/14/15.
 */
public class Flipkart implements IfaceAffiliate {

    private FlipkartJsonAdapter flipkartJsonAdapter;
    public List<FlipkartAffiliateCategory> flipkartAffiliateCategories;

    public Flipkart(FlipkartJsonAdapter flipkartJsonAdapter, List<FlipkartAffiliateCategory> flipkartAffiliateCategories) {
        this.flipkartJsonAdapter = flipkartJsonAdapter;
        this.flipkartAffiliateCategories = flipkartAffiliateCategories;
    }

    @Override
    public void pushCategoryList() {
        String responseString = fetchCategoryListInBackground();
        if(responseString != null) {
            try {
                JSONObject responseJson = new JSONObject(responseString);
                flipkartJsonAdapter.insertJsonToDbHelper(responseJson);
            } catch (Exception e) {
                Log.d(AppConstants.TAG, responseString);
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Category> getCategoryList() {
        List<Category> categoryList = flipkartJsonAdapter.getCategoryList();
        for (int i = 0; i < categoryList.size(); i++) {
            int index = getCategoryIndex(categoryList.get(i));
            if(index != -1) {
                Log.d(AppConstants.TAG, "no need to delete flipkart category is in valid state");
            } else {
                flipkartAffiliateCategories.add(new FlipkartAffiliateCategory(categoryList.get(i)));
            }
        }
        return categoryList;
    }

    @Override
    public void populateCategorywithData(String category) throws JSONException {
        for(int i = 0; i < flipkartAffiliateCategories.size(); i++) {
            if(flipkartAffiliateCategories.get(i).category.getCategoryName().contains(category)) {
                flipkartAffiliateCategories.get(i).fetchProducts();
            }
        }
    }

    @Override
    public List<Product> getProductListFromCategory(String category) {
        List<Product> products = new ArrayList<Product>();
        for(int i = 0; i < flipkartAffiliateCategories.size(); i++) {
            if(flipkartAffiliateCategories.get(i).category.getCategoryName().contains(category)) {
                products = flipkartAffiliateCategories.get(i).products;
            }
        }
        return products;
    }

    @Override
    public int getCategoryIndex(Category category) {
        for (int i = 0; i < flipkartAffiliateCategories.size(); i++) {
            if (flipkartAffiliateCategories.get(i).category.getCategoryName().contains(category.getCategoryName())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String fetchCategoryListInBackground() {
        String datafromServer = null;
        NetworkTask networkTask = new NetworkTask();
        datafromServer = networkTask.fetchDataFromUrl(createFetchCategoryListUrl());
        return datafromServer;
    }

    public String createFetchCategoryListUrl() {
        return (AppConstants.REQUEST_URL + "?requesttype=" + AppConstants.REQUEST_CATEGORY + "&affiliate="
                + AppConstants.AFFILIATE_FLIPKART);
    }

    @Override
    public void removeAllCategories() {
        flipkartJsonAdapter.removeCompleteList();
    }
}
