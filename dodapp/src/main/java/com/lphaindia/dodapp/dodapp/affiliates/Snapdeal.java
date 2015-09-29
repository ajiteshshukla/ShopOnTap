package com.lphaindia.dodapp.dodapp.affiliates;

import android.util.Log;
import com.lphaindia.dodapp.dodapp.AppConstants;
import com.lphaindia.dodapp.dodapp.Product.Product;
import com.lphaindia.dodapp.dodapp.affiliateAdapters.SnapdealJsonAdapter;
import com.lphaindia.dodapp.dodapp.affiliateCategories.Category;
import com.lphaindia.dodapp.dodapp.affiliateCategories.SnapdealAffiliateCategory;
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
public class Snapdeal implements IfaceAffiliate {

    private SnapdealJsonAdapter snapdealJsonAdapter;
    public List<SnapdealAffiliateCategory> snapdealAffiliateCategories;

    public Snapdeal(SnapdealJsonAdapter snapdealJsonAdapter, List<SnapdealAffiliateCategory> snapdealAffiliateCategories) {
        this.snapdealJsonAdapter = snapdealJsonAdapter;
        this.snapdealAffiliateCategories = snapdealAffiliateCategories;
    }

    @Override
    public void pushCategoryList() {
        String responseString = fetchCategoryListInBackground();
        if(responseString != null) {
            try {
                JSONObject responseJson = new JSONObject(responseString);
                snapdealJsonAdapter.insertJsonToDbHelper(responseJson);
            } catch (Exception e) {
                Log.d(AppConstants.TAG, responseString);
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Category> getCategoryList() {
        List<Category> categoryList = snapdealJsonAdapter.getCategoryList();
        for (int i = 0; i < categoryList.size(); i++) {
            int index = getCategoryIndex(categoryList.get(i));
            if(index != -1) {
                Log.d(AppConstants.TAG, "no need to delete flipkart category is in valid state");
            } else {
                snapdealAffiliateCategories.add(new SnapdealAffiliateCategory(categoryList.get(i)));
            }
        }
        return categoryList;
    }

    @Override
    public void populateCategorywithData(String category) throws JSONException {
        for(int i = 0; i < snapdealAffiliateCategories.size(); i++) {
            if(snapdealAffiliateCategories.get(i).category.getCategoryName().contains(category)) {
                snapdealAffiliateCategories.get(i).fetchProducts();
            }
        }
    }

    @Override
    public List<Product> getProductListFromCategory(String category) {
        List<Product> products = new ArrayList<Product>();
        for(int i = 0; i < snapdealAffiliateCategories.size(); i++) {
            if(snapdealAffiliateCategories.get(i).category.getCategoryName().contains(category)) {
                products = snapdealAffiliateCategories.get(i).products;
            }
        }
        return products;
    }

    @Override
    public int getCategoryIndex(Category category) {
        for (int i = 0; i < snapdealAffiliateCategories.size(); i++) {
            if (snapdealAffiliateCategories.get(i).category.getCategoryName().contains(category.getCategoryName())) {
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
                + AppConstants.AFFILIATE_SNAPDEAL);
    }

    @Override
    public void removeAllCategories() {
        snapdealJsonAdapter.removeCompleteList();
    }
}
