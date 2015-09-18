package com.lphaindia.dodapp.dodapp.affiliateCategories;

import com.lphaindia.dodapp.dodapp.Product.IfaceProduct;
import com.lphaindia.dodapp.dodapp.Product.Product;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by ajitesh.shukla on 9/15/15.
 */
public interface IfaceAffiliateCategory {
    void fetchProducts() throws JSONException;
}
