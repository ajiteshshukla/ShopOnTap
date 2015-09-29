package com.lphaindia.dodapp.dodapp.affiliateAdapters;

import android.util.Log;
import com.lphaindia.dodapp.dodapp.AppConstants;
import com.lphaindia.dodapp.dodapp.affiliateCategories.Category;
import com.lphaindia.dodapp.dodapp.data.DodDbHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by ajitesh.shukla on 9/15/15.
 */
public class FlipkartJsonAdapter implements IfaceAffiliateAdapter{

    private DodDbHelper dodDbHelper;

    public FlipkartJsonAdapter(DodDbHelper dodDbHelper) {
        this.dodDbHelper = dodDbHelper;
    }

    @Override
    public void insertJsonToDbHelper(JSONObject jsonObject) throws JSONException {
        JSONArray jList = jsonObject.getJSONArray("List");
        if (jList.length() > 0) {
            removeCompleteList();
        }
        for(int i = 0; i < jList.length(); i++) {
            JSONObject jCategory = (JSONObject) jList.get(i);
            String categoryName = jCategory.getString("category");
            dodDbHelper.insertOrUpdateCategories(categoryName, DodDbHelper.TABLE_FLIPKART);
        }
    }

    @Override
    public void removeCompleteList() {
        dodDbHelper.removeAllCategories(DodDbHelper.TABLE_FLIPKART);
    }

    @Override
    public List<Category> getCategoryList() {
        return dodDbHelper.getCategoryList(DodDbHelper.TABLE_FLIPKART);
    }
}
