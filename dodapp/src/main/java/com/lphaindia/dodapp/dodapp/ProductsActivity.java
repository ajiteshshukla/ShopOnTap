package com.lphaindia.dodapp.dodapp;

import android.app.Activity;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import com.lphaindia.dodapp.dodapp.data.Product;
import com.lphaindia.dodapp.dodapp.network.NetworkTask;
import com.lphaindia.dodapp.dodapp.uiAdapters.ProductCardAdapter;
import com.lphaindia.dodapp.dodapp.utils.Utility;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import com.rey.material.widget.ProgressView;
import com.rey.material.widget.Slider;
import com.rey.material.widget.SnackBar;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aasha.medhi on 10/8/15.
 */
public class ProductsActivity extends Activity  implements SearchBox.SearchListener, Slider.OnPositionChangeListener {
    private RecyclerView mRecyclerView;
    private ProductCardAdapter adapter;
    private ProgressDialog progressDialog;
    private Slider slider;
    private List<Product> mProducts;
    private SnackBar snackBar;
    SearchBox searchBox;

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
        Bundle b =getIntent().getExtras();
         // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(false);
        searchBox = (SearchBox)findViewById(R.id.searchbox);
        searchBox.enableVoiceRecognition(this);
        searchBox.setSearchListener(this);
        searchBox.setSaveEnabled(true);
        searchBox.setMenuVisibility(View.INVISIBLE);
        searchBox.setDrawerLogo(R.drawable.ic_undobar_undo);

        slider = (Slider) findViewById(R.id.discount_slider);
        slider.setOnPositionChangeListener(this);

        mProducts = null;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Products For You");
        progressDialog.show();

        snackBar = (SnackBar) findViewById(R.id.snackbarproduct);
        snackBar.actionTextColor(Color.WHITE);
        snackBar.text("Something went wrong!! Unable to connect.");
        snackBar.textColor(Color.WHITE);
        snackBar.setVisibility(View.INVISIBLE);
        snackBar.dismiss();

        String category = getIntent().getStringExtra(AppConstants.KEY_CATEGORY);
        String searchKey = getIntent().getStringExtra(AppConstants.KEY_SEARCH);
        if(category != null) {
            new FetchProducts(category, SEARCH_TYPE.CATEGORY).execute();
        }else if(searchKey != null){
            new FetchProducts(searchKey, SEARCH_TYPE.KEYWORD).execute();
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
            if(searchType == SEARCH_TYPE.CATEGORY) {
                url = AppConstants.REQUEST_URL + "?requesttype=" + AppConstants.REQUEST_PRODUCTS
                        + "&categoryname=" + this.searchString.replaceAll(" ", "%20");
            }else{
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
            List<Product> products = null;
            if (datafromServer != null) {
                try {
                    products = Utility.getProductListFromJSON(datafromServer);
                    mProducts = products;
                    renderProducts(mProducts, slider.getValue());
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
    public void onSearchOpened() {

    }

    @Override
    public void onSearchCleared() {

    }

    @Override
    public void onSearchClosed() {

    }

    @Override
    public void onSearchTermChanged(String s) {

    }

    @Override
    public void onSearch(String s) {
        new FetchProducts(s, SEARCH_TYPE.KEYWORD).execute();
    }

    @Override
    public void onResultClick(SearchResult searchResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            searchBox.populateEditText(matches.get(0));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
