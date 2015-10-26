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
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lphaindia.dodapp.dodapp.R;
import com.lphaindia.dodapp.dodapp.accessibilityFeatures.TapAccessibilityService;
import com.lphaindia.dodapp.dodapp.accessibilityFeatures.TapAnalytics;
import com.lphaindia.dodapp.dodapp.data.Product;
import com.lphaindia.dodapp.dodapp.overlays.CarouselOverlay;

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
        itemHolder.setImgProductImage(item.imageUrl, item.aspectRatio);
        itemHolder.setProductPrice(item.sellingPrice);
        itemHolder.setUrl(item.productUrl);
        itemHolder.setProductMerchant(item.affiliate);
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

    public class VerticalItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimpleDraweeView mImgProductImage;
        private Button mBtnProductPrice;
        private TextView mTxtMerchantName;
        private String mUrl;
        private CarouselAdapter mAdapter;
        public VerticalItemHolder(View itemView, CarouselAdapter adapter) {
            super(itemView);
            itemView.setOnClickListener(this);
            mAdapter = adapter;
            mImgProductImage = (SimpleDraweeView) itemView.findViewById(R.id.product_image);
            mBtnProductPrice = (Button) itemView.findViewById(R.id.product_price);
            mTxtMerchantName = (TextView)itemView.findViewById(R.id.product_merchant);
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

        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }

        public void setProductPrice(CharSequence productPrice) {
                mBtnProductPrice.setText("₹ " +productPrice);
        }
        public void setProductMerchant(String merchant) {
            mTxtMerchantName.setText(merchant.toUpperCase());
        }
        public void setImgProductImage(String productImage, String aspectRatioStr){
            if (aspectRatioStr != null) {
                float aspectRatio = Float.valueOf(aspectRatioStr);
                mImgProductImage.setAspectRatio(aspectRatio);
                GenericDraweeHierarchyBuilder builder =
                        new GenericDraweeHierarchyBuilder(mCtxt.getResources());
                GenericDraweeHierarchy hierarchy = builder
                        .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                        .build();
                mImgProductImage.setImageURI(Uri.parse(productImage));
                mImgProductImage.setHierarchy(hierarchy);
            }
        }
        public void setUrl(String url){
            mUrl = url;
        }
    }
}
