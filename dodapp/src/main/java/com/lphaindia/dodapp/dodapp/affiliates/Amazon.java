package com.lphaindia.dodapp.dodapp.affiliates;

import com.lphaindia.dodapp.dodapp.AppConstants;
import com.lphaindia.dodapp.dodapp.Product.Product;
import com.lphaindia.dodapp.dodapp.affiliateAdapters.AmazonJsonAdapter;
import com.lphaindia.dodapp.dodapp.affiliateCategories.AmazonAffiliateCategory;
import com.lphaindia.dodapp.dodapp.affiliateCategories.Category;
import com.lphaindia.dodapp.dodapp.network.NetworkTask;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajitesh.shukla on 9/14/15.
 */
public class Amazon implements IfaceAffiliate {

    private AmazonJsonAdapter amazonJsonAdapter;
    public List<AmazonAffiliateCategory> amazonAffiliateCategories;

    public Amazon(AmazonJsonAdapter amazonJsonAdapter, List<AmazonAffiliateCategory> amazonAffiliateCategories) {
        this.amazonJsonAdapter = amazonJsonAdapter;
        this.amazonAffiliateCategories = amazonAffiliateCategories;
    }

    @Override
    public void pushCategoryList() {
        String responseString = fetchCategoryListInBackground();
        if(responseString != null) {
            try {
                JSONObject responseJson = new JSONObject(responseString);
                amazonJsonAdapter.insertJsonToDbHelper(responseJson);
            } catch (Exception e) {
                //Log.d(AppConstants.TAG, responseString);
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Category> getCategoryList() {
        List<Category> categoryList = amazonJsonAdapter.getCategoryList();
        for (int i = 0; i < categoryList.size(); i++) {
            int index = getCategoryIndex(categoryList.get(i));
            if(index != -1) {
                //Log.d(AppConstants.TAG, "no need to delete flipkart category is in valid state");
            } else {
                amazonAffiliateCategories.add(new AmazonAffiliateCategory(categoryList.get(i)));
            }
        }
        return categoryList;
    }

    @Override
    public void populateCategorywithData(String category) throws JSONException {
        for(int i = 0; i < amazonAffiliateCategories.size(); i++) {
            if(amazonAffiliateCategories.get(i).category.getCategoryName().contains(category)) {
                amazonAffiliateCategories.get(i).fetchProducts();
            }
        }
    }

    @Override
    public List<Product> getProductListFromCategory(String category) {
        List<Product> products = new ArrayList<Product>();
        for(int i = 0; i < amazonAffiliateCategories.size(); i++) {
            if(amazonAffiliateCategories.get(i).category.getCategoryName().contains(category)) {
                products = amazonAffiliateCategories.get(i).products;
            }
        }
        return products;
    }

    @Override
    public int getCategoryIndex(Category category) {
        for (int i = 0; i < amazonAffiliateCategories.size(); i++) {
            if (amazonAffiliateCategories.get(i).category.getCategoryName().contains(category.getCategoryName())) {
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
                + AppConstants.AFFILIATE_AMAZON);
    }

    @Override
    public void removeAllCategories() {
        amazonJsonAdapter.removeCompleteList();
    }
}
