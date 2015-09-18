package com.lphaindia.dodapp.dodapp.network;

import android.util.Log;
import com.lphaindia.dodapp.dodapp.AppConstants;
import com.lphaindia.dodapp.dodapp.affiliates.Flipkart;
import com.lphaindia.dodapp.dodapp.affiliates.Snapdeal;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ajitesh.shukla on 9/17/15.
 */
public class NetworkTask {

    private int affiliate;

    public NetworkTask(int affiliate) {
        this.affiliate = affiliate;
    }

    public String fetchDataFromUrl(String categoryUrl) {
        String datafromServer = null;
        try {
            URL url = new URL(categoryUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Accept", "application/json");

            if (affiliate == AppConstants.AFFILIATE_COLLECTION_VALUE_FLIPKART) {
                urlConnection.setRequestProperty(Flipkart.AFFILIATE_HEADER, Flipkart.AFFILIATE_ID);
                urlConnection.setRequestProperty(Flipkart.TOKEN_HEADER, Flipkart.TOKEN_ID);
            } else if (affiliate == AppConstants.AFFILIATE_COLLECTION_VALUE_SNAPDEAL){
                urlConnection.setRequestProperty(Snapdeal.AFFILIATE_HEADER, Snapdeal.AFFILIATE_ID);
                urlConnection.setRequestProperty(Snapdeal.TOKEN_HEADER, Snapdeal.TOKEN_ID);
            }

            Log.d(AppConstants.TAG, urlConnection.toString());
            InputStream is = new BufferedInputStream(urlConnection.getInputStream());
            Log.d(AppConstants.TAG, urlConnection.getHeaderFields().toString());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            int size = 0;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
                size = size + line.length();
            }
            datafromServer = sb.toString();
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
