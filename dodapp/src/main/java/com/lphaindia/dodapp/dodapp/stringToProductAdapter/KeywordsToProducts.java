package com.lphaindia.dodapp.dodapp.stringToProductAdapter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.lphaindia.dodapp.dodapp.AppConstants;
import com.lphaindia.dodapp.dodapp.Product.Product;
import com.lphaindia.dodapp.dodapp.accessibilityFeatures.TapAccessibilityService;
import com.lphaindia.dodapp.dodapp.affiliateCategoryAdapter.FlipkartAffiliateCategoryAdapter;
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

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(AppConstants.TAG, "inside pre execute loading overlay shall show now");
        LoadingOverlay.getInstance(context).showOverlay();
    }

    @Override
    protected void onPostExecute(List<Product> products) {
        super.onPostExecute(products);
        Log.d(AppConstants.TAG, "inside post execute loading overlay shall go now");
        LoadingOverlay.getInstance(context).removeOverlay();
        if(products != null && TapAccessibilityService.isPackageWhiteListed())
            CarouselOverlay.getInstance(context).showOverlay(products);
        else if (TapAccessibilityService.isPackageWhiteListed())
            CarouselOverlay.getInstance(context).showOverlay(null);
        else {
            //No Op for now
        }
    }

    @Override
    protected List<Product> doInBackground(String... params) {
        List<Product> matchingProductList = new ArrayList<Product>();
        List<Product> flipkartProductList = new ArrayList<Product>();
        //List<Product> amazonProductList = new ArrayList<Product>();
        //List<Product> myntraProductList = new ArrayList<Product>();
        String dataFromFlipkartServer = null;
        try {
            NetworkTask networkTaskFlipkart = new NetworkTask(AppConstants.AFFILIATE_COLLECTION_VALUE_FLIPKART);
            dataFromFlipkartServer = networkTaskFlipkart.fetchDataFromUrl(createSearchUrlForFlipkart());
            if (dataFromFlipkartServer != null) {
                JSONObject flipkartJasonObject = new JSONObject(dataFromFlipkartServer);
                flipkartProductList = FlipkartAffiliateCategoryAdapter.fetchProductsFromJson
                        (flipkartJasonObject, "Matching Products");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        matchingProductList.addAll(flipkartProductList);
        //Log.d(AppConstants.TAG, dataFromFlipkartServer);
        return matchingProductList;
    }

    //implement similar url creators for other affiliates
    public String createSearchUrlForFlipkart() {
        StringBuilder sb = new StringBuilder();
        sb.append("https://affiliate-api.flipkart.net/affiliate/search/json?query=");
        if(TapAccessibilityService.activityDataList.size() > 0) {
            StringBuilder keywords = new StringBuilder();
            for (int i = 0; i < AppConstants.KEYWORD_DEPTH
                    && i < TapAccessibilityService.activityDataList.size(); i++) {
                keywords.append(TapAccessibilityService.activityDataList.get(i));
                keywords.append(" ");
            }
            String searchString = keywords.toString();
            String[] words = searchString.split("\\s+");
            for (int i = 0; i < words.length && i < 10; i++) {
                words[i] = words[i].replaceAll("[^\\w]", "");
                sb.append(words[i]);
                sb.append("+");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("&resultCount=");
            sb.append(String.valueOf(AppConstants.ACCESSIBILITY_MAX_RESULT_COUNT));
        }
        Log.d(AppConstants.TAG,"search Url: " + sb.toString());
        return sb.toString();
    }
}
