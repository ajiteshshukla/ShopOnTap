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
    private static final String PLAN_URL = "http://affiliate-feeds.snapdeal.com/feed/70385.json";
    public static final String TOKEN_ID = "bf654bc1807ed6d41afe7e2ad04a40";
    public static final String AFFILIATE_ID = "70385";
    public static final String AFFILIATE_HEADER = "Snapdeal-Affiliate-Id";
    public static final String TOKEN_HEADER = "Snapdeal-Token-Id";

    private SnapdealJsonAdapter snapdealJsonAdapter;

    public List<SnapdealAffiliateCategory> snapdealAffiliateCategories;

    public Snapdeal(SnapdealJsonAdapter snapdealJsonAdapter, List<SnapdealAffiliateCategory> snapdealAffiliateCategories) {
        this.snapdealJsonAdapter = snapdealJsonAdapter;
        this.snapdealAffiliateCategories = snapdealAffiliateCategories;
    }

    @Override
    public String getPlanUrl() {
        return PLAN_URL;
    }

    @Override
    public String getTokenId() {
        return TOKEN_ID;
    }

    @Override
    public String getAffiliateId() {
        return AFFILIATE_ID;
    }

    @Override
    public void pushCategoryUrlList() throws JSONException {
        String responseString = fetchCategoryDataInBackground();
        JSONObject responseJson = new JSONObject(responseString);
        snapdealJsonAdapter.insertJsonToDbHelper(responseJson);
    }

    @Override
    public List<Category> getCategoryUrlList() {
        List<Category> categoryUrlList = snapdealJsonAdapter.getCategoryList();
        for(int i = 0; i < categoryUrlList.size(); i++) {
            int index = getCategoryIndex(categoryUrlList.get(i).categoryName);
            if(index != -1) {
                if(snapdealAffiliateCategories.get(index).categoryProductListExpiry < Calendar
                        .getInstance().getTimeInMillis()) {
                    snapdealAffiliateCategories.remove(index);
                    snapdealAffiliateCategories.add(new SnapdealAffiliateCategory(categoryUrlList.get(i)));
                } else {
                    Log.d(AppConstants.TAG, "no need to delete category is in valid state");
                }
            } else {
                snapdealAffiliateCategories.add(new SnapdealAffiliateCategory(categoryUrlList.get(i)));
            }
        }
        return categoryUrlList;
    }

    @Override
    public long getCategoryExpiry(String category) {
        for(int i = 0; i < snapdealAffiliateCategories.size(); i++) {
            if(snapdealAffiliateCategories.get(i).category.categoryName.contains(category)) {
                return snapdealAffiliateCategories.get(i).categoryProductListExpiry;
            }
        }
        return 0;
    }

    @Override
    public int getCategoryIndex(String category) {
        for(int i = 0; i < snapdealAffiliateCategories.size(); i++) {
            if(snapdealAffiliateCategories.get(i).category.categoryName.contains(category)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void populateCategoriesWithData() throws JSONException {
        for(int i = 0; i < snapdealAffiliateCategories.size(); i++) {
            snapdealAffiliateCategories.get(i).fetchProducts();
        }
    }

    @Override
    public void populateCategorywithData(String category) throws JSONException {
        for(int i = 0; i < snapdealAffiliateCategories.size(); i++) {
            if(snapdealAffiliateCategories.get(i).category.categoryName.contains(category)) {
                snapdealAffiliateCategories.get(i).fetchProducts();
            }
        }
    }

    @Override
    public List<Product> getProductListFromCategory(String category) {
        List<Product> products = new ArrayList<Product>();
        for(int i = 0; i < snapdealAffiliateCategories.size(); i++) {
            if(snapdealAffiliateCategories.get(i).category.categoryName.contains(category)) {
                products = snapdealAffiliateCategories.get(i).products;
            }
        }
        return products;
    }

    @Override
    public Product getProductById(String category, String productId) {
        Product product = null;
        for(int i = 0; i < snapdealAffiliateCategories.size(); i++) {
            if(snapdealAffiliateCategories.get(i).category.categoryName.contains(category)) {
                List<Product> products = snapdealAffiliateCategories.get(i).products;
                for(int j = 0; j < products.size(); j++) {
                    if(products.get(j).productId.contains(productId)) {
                        product = products.get(j);
                    }
                }
            }
        }
        return product;
    }

    @Override
    public long getNextExpiry() {
        return snapdealJsonAdapter.getNextExpiryFromDbHelper();
    }

    @Override
    public boolean hasDataExpired() {
        return snapdealJsonAdapter.hasDataInDbExpired();
    }

    @Override
    public String fetchCategoryDataInBackground() {
        String datafromServer;
        NetworkTask networkTask = new NetworkTask(AppConstants.AFFILIATE_COLLECTION_VALUE_SNAPDEAL);
        datafromServer = networkTask.fetchDataFromUrl(PLAN_URL);
        return datafromServer;
    }

    @Override
    public void removeAllCategories() {
        snapdealJsonAdapter.removeCompleteList();
    }
}
