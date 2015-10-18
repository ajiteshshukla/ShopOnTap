package com.lphaindia.dodapp.dodapp;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.widget.SearchView;
import android.widget.Toast;
import com.lphaindia.dodapp.dodapp.data.Product;
import com.lphaindia.dodapp.dodapp.data.SubCategory;
import com.lphaindia.dodapp.dodapp.network.NetworkTask;
import com.lphaindia.dodapp.dodapp.overlays.IconOverlay;
import com.lphaindia.dodapp.dodapp.uiAdapters.ProductCardAdapter;
import com.lphaindia.dodapp.dodapp.utils.Utility;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import com.rey.material.widget.Slider;
import com.rey.material.widget.SnackBar;

import java.util.ArrayList;
import java.util.List;

import static android.view.Menu.NONE;

/**
 * Created by aasha.medhi on 10/8/15.
 */
public class ProductsActivity extends AppCompatActivity implements  Slider.OnPositionChangeListener {
    private RecyclerView mRecyclerView;
    private ProductCardAdapter adapter;
    private ProgressDialog progressDialog;
    private Slider slider;
    private List<Product> mProducts;
    private List<SubCategory> mSubCategories;
    private SnackBar snackBar;
    private boolean isMenuPrepared = false;
    SearchView searchView;

    @Override
    public void onPositionChanged(Slider slider, boolean b, float v, float v1, int i, int i1) {
        int value = slider.getValue();
        renderProducts(mProducts, value);
    }

    enum SEARCH_TYPE {
        KEYWORD,
        CATEGORY
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products);
        Bundle b = getIntent().getExtras();
        // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(false);
        slider = (Slider) findViewById(R.id.discount_slider);
        slider.setOnPositionChangeListener(this);

        mProducts = null;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Products For You");

        snackBar = (SnackBar) findViewById(R.id.snackbarproduct);
        snackBar.actionTextColor(Color.WHITE);
        snackBar.text("Something went wrong!! Unable to connect.");
        snackBar.textColor(Color.WHITE);
        snackBar.setVisibility(View.INVISIBLE);
        snackBar.dismiss();

        String category = getIntent().getStringExtra(AppConstants.KEY_CATEGORY);
        String searchKey = getIntent().getStringExtra(AppConstants.KEY_SEARCH);
        if (category != null) {
            new FetchProducts(category, SEARCH_TYPE.CATEGORY).execute();
        } else if (searchKey != null) {
            new FetchProducts(searchKey, SEARCH_TYPE.KEYWORD).execute();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (IconOverlay.getInstance(this).isOverlayShown()) {
            IconOverlay.getInstance(this).removeOverlay();
        }
    }

    class FetchProducts extends AsyncTask<Void, Void, String> {

        private String searchString;
        private SEARCH_TYPE searchType;

        public FetchProducts(String searchString, SEARCH_TYPE type) {
            this.searchString = searchString;
            this.searchType = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
            progressDialog.show();
            setProgressBarIndeterminateVisibility(true);
            if (snackBar.isShown()) {
                snackBar.dismiss();
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            String datafromServer = null;
            NetworkTask networkTask = new NetworkTask();
            String url = null;
            if (searchType == SEARCH_TYPE.CATEGORY) {
                url = AppConstants.REQUEST_URL + "?requesttype=" + AppConstants.REQUEST_PRODUCTS
                        + "&categoryname=" + this.searchString.replaceAll(" ", "%20");
            } else {
                url = AppConstants.REQUEST_URL + "?requesttype=" + AppConstants.REQUEST_SEARCH
                        + "&keywords=" + this.searchString.replaceAll(" ", "%20");
            }
            datafromServer = networkTask.fetchDataFromUrl(url);
            return datafromServer;
        }

        @Override
        protected void onPostExecute(String datafromServer) {
            super.onPostExecute(datafromServer);
            //progressBar.setVisibility(View.GONE);
            progressDialog.cancel();
            if (datafromServer != null) {
                try {
                    mProducts = Utility.getProductListFromJSON(datafromServer);
                    mSubCategories = Utility.getSubCategoryListFromJSON(datafromServer);
                    if(mSubCategories != null && !mSubCategories.isEmpty() && !isMenuPrepared){
                        invalidateOptionsMenu();
                    }
                    if(mProducts != null && !mProducts.isEmpty()) {
                        renderProducts(mProducts, slider.getValue());
                    }else{
                        Toast.makeText(ProductsActivity.this, "Cannot find products related to your search", Toast.LENGTH_LONG).show();
                    }
                    if (snackBar.isShown()) {
                        snackBar.dismiss();
                    }
                } catch (Exception e) {
                    snackBar.show();
                }
            } else {
                snackBar.show();
            }
        }
    }

    private void renderProducts(final List<Product> products, int value) {
        if (products == null || products.isEmpty()) {
            return;
        }
        List<Product> filteredProducts = new ArrayList<Product>();
        for (int i = 0; i < products.size(); i++) {
            try {
                if (Float.valueOf(products.get(i).discountPercentage) >= value) {
                    filteredProducts.add(products.get(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (filteredProducts != null && !filteredProducts.isEmpty()) {
            adapter = new ProductCardAdapter(this, filteredProducts);
            mRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);
        searchView.clearFocus();
        return true;

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(mSubCategories != null && !mSubCategories.isEmpty() && !isMenuPrepared) {
            SubMenu searchMenuItem = menu.findItem(R.id.subcategory_menu).getSubMenu();
            searchMenuItem.clear();
            for(int index = 0; index < mSubCategories.size(); index++) {
                final String subCategory = mSubCategories.get(index).getSubCategoryName();
                MenuItem item = searchMenuItem.add(0, index, index, subCategory);
                item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        new FetchProducts(subCategory, SEARCH_TYPE.CATEGORY).execute();
                        return true;
                    }
                });
            }
            isMenuPrepared = true;
            return true;
        }
        return false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchView.setQuery(query, false);
            searchView.clearFocus();
            new FetchProducts(query, SEARCH_TYPE.KEYWORD).execute();
        }
    }
}
