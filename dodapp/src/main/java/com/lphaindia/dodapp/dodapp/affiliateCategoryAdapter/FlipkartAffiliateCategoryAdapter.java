package com.lphaindia.dodapp.dodapp.affiliateCategoryAdapter;

import android.util.Log;
import com.lphaindia.dodapp.dodapp.AppConstants;
import com.lphaindia.dodapp.dodapp.Product.Product;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ajitesh.shukla on 9/18/15.
 */
public class FlipkartAffiliateCategoryAdapter {

    public static List<Product> fetchProductsFromJson(JSONObject jsonObject, String categoryName) throws JSONException {
        List<Product> intermProducts = new ArrayList<Product>();
        JSONArray productListInfo = jsonObject.getJSONArray("productInfoList");
        if(productListInfo != null) {
            for (int i = 0; i < productListInfo.length(); i++) {
                JSONObject productBaseInfo = (JSONObject) productListInfo.get(i);
                productBaseInfo = productBaseInfo.getJSONObject("productBaseInfo");
                JSONObject productAttributes = productBaseInfo.getJSONObject("productAttributes");
                JSONObject productIdentfiers = productBaseInfo.getJSONObject("productIdentifier");
                String title = productAttributes.getString("title");
                String productUrl = productAttributes.getString("productUrl");
                String brand = productAttributes.getString("productBrand");
                String discountPercentage = productAttributes.getString("discountPercentage");
                String color = productAttributes.getString("color");
                String sizeUnit = productAttributes.getString("sizeUnit");
                String maximumRetailPrice = productAttributes.getJSONObject("maximumRetailPrice").getString("amount");
                String sellingPrice = productAttributes.getJSONObject("sellingPrice").getString("amount");
                String currency = productAttributes.getJSONObject("sellingPrice").getString("currency");
                String productId = productIdentfiers.getString("productId");

                JSONObject imageUrls = productAttributes.getJSONObject("imageUrls");
                String imageUrl = null;
                if (imageUrls != null) {
                    Iterator iter = imageUrls.keys();
                    if (iter.hasNext()) {
                        imageUrl = imageUrls.getString(iter.next().toString());
                    }
                }

                Log.d(AppConstants.TAG, "title: " + title);
                Log.d(AppConstants.TAG, "productUrl: " + productUrl);
                Log.d(AppConstants.TAG, "productBrand: " + brand);
                Log.d(AppConstants.TAG, "discountPercentage: " + discountPercentage);
                Log.d(AppConstants.TAG, "color: " + color);
                Log.d(AppConstants.TAG, "sizeUnit: " + sizeUnit);
                Log.d(AppConstants.TAG, "MRP: " + maximumRetailPrice);
                Log.d(AppConstants.TAG, "SellingPrice: " + sellingPrice);
                Log.d(AppConstants.TAG, "currency: " + currency);
                Log.d(AppConstants.TAG, "imageurl: " + imageUrl);
                Log.d(AppConstants.TAG, "================================ \n");
                intermProducts.add(new Product(title, maximumRetailPrice, sellingPrice, discountPercentage, imageUrl,
                        currency, brand, color, sizeUnit, productId, categoryName, productUrl));
            }
        }
        return intermProducts;
    }
}
