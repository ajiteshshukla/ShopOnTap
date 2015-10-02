package com.lphaindia.dodapp.dodapp.affiliateCategories;

import com.lphaindia.dodapp.dodapp.AppConstants;
import com.lphaindia.dodapp.dodapp.Product.Product;
import com.lphaindia.dodapp.dodapp.affiliateCategoryAdapter.AffiliateCategoryAdapter;
import com.lphaindia.dodapp.dodapp.network.NetworkTask;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajitesh.shukla on 9/15/15.
 */
public class FlipkartAffiliateCategory implements IfaceAffiliateCategory{

    public Category category;

    public List<Product> products = new ArrayList<Product>();

    public FlipkartAffiliateCategory(Category category) {
        this.category = category;
    }

    public void fetchProducts() throws JSONException {
        String categoryUrl = createFetchProductListUrl(category.getCategoryName());
        fetchProducts(categoryUrl);
    }

    public String createFetchProductListUrl(String categoryName) {
        categoryName = categoryName.replaceAll(" ", "%20");
        return (AppConstants.REQUEST_URL + "?requesttype=" + AppConstants.REQUEST_PRODUCTS
                + "&affiliate=" + AppConstants.AFFILIATE_FLIPKART + "&category=" + categoryName);
    }

    public void fetchProducts(String categoryUrl) throws JSONException {
        String datafromServer = null;
        NetworkTask networkTask = new NetworkTask();
        datafromServer = networkTask.fetchDataFromUrl(categoryUrl);
        //Log.d(AppConstants.TAG, "Product Category: " + category.getCategoryName());
        //Log.d(AppConstants.TAG, "" + datafromServer);
        if (datafromServer != null) {
            JSONObject productJsonObject = new JSONObject(datafromServer);
            products = AffiliateCategoryAdapter.fetchProductsFromJson(productJsonObject);
        }
    }
}
