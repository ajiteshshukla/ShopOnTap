package com.lphaindia.dodapp.dodapp.affiliateCategories;

import android.util.Log;
import com.lphaindia.dodapp.dodapp.AppConstants;
import com.lphaindia.dodapp.dodapp.Product.SnapdealProduct;
import com.lphaindia.dodapp.dodapp.affiliates.Snapdeal;
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
import java.util.List;

/**
 * Created by ajitesh.shukla on 9/15/15.
 */
public class SnapdealAffiliateCategory implements IfaceAffiliateCategory{

    public Category category;

    public static List<SnapdealProduct> snapdealProducts = new ArrayList<SnapdealProduct>();

    public SnapdealAffiliateCategory(Category category) {
        this.category = category;
    }

    @Override
    public void fetchProducts() throws JSONException {
        fetchProducts(category.categoryUrl);
    }


    public void fetchProducts(String categoryUrl) throws JSONException {
        String datafromServer = null;
        try {
            URL url = new URL(categoryUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty(Snapdeal.AFFILIATE_HEADER, Snapdeal.AFFILIATE_ID);
            urlConnection.setRequestProperty(Snapdeal.TOKEN_HEADER, Snapdeal.TOKEN_ID);

            Log.d(AppConstants.TAG, urlConnection.toString());
            InputStream is = new BufferedInputStream(url.openStream());
            Log.d(AppConstants.TAG, urlConnection.getHeaderFields().toString());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            int size = 0;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
                size = size + line.length();
            }
            datafromServer=sb.toString();
            Log.d(AppConstants.TAG, "Fetched data: " + size);
            Log.d(AppConstants.TAG, String.valueOf(datafromServer.length()));

        } catch (MalformedURLException e) {
            Log.d(AppConstants.TAG, "Malformed URL Exception");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d(AppConstants.TAG, "IOException");
            e.printStackTrace();
        } catch (Exception e) {
            Log.d(AppConstants.TAG, e.getClass() + "--" + e.getMessage());
        }
        Log.d(AppConstants.TAG, "Product Category: " + category.categoryName);
        Log.d(AppConstants.TAG, "" + datafromServer);
        JSONObject productJsonObject = new JSONObject(datafromServer);
    }
}
