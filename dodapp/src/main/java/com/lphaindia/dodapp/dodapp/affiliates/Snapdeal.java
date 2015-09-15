package com.lphaindia.dodapp.dodapp.affiliates;

import android.content.Context;
import android.util.Log;
import com.lphaindia.dodapp.dodapp.AppConstants;
import com.lphaindia.dodapp.dodapp.DodIntentService;
import com.lphaindia.dodapp.dodapp.affiliateAdapters.SnapdealJsonAdapter;
import com.lphaindia.dodapp.dodapp.affiliateCategories.Category;
import com.lphaindia.dodapp.dodapp.affiliateCategories.FlipkartAffiliateCategory;
import com.lphaindia.dodapp.dodapp.affiliateCategories.SnapdealAffiliateCategory;
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
 * Created by ajitesh.shukla on 9/14/15.
 */
public class Snapdeal implements IfaceAffiliate {
    private static final String PLAN_URL = "http://affiliate-feeds.snapdeal.com/feed/70385.json";
    public static final String TOKEN_ID = "bf654bc1807ed6d41afe7e2ad04a40";
    public static final String AFFILIATE_ID = "70385";
    public static final String AFFILIATE_HEADER = "Snapdeal-Affiliate-Id";
    public static final String TOKEN_HEADER = "Snapdeal-Token-Id";

    private Context context;

    private SnapdealJsonAdapter snapdealJsonAdapter;

    private List<SnapdealAffiliateCategory> snapdealAffiliateCategories;

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
    public void getCategoryUrlList() {
        List<Category> categoryUrlList = snapdealJsonAdapter.getCategoryList();
        for(int i = 0; i < categoryUrlList.size(); i++) {
            snapdealAffiliateCategories.add(new SnapdealAffiliateCategory(categoryUrlList.get(i)));
        }
    }

    @Override
    public void populateCategoriesWithData() throws JSONException {
        for(int i = 0; i < snapdealAffiliateCategories.size(); i++) {
            snapdealAffiliateCategories.get(i).fetchProducts();
        }
    }

    @Override
    public long getNextExpiry() {
        return snapdealJsonAdapter.getNextExpiryFromDbHelper();
    }

    @Override
    public boolean hasDataExpired() {
        return snapdealJsonAdapter.hasDataInDbExpired();
    }

    public String fetchCategoryDataInBackground() {
        String datafromServer = null;
        try {
            URL url = new URL(PLAN_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty(AFFILIATE_HEADER, AFFILIATE_ID);
            urlConnection.setRequestProperty(TOKEN_HEADER, TOKEN_ID);

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
        return datafromServer;
    }
}
