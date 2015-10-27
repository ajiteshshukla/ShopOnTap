package com.lphaindia.dodapp.dodapp.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lphaindia.dodapp.dodapp.data.Category;
import com.lphaindia.dodapp.dodapp.data.Product;
import com.lphaindia.dodapp.dodapp.data.SubCategory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by aasha.medhi on 10/8/15.
 */
public class Utility {
    public static List<Product> getProductListFromJSON(String productListJSON) {
        try {
            Gson gson = new GsonBuilder().create();
            Type type = new TypeToken<List<Product>>() {
            }.getType();
            JSONArray productListInfo = new JSONObject(productListJSON).getJSONArray("List");
            List<Product> products = gson.fromJson(productListInfo.toString(), type);
            return products;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static List<SubCategory> getSubCategoryListFromJSON(String subcategoryListJSON) {
        try {
            Gson gson = new GsonBuilder().create();
            Type type = new TypeToken<List<SubCategory>>() {
            }.getType();
            JSONArray subCategoryListInfo = new JSONObject(subcategoryListJSON).getJSONArray("subCategoryList");
            List<SubCategory> subCategories = gson.fromJson(subCategoryListInfo.toString(), type);
            return subCategories;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static List<Category> getCategoryListFromJSON(String catgeoryListJSON) {
        try {
            Gson gson = new GsonBuilder().create();
            Type type = new TypeToken<List<Category>>() {
            }.getType();
            JSONArray categoryListInfo = new JSONObject(catgeoryListJSON).getJSONArray("List");
            List<Category> categories = gson.fromJson(categoryListInfo.toString(), type);
            return categories;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
