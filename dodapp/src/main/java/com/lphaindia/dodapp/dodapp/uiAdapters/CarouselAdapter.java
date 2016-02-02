package com.lphaindia.dodapp.dodapp.uiAdapters;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;


import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
        View root = inflater.inflate(R.layout.carousal_item, container, false);
        return new VerticalItemHolder(root, this);
    }

    @Override
    public void onBindViewHolder(VerticalItemHolder itemHolder, int position) {
        Product item = mProductList.get(position);
        itemHolder.setImgProductImage(item.imageUrl, item.aspectRatio);
        itemHolder.setProductPrice(item.sellingPrice, item.discountPercentage, item.maximumRetailPrice);
        itemHolder.setUrl(item.productUrl);
        itemHolder.setProductMerchant(item.affiliateLogo);
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
        private TextView mTxtProductPrice;
        private SimpleDraweeView mImgMerchantName;
        private String mUrl;
        private TextView mTextProductMrp;
        private TextView mTextProductDiscount;
        private ImageView mImgDisc;
        private ImageView mBtnCta;
        private CarouselAdapter mAdapter;
        public VerticalItemHolder(View itemView, CarouselAdapter adapter) {
            super(itemView);
            itemView.setOnClickListener(this);
            mAdapter = adapter;
            mImgProductImage = (SimpleDraweeView) itemView.findViewById(R.id.product_image);
            mTxtProductPrice = (TextView) itemView.findViewById(R.id.product_price);
            mImgMerchantName = (SimpleDraweeView) itemView.findViewById(R.id.product_merchant);
            mImgDisc = (ImageView)itemView.findViewById(R.id.imgDiscount);
            mBtnCta = (ImageView)itemView.findViewById(R.id.btnCta);
            mTextProductDiscount = (TextView) itemView.findViewById(R.id.product_discount);
            mTextProductMrp = (TextView) itemView.findViewById(R.id.product_mrp);
            mImgDisc = (ImageView)itemView.findViewById(R.id.imgDiscount);

            mBtnCta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TapAnalytics.sendAnalyticsBuyProduct(TapAccessibilityService.mTracker);
                    if (mUrl != null) {
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
        public void setProductPrice(String productPrice, String discount, String mrp) {
            if (Float.parseFloat(productPrice) <= 0) {
                mTxtProductPrice.setText("FREE");
            } else {
                if (Float.parseFloat(discount) > 0) {
                    mTextProductMrp.setVisibility(View.VISIBLE);
                    mTextProductDiscount.setVisibility(View.VISIBLE);
                    mTextProductDiscount.setPivotX(0);
                    mTextProductDiscount.setPivotY(0);
                    mTextProductDiscount.setRotation(-50);
                    mImgDisc.setVisibility(View.VISIBLE);
                    mTextProductMrp.setText("₹ " + mrp);
                    mTextProductMrp.setPaintFlags(mTextProductMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    mTextProductDiscount.setText(discount + "% OFF");
                }else{
                    mTextProductMrp.setVisibility(View.GONE);
                    mTextProductDiscount.setVisibility(View.GONE);
                    mImgDisc.setVisibility(View.GONE);
                }

            }
            mTxtProductPrice.setText("₹ " + productPrice);
        }

        public void setProductMerchant(String merchant) {
            if(merchant != null) {
                GenericDraweeHierarchyBuilder builder =
                        new GenericDraweeHierarchyBuilder(mCtxt.getResources());
                GenericDraweeHierarchy hierarchy = builder
                        .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                        .build();
                mImgMerchantName.setImageURI(Uri.parse(merchant));
                mImgMerchantName.setHierarchy(hierarchy);
            }
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
