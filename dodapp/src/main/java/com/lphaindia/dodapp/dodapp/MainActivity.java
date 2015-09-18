package com.lphaindia.dodapp.dodapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.lphaindia.dodapp.dodapp.Product.Product;
import com.lphaindia.dodapp.dodapp.affiliateCategories.Category;
import com.lphaindia.dodapp.dodapp.affiliateResources.AffiliateActivityResources;
import com.lphaindia.dodapp.dodapp.affiliates.AffiliateCollection;
import com.lphaindia.dodapp.dodapp.injectors.Injectors;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener {

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

    public void registerResourcesFlipkart() {
        Button flipkartBackToCategories = (Button) findViewById(R.id.button1);
        flipkartBackToCategories.setOnClickListener(this);

        LinearLayout flipkartLinearLayout = (LinearLayout) findViewById(R.id.ll2);
        HorizontalScrollView flipkartScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);

        flipkartAffiliateCategories = affiliateCollection.flipkart.getCategoryUrlList();
        for(int i = 0; i < flipkartAffiliateCategories.size(); i++) {
            Log.d(AppConstants.TAG, "Adding view for: " + flipkartAffiliateCategories.get(i).categoryName);
            TextView txtview = new TextView(this);
            txtview.setText(flipkartAffiliateCategories.get(i).categoryName);
            txtview.setId(AppConstants.FLIPKART_CATEGORY_ID_OFFSET + i);
            txtview.setPadding(10, 0, 10, 0);
            txtview.setOnClickListener(this);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(1000, ViewGroup.LayoutParams.MATCH_PARENT);
            flipkartLinearLayout.addView(txtview, i, layoutParams);
        }
        flipkartResources = new AffiliateActivityResources(flipkartBackToCategories, flipkartLinearLayout,
                flipkartScrollView);
    }

    public void registerResourcesSnapdeal() {
        Button snapdealBackToCategories = (Button) findViewById(R.id.button2);
        snapdealBackToCategories.setOnClickListener(this);

        LinearLayout snapdealLinearLayout = (LinearLayout) findViewById(R.id.ll3);
        HorizontalScrollView snapdealScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView2);

        snapdealAffiliateCategories = affiliateCollection.snapdeal.getCategoryUrlList();
        for(int i = 0; i < snapdealAffiliateCategories.size(); i++) {
            TextView txtview = new TextView(this);
            txtview.setText(snapdealAffiliateCategories.get(i).categoryName);
            txtview.setId(AppConstants.SNAPDEAL_CATEGORY_ID_OFFSET + i);
            txtview.setPadding(10, 0, 10, 0);
            txtview.setOnClickListener(this);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(1000, ViewGroup.LayoutParams.MATCH_PARENT);
            snapdealLinearLayout.addView(txtview, i, layoutParams);
        }
        snapdealResources = new AffiliateActivityResources(snapdealBackToCategories, snapdealLinearLayout,
                snapdealScrollView);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == flipkartResources.backToCategoryButton.getId()) {
            handleFlipkartBackButtonClick();
        } else if (v.getId() == snapdealResources.backToCategoryButton.getId()) {
            handleSnapdealBackButtonClick();
        } else if(v.getId() >= AppConstants.FLIPKART_CATEGORY_ID_OFFSET
                && v.getId() < AppConstants.FLIPKART_CATEGORY_ID_LIMIT) {
            handleFlipkartCategoryClick(v);
        } else if(v.getId() >= AppConstants.FLIPKART_PRODUCT_ID_OFFSET
                && v.getId() < AppConstants.FLIPKART_PRODUCT_ID_LIMIT) {
            handleFlipkartProductClick(v);
        } else if(v.getId() >= AppConstants.SNAPDEAL_CATEGORY_ID_OFFSET
                && v.getId() < AppConstants.SNAPDEAL_CATEGORY_ID_LIMIT) {
            handleSnapdealCategoryClick(v);
        } else if(v.getId() >= AppConstants.SNAPDEAL_PRODUCT_ID_OFFSET
                && v.getId() < AppConstants.SNAPDEAL_PRODUCT_ID_LIMIT) {
            handleSnapdealProductClick(v);
        }
    }

    public void handleSnapdealBackButtonClick() {
        snapdealResources.linearLayout.removeAllViewsInLayout();
        snapdealAffiliateCategories = affiliateCollection.snapdeal.getCategoryUrlList();
        for(int i = 0; i < snapdealAffiliateCategories.size(); i++) {
            TextView txtview = new TextView(this);
            txtview.setText(snapdealAffiliateCategories.get(i).categoryName);
            txtview.setId(AppConstants.SNAPDEAL_CATEGORY_ID_OFFSET + i);
            txtview.setPadding(10, 0, 10, 0);
            txtview.setOnClickListener(this);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(1000, ViewGroup.LayoutParams.MATCH_PARENT);
            snapdealResources.linearLayout.addView(txtview, i, layoutParams);
        }
        snapdealResources.horizontalScrollView.scrollTo(0,0);
    }

    public void handleFlipkartBackButtonClick() {
        flipkartResources.linearLayout.removeAllViewsInLayout();
        flipkartAffiliateCategories = affiliateCollection.flipkart.getCategoryUrlList();
        for(int i = 0; i < flipkartAffiliateCategories.size(); i++) {
            TextView txtview = new TextView(this);
            txtview.setText(flipkartAffiliateCategories.get(i).categoryName);
            txtview.setId(AppConstants.FLIPKART_CATEGORY_ID_OFFSET + i);
            txtview.setPadding(10, 0, 10, 0);
            txtview.setOnClickListener(this);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(1000, ViewGroup.LayoutParams.MATCH_PARENT);
            flipkartResources.linearLayout.addView(txtview, i, layoutParams);
        }
        flipkartResources.horizontalScrollView.scrollTo(0,0);
    }

    public void handleSnapdealCategoryClick(View view) {
        TextView textView = (TextView) view;
        String category = (String) textView.getText();
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
        snapdealResources.horizontalScrollView.scrollTo(0, 0);
    }

    public void handleFlipkartCategoryClick(View view) {
        TextView textView = (TextView) view;
        String category = (String) textView.getText();
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
        flipkartResources.horizontalScrollView.scrollTo(0, 0);
    }

    public void handleSnapdealProductClick(View view) {
        String description = String.valueOf(view.getContentDescription());
        String productId = description.substring(description.indexOf("productId:") + 10,
                description.indexOf("categoryName:"));
        String categoryName = description.substring(description.indexOf("categoryName:") + 13);
        Product product = affiliateCollection.snapdeal.getProductById(categoryName, productId);
        launchUri(product.productUrl);
    }

    public void handleFlipkartProductClick(View view) {
        String description = String.valueOf(view.getContentDescription());
        String productId = description.substring(description.indexOf("productId:") + 10,
                description.indexOf("categoryName:"));
        String categoryName = description.substring(description.indexOf("categoryName:") + 13);
        Product product = affiliateCollection.flipkart.getProductById(categoryName, productId);
        launchUri(product.productUrl);
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
        flipkartResources.linearLayout.removeAllViewsInLayout();
        for(int i = 0; i < products.size() && i < AppConstants.PRODUCT_CLUSTER_SIZE; i++) {
            if (products.get(i).imageUrl != null && products.get(i).imageUrl.length() > 5) {
                ImageView imageView = new ImageView(this);
                Picasso.with(this).load(products.get(i).imageUrl).into(imageView);
                imageView.setId(AppConstants.FLIPKART_PRODUCT_ID_OFFSET + i);
                imageView.setPadding(10, 0, 10, 0);
                imageView.setOnClickListener(this);
                imageView.setContentDescription("productId:" + products.get(i).productId
                        + "categoryName:" + products.get(i).category);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(1000, ViewGroup.LayoutParams.MATCH_PARENT);
                flipkartResources.linearLayout.addView(imageView, i, layoutParams);
            } else {
                TextView txtview = new TextView(this);
                txtview.setText(products.get(i).title);
                txtview.setId(AppConstants.FLIPKART_PRODUCT_ID_OFFSET + i);
                txtview.setPadding(10, 0, 10, 0);
                txtview.setOnClickListener(this);
                txtview.setContentDescription("productId:" + products.get(i).productId
                        + "categoryName:" + products.get(i).category);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(1000, ViewGroup.LayoutParams.MATCH_PARENT);
                flipkartResources.linearLayout.addView(txtview, i, layoutParams);
            }
        }
    }

    public void snapdealPostExecute(String category) {
        List<Product> products = affiliateCollection.snapdeal.getProductListFromCategory(category);
        snapdealResources.linearLayout.removeAllViewsInLayout();
        for(int i = 0; i < products.size() && i < AppConstants.PRODUCT_CLUSTER_SIZE; i++) {
            if (products.get(i).imageUrl != null && products.get(i).imageUrl.length() > 5) {
                ImageView imageView = new ImageView(this);
                Picasso.with(this).load(products.get(i).imageUrl).into(imageView);
                imageView.setId(AppConstants.SNAPDEAL_PRODUCT_ID_OFFSET + i);
                imageView.setPadding(10, 0, 10, 0);
                imageView.setOnClickListener(this);
                imageView.setContentDescription("productId:" + products.get(i).productId
                        + "categoryName:" + products.get(i).category);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(1000, ViewGroup.LayoutParams.MATCH_PARENT);
                snapdealResources.linearLayout.addView(imageView, i, layoutParams);
            } else {
                TextView txtview = new TextView(this);
                txtview.setText(products.get(i).title);
                txtview.setId(AppConstants.SNAPDEAL_PRODUCT_ID_OFFSET + i);
                txtview.setPadding(10, 0, 10, 0);
                txtview.setOnClickListener(this);
                txtview.setContentDescription("productId:" + products.get(i).productId
                        + "categoryName:" + products.get(i).category);
                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(1000, ViewGroup.LayoutParams.MATCH_PARENT);
                snapdealResources.linearLayout.addView(txtview, i, layoutParams);
            }
        }
    }

    public void launchUri(String uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }
}
