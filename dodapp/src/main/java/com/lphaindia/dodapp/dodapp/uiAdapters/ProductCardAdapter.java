package com.lphaindia.dodapp.dodapp.uiAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.lphaindia.dodapp.dodapp.R;
import com.lphaindia.dodapp.dodapp.data.Product;

import java.util.List;

/**
 * Created by aasha.medhi on 10/7/15.
 */
public class ProductCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Product> mProductList;
    private Context mCtxt;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    public static final int ITEM = 1;
    public static final int LOADING = 2;
    public static final int ADS = 3;
    private boolean mIsLoadingFooterAdded = false;

    public ProductCardAdapter(Context context, List<Product> productList) {
        this.mProductList = productList;
        this.mCtxt = context;
    }

    private void add(Product item) {
        mProductList.add(item);
        notifyItemInserted(mProductList.size()-1);
    }

    private void add(int index, Product item) {
        mProductList.add(index, item);
        notifyItemInserted(index-1);
    }

    public void addAll(List<Product> products) {
        for (Product product : products) {
            add(product);
        }
    }

    public void removeAll() {
        mProductList.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case ITEM:
                viewHolder = createProductViewHolder(viewGroup);
                break;
            case LOADING:
                viewHolder = createLoadingViewHolder(viewGroup);
                break;
            case ADS:
                viewHolder = createAdsViewHolder(viewGroup);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ITEM:
                bindProductViewHolder(holder, position);
                break;
            case LOADING:
                bindLoadingViewHolder(holder);
                break;
            case ADS:
                bindAdsViewHolder(holder);
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return (null != mProductList ? mProductList.size() : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if(position!= 0 && position % 6 == 0)
            return ADS;
        return (position == mProductList.size()-1 && mIsLoadingFooterAdded) ? LOADING : ITEM;
    }

    public void remove(Product item) {
        int position = mProductList.indexOf(item);
        if (position > -1) {
            mProductList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        mIsLoadingFooterAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoading(){
        mIsLoadingFooterAdded = true;
        add(new Product());
    }

    public void addAds(int index){
        add(index, new Product());
    }

    public void removeLoading() {
        if(mProductList.size() > 0) {
            mIsLoadingFooterAdded = false;
            int position = mProductList.size() - 1;
            Product item = getItem(position);

            if (item != null) {
                mProductList.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    public Product getItem(int position) {
        return mProductList.get(position);
    }


    private RecyclerView.ViewHolder createProductViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card_view, parent, false);
        final ProductViewHolder holder = new ProductViewHolder(v);
        return holder;
    }

    private RecyclerView.ViewHolder createLoadingViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more, parent, false);
        return new LoadingViewHolder(v);
    }

    private RecyclerView.ViewHolder createAdsViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ads, parent, false);
        return new AdsViewHolder(v);
    }

    private void bindProductViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final ProductViewHolder holder = (ProductViewHolder) viewHolder;
        Product product = mProductList.get(position);
        holder.setImgProductImage(product.imageUrl, product.aspectRatio);
        holder.setProductPrice(product.sellingPrice, product.discountPercentage, product.maximumRetailPrice);
        holder.setProductName(product.title);
        holder.setProductLandingPage(product.productUrl);
        holder.setProductMerchantLogo(product.affiliateLogo);
    }

    private void bindLoadingViewHolder(RecyclerView.ViewHolder viewHolder){
    }

    private void bindAdsViewHolder(RecyclerView.ViewHolder viewHolder){
        final AdsViewHolder holder = (AdsViewHolder)viewHolder;
        AdRequest adRequest = new AdRequest.Builder().build();
        holder.mAdView.loadAd(adRequest);
    }


    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextProductName;
        private TextView mTextProductPrice;
        private TextView mTextProductMrp;
        private TextView mTextProductDiscount;
        private ImageView mImgDisc;
        private SimpleDraweeView mImgProductImage;
        private String mProductLandingPage;
        private SimpleDraweeView mLogoMerchantName;
        private ImageView mBtnCta;

        public ProductViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTextProductName = (TextView) itemView.findViewById(R.id.product_title);
            mTextProductPrice = (TextView) itemView.findViewById(R.id.product_price);
            mLogoMerchantName = (SimpleDraweeView) itemView.findViewById(R.id.product_merchant);
            mTextProductMrp = (TextView) itemView.findViewById(R.id.product_mrp);
            mTextProductDiscount = (TextView) itemView.findViewById(R.id.product_discount);
            mImgProductImage = (SimpleDraweeView) itemView.findViewById(R.id.product_image);
            mImgDisc = (ImageView)itemView.findViewById(R.id.imgDiscount);
            mBtnCta = (ImageView)itemView.findViewById(R.id.btnCta);
            mBtnCta.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(mProductLandingPage));
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mCtxt.startActivity(i);
        }

        public void setProductPrice(String productPrice, String discount, String mrp) {
            StringBuilder title = new StringBuilder();
            try {
                if (Float.parseFloat(productPrice) <= 0) {
                    title.append("FREE");
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
                        title.append(productPrice);
                    } else {
                        mTextProductMrp.setVisibility(View.GONE);
                        mTextProductDiscount.setVisibility(View.GONE);
                        mImgDisc.setVisibility(View.GONE);
                        title.append("₹ " + productPrice);
                    }

                }
                mTextProductPrice.setText(title.toString());
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public void setProductMerchantLogo(String merchant) {
            if(merchant != null) {
                GenericDraweeHierarchyBuilder builder =
                        new GenericDraweeHierarchyBuilder(mCtxt.getResources());
                GenericDraweeHierarchy hierarchy = builder
                        .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                        .build();
                mLogoMerchantName.setImageURI(Uri.parse(merchant));
                mLogoMerchantName.setHierarchy(hierarchy);
            }
        }

        public void setProductName(CharSequence productName) {
            mTextProductName.setText(productName);
        }

        public void setProductLandingPage(String productLandingPage) {
            mProductLandingPage = productLandingPage;
        }

        public void setImgProductImage(String productImage, String aspectRatiostr) {
            try {
                if (aspectRatiostr != null) {
                    float aspectRatio = Float.valueOf(aspectRatiostr);
                    mImgProductImage.setAspectRatio(aspectRatio);
                }
                GenericDraweeHierarchyBuilder builder =
                        new GenericDraweeHierarchyBuilder(mCtxt.getResources());
                GenericDraweeHierarchy hierarchy = builder
                        .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                        .build();
                mImgProductImage.setImageURI(Uri.parse(productImage));
                mImgProductImage.setHierarchy(hierarchy);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {
       // LoadingImageView mLoadingImageView;

        public LoadingViewHolder(View view) {
            super(view);
            //mLoadingImageView = (LoadingImageView)view.findViewById(R.id.loading_iv);
        }
    }
    public static class AdsViewHolder extends RecyclerView.ViewHolder {
        AdView mAdView;

        public AdsViewHolder(View view) {
            super(view);
            mAdView = (AdView)view.findViewById(R.id.adView);
        }
    }
}