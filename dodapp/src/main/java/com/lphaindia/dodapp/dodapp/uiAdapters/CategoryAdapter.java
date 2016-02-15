package com.lphaindia.dodapp.dodapp.uiAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.lphaindia.dodapp.dodapp.AppConstants;
import com.lphaindia.dodapp.dodapp.ProductsActivity;
import com.lphaindia.dodapp.dodapp.R;
import com.lphaindia.dodapp.dodapp.data.Category;
import com.lphaindia.dodapp.dodapp.typeface.BigNoodleTitling;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aasha.medhi on 12/31/15.
 */
public class CategoryAdapter extends BaseAdapter {
    private Context mContext;
    private List<Category> mCategoryList = new ArrayList<Category>();
    private ArrayList<Integer> mSelectedCategoryList = new ArrayList<Integer>();

    public CategoryAdapter(Context ctxt) {
        mContext = ctxt;
        Category c = new Category("Women Clothing", "Women Clothing", R.drawable.women_clothing);
        mCategoryList.add(c);
        c = new Category("Men Clothing", "Men Clothing", R.drawable.men_clothing);
        mCategoryList.add(c);
        c = new Category("Women Shoes","Women Footwear", R.drawable.women_shoes);
        mCategoryList.add(c);
        c = new Category("Men Shoes","Men Footwear", R.drawable.men_shoes);
        mCategoryList.add(c);
        c = new Category("Watches", "Watches", R.drawable.watches);
        mCategoryList.add(c);
        c = new Category("Phones & Tablets", "Mobiles and Tabs", R.drawable.phones_tablets);
        mCategoryList.add(c);
        c = new Category("Books","Books", R.drawable.books);
        mCategoryList.add(c);
        c = new Category("Cameras", "Cameras", R.drawable.cameras);
        mCategoryList.add(c);
        c = new Category("Laptops", "Laptops", R.drawable.laptops);
        mCategoryList.add(c);
        c = new Category("Bags","Bags", R.drawable.bags);
        mCategoryList.add(c);
        c = new Category("Jewellery", "Jewellery", R.drawable.jewellery);
        mCategoryList.add(c);
        c = new Category("Special Offers", "Special Offers", R.drawable.offers);
        mCategoryList.add(c);
    }

    @Override
    public int getCount() {
        return mCategoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCategoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = (FrameLayout) inflater.inflate(R.layout.category_row, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.img_interest);
            BigNoodleTitling.applyFont(mContext, holder.textView);
            holder.textView = (TextView) convertView.findViewById(R.id.text_interest);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(mContext).load(mCategoryList.get(position).getDrawable()).fit().into(holder.imageView);
        holder.textView.setText(mCategoryList.get(position).getName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ProductsActivity.class);
                i.putExtra(AppConstants.KEY_CATEGORY, mCategoryList.get(position).getCategoryName());
                mContext.startActivity(i);
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
