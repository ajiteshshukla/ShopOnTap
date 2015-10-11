package com.lphaindia.dodapp.dodapp;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aasha.medhi on 10/8/15.
 */
public class ProductsActivity extends Activity {
    private RecyclerView mRecyclerView;
    private ProductCardAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products);
        Bundle b =getIntent().getExtras();
        String category = getIntent().getStringExtra(AppConstants.KEY_CATEGORY);
        // Initialize recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(false);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        new FetchProductsForCategory(category).execute();
    }


    class FetchProductsForCategory extends AsyncTask<Void, Void, String> {

        private String category;
        public FetchProductsForCategory(String category) {
            this.category = category;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected String doInBackground(Void... params) {
            String datafromServer = null;
            NetworkTask networkTask = new NetworkTask();
            String url = AppConstants.REQUEST_URL + "?requesttype=" + AppConstants.REQUEST_PRODUCTS
                    + "&categoryname=" + this.category.replaceAll(" ", "%20");
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
}
