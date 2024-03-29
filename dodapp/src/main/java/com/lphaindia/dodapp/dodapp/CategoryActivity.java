package com.lphaindia.dodapp.dodapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import com.crittercism.app.Crittercism;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.lphaindia.dodapp.dodapp.data.Category;
import com.lphaindia.dodapp.dodapp.network.NetworkTask;
import com.lphaindia.dodapp.dodapp.uiAdapters.CategoryCard;
import com.lphaindia.dodapp.dodapp.utils.Utility;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import com.rey.material.widget.SnackBar;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardGridArrayAdapter;
import it.gmariotti.cardslib.library.view.CardGridView;

import java.util.ArrayList;
import java.util.List;


public class CategoryActivity extends Activity implements SearchBox.SearchListener {
    SearchBox searchBox;
    private ProgressDialog progressDialog;
    private SnackBar snackBar;
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category);

        Crittercism.initialize(getApplicationContext(), "56238617d224ac0a00ed409b");

        if (isAccessibilityEnabled() == false
                && ScreenSlidePagerActivity.AccessibilityExplored == false) {
            Intent intent = new Intent(getApplicationContext(), ScreenSlidePagerActivity.class);
            startActivity(intent);
        } else if (isAccessibilityEnabled() == true
                && Build.VERSION.SDK_INT >= 23) {
            displayDialogForAndroidM();
        }

        if (AppConstants.isFrescoInitialized == false) {
            Fresco.initialize(this);
            AppConstants.isFrescoInitialized = true;
        }

        searchBox = (SearchBox)findViewById(R.id.searchbox);
        searchBox.enableVoiceRecognition(this);
        searchBox.setSearchListener(this);
        searchBox.setSaveEnabled(true);
        searchBox.setMenuVisibility(View.INVISIBLE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Trending Categories");

        snackBar = (SnackBar) findViewById(R.id.snackbar);
        snackBar.actionTextColor(Color.WHITE);
        snackBar.text("Something went wrong!! Unable to connect.");
        snackBar.textColor(Color.WHITE);
        snackBar.setVisibility(View.INVISIBLE);
        snackBar.dismiss();

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
            String url = AppConstants.REQUEST_URL + "?requesttype=" + AppConstants.REQUEST_CATEGORY;
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
                    List<Category> categories = Utility.getCategoryListFromJSON(datafromServer);
                    if (categories != null && !categories.isEmpty()) {
                        renderCategories(categories);
                        if (snackBar.isShown()) {
                            snackBar.dismiss();
                        }
                    } else {
                        snackBar.show();
                    }
                } catch(Exception e) {
                    snackBar.show();
                    e.printStackTrace();
                }
            } else {
                snackBar.show();
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

    @TargetApi(23)
    public void displayDialogForAndroidM() {
        if (!Settings.canDrawOverlays(this)) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Enable Draw Overlay");
            alertDialog.setMessage("Please enable overlay settings for ShopOnTap to browse similar " +
                    "products across different shopping apps");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + getPackageName()));
                            startActivity(intent);
                        }
                    });
            alertDialog.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isAccessibilityEnabled() == true
                && Build.VERSION.SDK_INT >= 23) {
            displayDialogForAndroidM();
        }
    }
}
