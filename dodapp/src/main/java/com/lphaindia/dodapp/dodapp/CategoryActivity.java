package com.lphaindia.dodapp.dodapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.lphaindia.dodapp.dodapp.data.Category;
import com.lphaindia.dodapp.dodapp.network.NetworkTask;
import com.lphaindia.dodapp.dodapp.uiAdapters.CategoryCard;
import com.lphaindia.dodapp.dodapp.utils.Utility;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;

import java.util.ArrayList;
import java.util.List;


public class CategoryActivity extends Activity implements SearchBox.SearchListener{
    private ProgressBar progressBar;
    SearchBox searchBox;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category);
        Fresco.initialize(this);
        if (isAccessibilityEnabled() == false) {
            Intent intent = new Intent(getApplicationContext(), ScreenSlidePagerActivity.class);
            startActivity(intent);
        }
        searchBox = (SearchBox)findViewById(R.id.searchbox);
        searchBox.enableVoiceRecognition(this);
        searchBox.setSearchListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        new FetchCategories().execute();
    }


    public boolean isAccessibilityEnabled() {
        int accessibilityEnabled = 0;
        String checkSettings = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        try {
            accessibilityEnabled = Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            //Log.d(AppConstants.TAG, " Settings not found Exception");
            e.printStackTrace();
        }
        if (accessibilityEnabled == 1 && checkSettings.contains("com.lphaindia.dodapp.dodapp")) {
            //Log.d(AppConstants.TAG, " Settings fine. No need to redirect");
            return true;
        }
        return false;
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
        //Launch product activity with the search string name
        Intent i=new Intent(CategoryActivity.this, ProductsActivity.class);
        i.putExtra(AppConstants.KEY_SEARCH, s);
        startActivity(i);
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

    class FetchCategories extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected String doInBackground(Void... params) {
            String datafromServer = null;
            NetworkTask networkTask = new NetworkTask();
            String url = AppConstants.REQUEST_URL + "?requesttype=" + AppConstants.REQUEST_CATEGORY;
            datafromServer = networkTask.fetchDataFromUrl(url);
            Log.e(AppConstants.TAG, "data " + datafromServer);
            return datafromServer;
        }

        @Override
        protected void onPostExecute(String datafromServer) {
            super.onPostExecute(datafromServer);
            progressBar.setVisibility(View.GONE);
            if (datafromServer != null) {
                List<Category> categories = Utility.getCategoryListFromJSON(datafromServer);
                renderCategories(categories);
            }
        }
    }

    private void renderCategories(final List<Category> categories) {
        if (categories != null && !categories.isEmpty()) {
            ArrayList<Card> cards = new ArrayList<Card>();
            for (int i = 0; i < categories.size(); i++) {
                CategoryCard card = new CategoryCard(this, categories.get(i));
                card.setOnClickListener(new Card.OnCardClickListener() {
                    @Override
                    public void onClick(Card card, View view) {
                        //Launch product activity with the category name
                        Intent i=new Intent(CategoryActivity.this, ProductsActivity.class);
                        i.putExtra(AppConstants.KEY_CATEGORY, ((CategoryCard) card).getCategory().getName());
                        startActivity(i);
                    }
                });
                cards.add(card);
            }
            CardGridArrayAdapter mCardArrayAdapter = new CardGridArrayAdapter(this, cards);
            CardGridView gridView = (CardGridView) findViewById(R.id.category_grid);
            if (gridView != null) {
                gridView.setAdapter(mCardArrayAdapter);
            }
        }
    }

}
