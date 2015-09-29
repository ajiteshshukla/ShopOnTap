package com.lphaindia.dodapp.dodapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.lphaindia.dodapp.dodapp.AppConstants;
import com.lphaindia.dodapp.dodapp.affiliateCategories.Category;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ajitesh.shukla on 9/13/15.
 */
public class DodDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "dealsdb";

    public static final String TABLE_FLIPKART = "tableflipkart";
    public static final String TABLE_SNAPDEAL = "tablesnapdeal";

    public static final String CATEGORY_ID = "categoryid";
    public static final String CATEGORY_NAME = "categoryname";

    public DodDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + TABLE_FLIPKART + " ("
                        + CATEGORY_ID + " INTEGER PRIMARY KEY, "
                        + CATEGORY_NAME + " TEXT UNIQUE NOT NULL);"
        );

        db.execSQL(" CREATE TABLE " + TABLE_SNAPDEAL + " ("
                        + CATEGORY_ID + " INTEGER PRIMARY KEY, "
                        + CATEGORY_NAME + " TEXT UNIQUE NOT NULL);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void removeAllCategories(String tablename) {
        final SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + tablename);
    }

    public void insertOrUpdateCategories(String categoryName, String tableName) {
        final SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + tableName + " WHERE " + CATEGORY_NAME + " = "
                + "'" + categoryName + "'", null);
        if(c.moveToFirst()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(CATEGORY_NAME, categoryName);
            db.update(tableName, contentValues, CATEGORY_NAME + "="
                        + "'" + categoryName + "'", null);
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(CATEGORY_NAME, categoryName);
            db.insert(tableName, null, contentValues);
        }
    }

    public List<Category> getCategoryList(String tableName) {
        final SQLiteDatabase db = this.getWritableDatabase();
        List<Category> categoryList = new ArrayList<Category>();
        Cursor c = db.rawQuery("SELECT * FROM " + tableName, null);
        if(c.moveToFirst()) {
            do {
                String categoryName = c.getString(c.getColumnIndex(CATEGORY_NAME));
                Category category = new Category(categoryName);
                categoryList.add(category);
            } while (c.moveToNext());
        }
        return categoryList;
    }
}
