package com.lphaindia.dodapp.dodapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.lphaindia.dodapp.dodapp.Product.Product;
import com.lphaindia.dodapp.dodapp.affiliateCategories.Category;
import com.lphaindia.dodapp.dodapp.affiliateResources.AffiliateActivityResources;
import com.lphaindia.dodapp.dodapp.affiliates.AffiliateCollection;
import com.lphaindia.dodapp.dodapp.injectors.Injectors;
import com.lphaindia.dodapp.dodapp.uiAdapters.FlipkartRecyclerViewAdapter;
import com.lphaindia.dodapp.dodapp.uiAdapters.SnapdealRecyclerViewAdapter;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener {

    @Inject
    AffiliateCollection affiliateCollection;

    private List<Category> flipkartAffiliateCategories;
    private List<Category> snapdealAffiliateCategories;

    private AffiliateActivityResources flipkartResources;
    private AffiliateActivityResources snapdealResources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injectors.initialize(this);
        Injectors.serviceInjector.injectMainActivity(this);
        setContentView(R.layout.activity_main);
        Intent dodIntent = new Intent(getApplicationContext(), DodIntentService.class);
        Log.d(AppConstants.TAG, "inside onCreate invoking DodIntentService");
        startService(dodIntent);

        //register resources for affiliates
        registerResourcesFlipkart();
        registerResourcesSnapdeal();

    }

    public List<String> getFlipkartCategoryList() {
        List<String> categoryList= new ArrayList<String>();
        flipkartAffiliateCategories = affiliateCollection.flipkart.getCategoryUrlList();
        for(int i = 0; i < flipkartAffiliateCategories.size(); i++) {
            categoryList.add(flipkartAffiliateCategories.get(i).categoryName);
        }
        return categoryList;
    }

    public List<String> getSnapdealCategoryList() {
        List<String> categoryList= new ArrayList<String>();
        snapdealAffiliateCategories = affiliateCollection.snapdeal.getCategoryUrlList();
        for(int i = 0; i < snapdealAffiliateCategories.size(); i++) {
            categoryList.add(snapdealAffiliateCategories.get(i).categoryName);
        }
        return categoryList;
    }

    public void registerResourcesFlipkart() {
        Spinner flipkartSpinner = (Spinner) findViewById(R.id.flipkart_category_spinner);
        ArrayAdapter flipkartArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,
                getFlipkartCategoryList());
        flipkartArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        flipkartSpinner.setAdapter(flipkartArrayAdapter);
        flipkartSpinner.setOnItemSelectedListener(this);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.flipkart_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        flipkartResources = new AffiliateActivityResources(flipkartSpinner, mRecyclerView);
    }

    public void registerResourcesSnapdeal() {
        Spinner snapdealSpinner = (Spinner) findViewById(R.id.snapdeal_category_spinner);
        ArrayAdapter snapdealArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,
                getSnapdealCategoryList());
        snapdealArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        snapdealSpinner.setAdapter(snapdealArrayAdapter);
        snapdealSpinner.setOnItemSelectedListener(this);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.snapdeal_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        snapdealResources = new AffiliateActivityResources(snapdealSpinner, mRecyclerView);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.flipkart_category_spinner) {
            handleFlipkartCategoryClick((String)parent.getItemAtPosition(position));
        } else if (parent.getId() == R.id.snapdeal_category_spinner) {
            handleSnapdealCategoryClick((String)parent.getItemAtPosition(position));
        }
    }

    public void handleSnapdealCategoryClick(String category) {
        Log.d(AppConstants.TAG, "" + affiliateCollection.snapdeal.getCategoryExpiry(category));
        if (affiliateCollection.snapdeal.getCategoryExpiry(category) > Calendar.getInstance().getTimeInMillis()) {
            snapdealPostExecute(category);
        } else {
            try {
                new NetworkRequest(category, AppConstants.AFFILIATE_COLLECTION_VALUE_SNAPDEAL, this)
                        .execute(affiliateCollection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void handleFlipkartCategoryClick(String category) {
        Log.d(AppConstants.TAG, "" + affiliateCollection.flipkart.getCategoryExpiry(category));
        if (affiliateCollection.flipkart.getCategoryExpiry(category) > Calendar.getInstance().getTimeInMillis()) {
            flipkartPostExecute(category);
        } else {
            try {
                new NetworkRequest(category, AppConstants.AFFILIATE_COLLECTION_VALUE_FLIPKART, this)
                        .execute(affiliateCollection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class NetworkRequest extends AsyncTask<AffiliateCollection, Void, Void> {

        private String category;
        private Context context;
        private int affiliate;
        private ProgressDialog progressDialog;
        public NetworkRequest(String category, int affiliate, Context context){
            this.category = category;
            this.context = context;
            this.affiliate = affiliate;
        }

        @Override
        protected Void doInBackground(AffiliateCollection... params) {
            try {
                switch (affiliate) {
                    case AppConstants.AFFILIATE_COLLECTION_VALUE_FLIPKART:
                        params[0].flipkart.populateCategorywithData(category);
                        break;
                    case AppConstants.AFFILIATE_COLLECTION_VALUE_SNAPDEAL:
                        params[0].snapdeal.populateCategorywithData(category);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("please wait");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(null);
            if(progressDialog.isShowing())
                progressDialog.dismiss();
            switch (affiliate) {
                case AppConstants.AFFILIATE_COLLECTION_VALUE_FLIPKART:
                    flipkartPostExecute(category);
                    break;
                case AppConstants.AFFILIATE_COLLECTION_VALUE_SNAPDEAL:
                    snapdealPostExecute(category);
                    break;
            }
        }
    }

    public void flipkartPostExecute(String category) {
        List<Product> products = affiliateCollection.flipkart.getProductListFromCategory(category);
        RecyclerView.Adapter mAdapter = new FlipkartRecyclerViewAdapter(products, this);
        flipkartResources.recyclerView.setAdapter(mAdapter);
    }

    public void snapdealPostExecute(String category) {
        List<Product> products = affiliateCollection.snapdeal.getProductListFromCategory(category);
        RecyclerView.Adapter mAdapter = new SnapdealRecyclerViewAdapter(products, this);
        snapdealResources.recyclerView.setAdapter(mAdapter);
    }
}
