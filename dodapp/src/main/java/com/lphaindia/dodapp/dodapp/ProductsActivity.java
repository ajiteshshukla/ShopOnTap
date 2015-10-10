package com.lphaindia.dodapp.dodapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.lphaindia.dodapp.dodapp.data.Product;
import com.lphaindia.dodapp.dodapp.network.NetworkTask;
import com.lphaindia.dodapp.dodapp.uiAdapters.ProductCard;
import com.lphaindia.dodapp.dodapp.utils.Utility;
import it.gmariotti.cardslib.library.cards.material.MaterialLargeImageCard;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;
import it.gmariotti.cardslib.library.view.CardViewNative;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aasha.medhi on 10/8/15.
 */
public class ProductsActivity extends Activity {
    CardArrayRecyclerViewAdapter mCardArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products);
        Bundle b =getIntent().getExtras();
        String category = getIntent().getStringExtra(AppConstants.KEY_CATEGORY);
        new FetchProductsForCategory(category).execute();
        ArrayList<Card> cards = new ArrayList<Card>();
        mCardArrayAdapter = new CardArrayRecyclerViewAdapter(this, cards);

        //Staggered grid view
        CardRecyclerView mRecyclerView = (CardRecyclerView) findViewById(R.id.products_grid);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Set the empty view
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(mCardArrayAdapter);
        }
    }

    public List<Product> checkProduct(List<Product> products) {
        List<Product> newList = new ArrayList<Product>();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).imageUrl != null
                    && products.get(i).imageUrl.length() > 2
                    && products.get(i).sellingPrice != null
                    && products.get(i).sellingPrice.length() > 0
                    && Float.valueOf(products.get(i).sellingPrice) > 0.0) {
                //Log.d(AppConstants.TAG, "product satifies all criteria: " + products.get(i).toString());
                newList.add(products.get(i));
            } else {
                //Log.d(AppConstants.TAG, "criteria failed: " + products.get(i).toString());
            }
        }
        return newList;
    }

    public List<Product> filterOnDiscount(Float discount, List<Product> products) {
        List<Product> newList = new ArrayList<Product>();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).discountPercentage != null
                    && products.get(i).discountPercentage.length() > 0
                    && Float.valueOf(products.get(i).discountPercentage) >= discount) {
                //Log.d(AppConstants.TAG, "product satifies all criteria: " + products.get(i).discountPercentage);
                newList.add(products.get(i));
            } else {
                //Log.d(AppConstants.TAG, "criteria failed: " + products.get(i).toString());
            }
        }
        return newList;
    }


    class FetchProductsForCategory extends AsyncTask<Void, Void, String> {

        private String category;
        public FetchProductsForCategory(String category) {
            this.category = category;
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
            List<Product> products = null;
            if (datafromServer != null) {
                products = Utility.getProductListFromJSON(datafromServer);
            }
            renderProducts(products);
        }
    }
    private void renderProducts(final List<Product> products) {
        if (products != null && !products.isEmpty()) {
            ArrayList<Card> cards = new ArrayList<Card>();
            for (int i = 0; i < products.size(); i++) {
                final Product product = products.get(i);
                ProductCard card = new ProductCard(this, product);
                card.setOnClickListener(new Card.OnCardClickListener() {
                    @Override
                    public void onClick(Card card, View view) {
                        if(product.productUrl != null){
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(product.productUrl));
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
                    }
                });
                Log.e("AASHA", product.title);
                cards.add(card);
            }
            mCardArrayAdapter.addAll(cards);
            //mCardArrayAdapter.notifyDataSetChanged();
        }
    }
}
