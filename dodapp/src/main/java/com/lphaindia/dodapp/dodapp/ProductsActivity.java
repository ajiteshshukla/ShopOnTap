package com.lphaindia.dodapp.dodapp;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
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
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aasha.medhi on 10/8/15.
 */
public class ProductsActivity extends Activity  implements SearchBox.SearchListener{
    private RecyclerView mRecyclerView;
    private ProductCardAdapter adapter;
    private ProgressBar progressBar;
    SearchBox searchBox;

    enum SEARCH_TYPE{
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
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        searchBox = (SearchBox)findViewById(R.id.searchbox);
        searchBox.enableVoiceRecognition(this);
        searchBox.setSearchListener(this);
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
            this.searchType = searchType;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            setProgressBarIndeterminateVisibility(true);
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
            Log.e(AppConstants.TAG, "" +datafromServer);
            return datafromServer;
        }

        @Override
        protected void onPostExecute(String datafromServer) {
            super.onPostExecute(datafromServer);
            progressBar.setVisibility(View.GONE);
            List<Product> products = null;
            if (datafromServer != null) {
                products = Utility.getProductListFromJSON(datafromServer);
                renderProducts(products);
            }
        }
    }
    private void renderProducts(final List<Product> products) {
        if (products != null && !products.isEmpty()) {
            adapter = new ProductCardAdapter(this, products);
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
