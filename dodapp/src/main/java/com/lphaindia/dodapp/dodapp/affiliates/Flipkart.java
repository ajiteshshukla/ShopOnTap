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

    private static final String PLAN_URL = "https://affiliate-api.flipkart.net/affiliate/api/ajiteshsh.json";
    public static final String TOKEN_ID = "65d9f08946454a9e871d6ee07ecaa63d";
    public static final String AFFILIATE_ID = "ajiteshsh";
    public static final String AFFILIATE_HEADER = "Fk-Affiliate-Id";
    public static final String TOKEN_HEADER = "Fk-Affiliate-Token";

    private FlipkartJsonAdapter flipkartJsonAdapter;
    public List<FlipkartAffiliateCategory> flipkartAffiliateCategories;

    public Flipkart(FlipkartJsonAdapter flipkartJsonAdapter, List<FlipkartAffiliateCategory> flipkartAffiliateCategories) {
        this.flipkartJsonAdapter = flipkartJsonAdapter;
        this.flipkartAffiliateCategories = flipkartAffiliateCategories;
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
        flipkartJsonAdapter.insertJsonToDbHelper(responseJson);
    }

    @Override
    public List<Category> getCategoryUrlList() {
        List<Category> categoryUrlList = flipkartJsonAdapter.getCategoryList();
        for (int i = 0; i < categoryUrlList.size(); i++) {
            int index = getCategoryIndex(categoryUrlList.get(i).categoryName);
            if(index != -1) {
                if(flipkartAffiliateCategories.get(index).categoryProductListExpiry < Calendar
                        .getInstance().getTimeInMillis()) {
                    flipkartAffiliateCategories.remove(index);
                    flipkartAffiliateCategories.add(new FlipkartAffiliateCategory(categoryUrlList.get(i)));
                } else {
                    Log.d(AppConstants.TAG, "no need to delete category is in valid state");
                }
            } else {
                flipkartAffiliateCategories.add(new FlipkartAffiliateCategory(categoryUrlList.get(i)));
            }
        }
        return categoryUrlList;
    }

    @Override
    public void populateCategoriesWithData() throws JSONException {
        for(int i = 0; i < flipkartAffiliateCategories.size(); i++) {
            flipkartAffiliateCategories.get(i).fetchProducts();
        }
    }

    @Override
    public void populateCategorywithData(String category) throws JSONException {
        for(int i = 0; i < flipkartAffiliateCategories.size(); i++) {
            if(flipkartAffiliateCategories.get(i).category.categoryName.contains(category)) {
                flipkartAffiliateCategories.get(i).fetchProducts();
            }
        }
    }

    @Override
    public long getCategoryExpiry(String category) {
        for(int i = 0; i < flipkartAffiliateCategories.size(); i++) {
            if(flipkartAffiliateCategories.get(i).category.categoryName.contains(category)) {
                return flipkartAffiliateCategories.get(i).categoryProductListExpiry;
            }
        }
        return 0;
    }

    @Override
    public List<Product> getProductListFromCategory(String category) {
        List<Product> products = new ArrayList<Product>();
        for(int i = 0; i < flipkartAffiliateCategories.size(); i++) {
            if(flipkartAffiliateCategories.get(i).category.categoryName.contains(category)) {
                products = flipkartAffiliateCategories.get(i).products;
            }
        }
        return products;
    }

    @Override
    public Product getProductById(String category, String productId) {
        Product product = null;
        for(int i = 0; i < flipkartAffiliateCategories.size(); i++) {
            if(flipkartAffiliateCategories.get(i).category.categoryName.contains(category)) {
                List<Product> products = flipkartAffiliateCategories.get(i).products;
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
        return flipkartJsonAdapter.getNextExpiryFromDbHelper();
    }

    @Override
    public boolean hasDataExpired() {
        return flipkartJsonAdapter.hasDataInDbExpired();
    }

    @Override
    public int getCategoryIndex(String category) {
        for (int i = 0; i < flipkartAffiliateCategories.size(); i++) {
            if (flipkartAffiliateCategories.get(i).category.categoryName.contains(category)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String fetchCategoryDataInBackground() {
        String datafromServer = null;
        NetworkTask networkTask = new NetworkTask(AppConstants.AFFILIATE_COLLECTION_VALUE_FLIPKART);
        datafromServer = networkTask.fetchDataFromUrl(PLAN_URL);
        return datafromServer;
    }

    @Override
    public void removeAllCategories() {
        flipkartJsonAdapter.removeCompleteList();
    }
}
