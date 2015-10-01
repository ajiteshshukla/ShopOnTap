package com.lphaindia.dodapp.dodapp.stringToProductAdapter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.Log;
import com.lphaindia.dodapp.dodapp.AppConstants;
import com.lphaindia.dodapp.dodapp.Product.Product;
import com.lphaindia.dodapp.dodapp.accessibilityFeatures.TapAccessibilityService;
import com.lphaindia.dodapp.dodapp.affiliateCategoryAdapter.AffiliateCategoryAdapter;
import com.lphaindia.dodapp.dodapp.network.NetworkTask;
import com.lphaindia.dodapp.dodapp.overlays.CarouselOverlay;
import com.lphaindia.dodapp.dodapp.overlays.LoadingOverlay;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajitesh.shukla on 9/18/15.
 */
public class KeywordsToProducts extends AsyncTask<String, String, List<Product>> {
    private Context context;

    public KeywordsToProducts(Context context) {
        this.context = context;
    }

    public KeywordsToProducts keywordsToProducts;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(AppConstants.TAG, "inside pre execute loading overlay shall show now");
        LoadingOverlay.getInstance(context).showOverlay();

        keywordsToProducts = this;
        new CountDownTimer(10000, 500) {
            public void onTick(long millisUntilFinished) {
                // You can monitor the progress here as well by changing the onTick() time
                if (LoadingOverlay.getInstance(context).isOverlayShown() == false) {
                    keywordsToProducts.cancel(false);
                }
            }
            public void onFinish() {
                // stop async task if not in progress
                if (keywordsToProducts.getStatus() == AsyncTask.Status.RUNNING) {
                    keywordsToProducts.cancel(false);
                    // Add any specific task you wish to do as your extended class variable works here as well.
                    if (LoadingOverlay.getInstance(context).isOverlayShown()) {
                        LoadingOverlay.getInstance(context).removeOverlay();
                        if (TapAccessibilityService.isPackageWhiteListed())
                            CarouselOverlay.getInstance(context).showOverlay(null);
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onPostExecute(List<Product> products) {
        super.onPostExecute(products);
        Log.d(AppConstants.TAG, "inside post execute loading overlay shall go now");
        if (LoadingOverlay.getInstance(context).isOverlayShown()) {
            LoadingOverlay.getInstance(context).removeOverlay();
            if (products != null && TapAccessibilityService.isPackageWhiteListed())
                CarouselOverlay.getInstance(context).showOverlay(products);
            else if (TapAccessibilityService.isPackageWhiteListed())
                CarouselOverlay.getInstance(context).showOverlay(null);
            else {
                //No Op for now
            }
        }
    }

    @Override
    protected List<Product> doInBackground(String... params) {
        List<Product> matchingProductList = new ArrayList<Product>();
        List<Product> flipkartProductList = new ArrayList<Product>();
        String dataFromFlipkartServer = null;
        NetworkTask networkTaskFlipkart = new NetworkTask();
        for (int i = 1; i < AppConstants.KEYWORD_DEPTH; i++) {
            try {
                Log.d(AppConstants.TAG, "inside loop");
                String searchUrl = createSearchUrlForFlipkart(i);
                if (searchUrl != null) {
                    dataFromFlipkartServer = networkTaskFlipkart.fetchDataFromUrl(searchUrl);
                    if (dataFromFlipkartServer != null) {
                        JSONObject flipkartJasonObject = new JSONObject(dataFromFlipkartServer);
                        flipkartProductList = AffiliateCategoryAdapter.fetchProductsFromJson
                                (flipkartJasonObject);
                    }
                    if (flipkartProductList != null &&
                            flipkartProductList.size() > 0) {
                        i = AppConstants.KEYWORD_DEPTH;
                    }
                } else {
                    i = AppConstants.KEYWORD_DEPTH;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        matchingProductList.addAll(flipkartProductList);
        return matchingProductList;
    }

    //implement similar url creators for other affiliates
    public String createSearchUrlForFlipkart(int i) {
        if(TapAccessibilityService.activityDataList.size() > i) {
            StringBuilder sb = new StringBuilder();
            sb.append(AppConstants.REQUEST_URL);
            sb.append("?requesttype=" + AppConstants.REQUEST_SEARCH);
            sb.append("&keywords=");
            StringBuilder searchStringBuilder = new StringBuilder();
            for (int j = 0; j <= i; j++) {
                searchStringBuilder.append(TapAccessibilityService.activityDataList.get(j));
                searchStringBuilder.append(" ");
            }
            String searchString = searchStringBuilder.toString();
            String[] words = searchString.split("\\s+");
            for (int j = 0; j < words.length && j < 10; j++) {
                words[j] = words[j].replaceAll("-", "");
                words[j] = words[j].replaceAll("[^\\w]", "");
                if(words[j].length() > 0) {
                    sb.append(words[j]);
                    sb.append("+");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            Log.d(AppConstants.TAG, "search Url: " + sb.toString());
            return sb.toString();
        }
        return null;
    }
}
