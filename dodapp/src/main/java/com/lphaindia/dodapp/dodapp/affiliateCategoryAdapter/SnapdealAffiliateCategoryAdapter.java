package com.lphaindia.dodapp.dodapp.affiliateCategoryAdapter;

import android.util.Log;
import com.lphaindia.dodapp.dodapp.AppConstants;
import com.lphaindia.dodapp.dodapp.Product.Product;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajitesh.shukla on 9/18/15.
 */
public class SnapdealAffiliateCategoryAdapter {

    public static List<Product> fetchProductsFromJson(JSONObject jsonObject, String categoryName) throws JSONException {
        List<Product> intermProducts = new ArrayList<Product>();
        JSONArray productListInfo = jsonObject.getJSONArray("products");
        for (int i = 0; i < productListInfo.length(); i++) {
            JSONObject productAttributes = (JSONObject) productListInfo.get(i);
            //productBaseInfo = productBaseInfo.getJSONObject("productBaseInfo");
            //JSONObject productAttributes = productBaseInfo.getJSONObject("productAttributes");
            String title = productAttributes.getString("title");
            String productUrl = productAttributes.getString("link");
            String brand = productAttributes.getString("brand");
            String discountPercentage = "";
            String color = "";
            String sizeUnit = "";
            String maximumRetailPrice = productAttributes.getString("mrp");
            String sellingPrice = productAttributes.getString("offerPrice");
            String currency = "";
            String imageUrl = productAttributes.getString("imageLink");
            String productId = productAttributes.getString("id");

            if(maximumRetailPrice != null
                    && maximumRetailPrice.length() > 0
                    && sellingPrice != null
                    && sellingPrice.length() > 0) {
                float discountPct = (float) 0.0;
                float mrp = Float.valueOf(maximumRetailPrice);
                float sp = Float.valueOf(sellingPrice);
                if (sp > 0.0) {
                    discountPct = (mrp - sp) * 100 / mrp;
                    discountPct = round(discountPct, 2);
                }
                discountPercentage = String.valueOf(discountPct);
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
        return intermProducts;
    }
    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
