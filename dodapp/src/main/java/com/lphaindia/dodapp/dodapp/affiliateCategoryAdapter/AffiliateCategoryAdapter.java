package com.lphaindia.dodapp.dodapp.affiliateCategoryAdapter;

import com.lphaindia.dodapp.dodapp.Product.Product;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajitesh.shukla on 9/28/15.
 */
public class AffiliateCategoryAdapter {
    public static List<Product> fetchProductsFromJson(JSONObject jsonObject) throws JSONException {
        List<Product> intermProducts = new ArrayList<Product>();
        JSONArray productListInfo = jsonObject.getJSONArray("List");
        if(productListInfo != null) {
            for (int i = 0; i < productListInfo.length(); i++) {
                JSONObject productInfo = (JSONObject) productListInfo.get(i);

                String title = productInfo.getString("title");
                String productUrl = productInfo.getString("productUrl");
                String productId = productInfo.getString("productId");
                String brand = productInfo.getString("brand");
                String color = productInfo.getString("color");
                String sellingPrice = productInfo.getString("sellingPrice");
                String maximumRetailPrice = productInfo.getString("maximumRetailPrice");
                String imageUrl = productInfo.getString("imageUrl");
                String discountPercentage = productInfo.getString("discountPercentage");
                String currency = productInfo.getString("currency");
                String categoryName = productInfo.getString("category");
                String sizeUnit = productInfo.getString("sizeUnit");

                /*Log.d(AppConstants.TAG, "title: " + title);
                Log.d(AppConstants.TAG, "productUrl: " + productUrl);
                Log.d(AppConstants.TAG, "productBrand: " + brand);
                Log.d(AppConstants.TAG, "discountPercentage: " + discountPercentage);
                Log.d(AppConstants.TAG, "color: " + color);
                Log.d(AppConstants.TAG, "sizeUnit: " + sizeUnit);
                Log.d(AppConstants.TAG, "MRP: " + maximumRetailPrice);
                Log.d(AppConstants.TAG, "SellingPrice: " + sellingPrice);
                Log.d(AppConstants.TAG, "currency: " + currency);
                Log.d(AppConstants.TAG, "imageurl: " + imageUrl);
                Log.d(AppConstants.TAG, "================================ \n");*/
                intermProducts.add(new Product(title, maximumRetailPrice, sellingPrice, discountPercentage, imageUrl,
                        currency, brand, color, sizeUnit, productId, categoryName, productUrl));
            }
        }
        return intermProducts;
    }
}
