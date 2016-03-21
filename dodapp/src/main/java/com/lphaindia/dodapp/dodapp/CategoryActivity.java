package com.lphaindia.dodapp.dodapp;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.*;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.GridView;
import android.widget.SearchView;
import com.crittercism.app.Crittercism;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.lphaindia.dodapp.dodapp.pushnotification.QuickstartPreferences;
import com.lphaindia.dodapp.dodapp.pushnotification.RegistrationIntentService;
import com.lphaindia.dodapp.dodapp.uiAdapters.CategoryAdapter;


public class CategoryActivity extends AppCompatActivity {

    SearchView searchView;
    GridView mGridView;
    CategoryAdapter mAdapter;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    //Push notification
    private BroadcastReceiver mRegistrationBroadcastReceiver;

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
        Log.e("AASHA", "Reg ");
        //Register for push notifs
        registerForPushNotification();
        renderCategories();
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

    private void renderCategories() {
        mGridView = (GridView) findViewById(R.id.category_grid);
        mAdapter = new CategoryAdapter(CategoryActivity.this);
        mGridView.setAdapter(mAdapter);
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
        if (isAccessibilityEnabled() == true) {
            if (Build.VERSION.SDK_INT >= 23) {
                displayDialogForAndroidM();
            }
            invalidateOptionsMenu();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.category_menu, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.clearFocus();
        searchMenuItem
                .setShowAsAction(MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW
                        | MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
        return true;

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        SubMenu searchMenuItem = menu.findItem(R.id.menu).getSubMenu();
        searchMenuItem.clear();
        if (isAccessibilityEnabled() == false) {
            MenuItem itemEnableAccesibility = searchMenuItem.add(0, 0, 0, "Enable Accesibility");
            itemEnableAccesibility.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    CategoryActivity.this.startActivity(intent);
                    return true;
                }
            });
        }
        MenuItem itemHelp = searchMenuItem.add(0, 0, 0, "View Tutorial");
        itemHelp.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(CategoryActivity.this, TutorialActivity.class);
                CategoryActivity.this.startActivity(intent);
                return true;
            }
        });
        return true;
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
            Intent i = new Intent(CategoryActivity.this, ProductsActivity.class);
            i.putExtra(AppConstants.KEY_SEARCH, query);
            startActivity(i);
        }
    }

    private void registerForPushNotification() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    Log.e("AASHA", "Registered for push notifs");
                } else {
                    Log.e("AASHA", "Failed");
                }
            }
        };
        if (checkPlayServices()) {
            Log.e("AASHA", " play");
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }else{
            Log.e("AASHA", "No play");
        }
    }
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("TAG", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
