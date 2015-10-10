package com.lphaindia.dodapp.dodapp.uiAdapters;

import android.support.v7.widget.RecyclerView;


import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import com.lphaindia.dodapp.dodapp.R;
import com.lphaindia.dodapp.dodapp.accessibilityFeatures.TapAccessibilityService;
import com.lphaindia.dodapp.dodapp.accessibilityFeatures.TapAnalytics;
import com.lphaindia.dodapp.dodapp.data.Product;
import com.lphaindia.dodapp.dodapp.overlays.CarouselOverlay;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.VerticalItemHolder> {

    private ArrayList<Product> mProductList;
    private Context mCtxt;
    private AdapterView.OnItemClickListener mOnItemClickListener;

    public CarouselAdapter(Context ctxt) {
        mProductList = new ArrayList<Product>();
        mCtxt = ctxt;
    }

    /*
     * A common adapter modification or reset mechanism. As with ListAdapter,
     * calling notifyDataSetChanged() will trigger the RecyclerView to update
     * the view. However, this method will not trigger any of the RecyclerView
     * animation features.
     */
    public void setItemCount(int count) {
        mProductList.clear();
        notifyDataSetChanged();
    }

    /*
     * Inserting a new item at the head of the list. This uses a specialized
     * RecyclerView method, notifyItemInserted(), to trigger any enabled item
     * animations in addition to updating the view.
     */
    public void addItem(int position, Product p) {
        mProductList.add(position, p);
        notifyItemInserted(position);
    }

    /*
     * Inserting a new item at the head of the list. This uses a specialized
     * RecyclerView method, notifyItemRemoved(), to trigger any enabled item
     * animations in addition to updating the view.
     */
    public void removeItem(int position) {
        mProductList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public VerticalItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View root = inflater.inflate(R.layout.view_item, container, false);
        return new VerticalItemHolder(root, this);
    }

    @Override
    public void onBindViewHolder(VerticalItemHolder itemHolder, int position) {
        Product item = mProductList.get(position);
        itemHolder.setContext(this.mCtxt);
        itemHolder.setImgProductImage(item.imageUrl);
        itemHolder.setProductName(item.title);
        itemHolder.setProductPrice(item.sellingPrice);
        itemHolder.setBrandName(item.brand);
        itemHolder.setUrl(item.productUrl);
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(VerticalItemHolder itemHolder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }

    public static class VerticalItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextProductName;
        private ImageView mImgProductImage;
        private Button mBtnProductPrice;
        private TextView mTextBrandName;
        private String mUrl;
        private CarouselAdapter mAdapter;
        private Context mCtxt;
        public VerticalItemHolder(View itemView, CarouselAdapter adapter) {
            super(itemView);
            itemView.setOnClickListener(this);
            mAdapter = adapter;
            mTextProductName = (TextView) itemView.findViewById(R.id.product_name);
            mImgProductImage = (ImageView) itemView.findViewById(R.id.product_image);
            mBtnProductPrice = (Button) itemView.findViewById(R.id.product_price);
            mBtnProductPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TapAnalytics.sendAnalyticsBuyProduct(TapAccessibilityService.mTracker);
                    if(mUrl != null){
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(mUrl));
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mCtxt.startActivity(i);
                        CarouselOverlay.getInstance(mCtxt).removeOverlay();
                    }
                }
            });
            mTextBrandName = (TextView) itemView.findViewById(R.id.product_brand);
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }

        public void setProductPrice(CharSequence productPrice) {
            mBtnProductPrice.setText(productPrice);
        }
        public void setProductName(CharSequence productName) {
            mTextProductName.setText(productName);
        }
        public void setBrandName(CharSequence brandName) {
            mTextBrandName.setText(brandName);
        }
        public void setContext(Context ctxt){
            mCtxt = ctxt;
        }
        public void setImgProductImage(String productImage){
            Picasso.with(mCtxt).load(productImage).resize(300, 200).into(mImgProductImage);
        }
        public void setUrl(String url){
            mUrl = url;
        }
    }
}
