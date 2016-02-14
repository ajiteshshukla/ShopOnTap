package com.lphaindia.dodapp.dodapp.uiAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
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
    private boolean mIsLoadingFooterAdded = false;

    public ProductCardAdapter(Context context, List<Product> productList) {
        this.mProductList = productList;
        this.mCtxt = context;
    }

    private void add(Product item) {
        mProductList.add(item);
        notifyItemInserted(mProductList.size()-1);
    }

    public void addAll(List<Product> videos) {
        for (Product video : videos) {
            add(video);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_card_view, null);
//        CustomViewHolder viewHolder = new CustomViewHolder(view);
//        return viewHolder;

        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case ITEM:
                viewHolder = createProductViewHolder(viewGroup);
                break;
            case LOADING:
                viewHolder = createLoadingViewHolder(viewGroup);
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
            default:
                break;
        }
    }

//    @Override
//    public void onBindViewHolder(CustomViewHolder holder, int position) {
//        Product product = mProductList.get(position);
//        holder.setImgProductImage(product.imageUrl, product.aspectRatio);
//        holder.setProductPrice(product.sellingPrice, product.discountPercentage, product.maximumRetailPrice);
//        holder.setProductName(product.title);
//        holder.setProductLandingPage(product.productUrl);
//        holder.setProductMerchantLogo(product.affiliateLogo);
//    }

    @Override
    public int getItemCount() {
        return (null != mProductList ? mProductList.size() : 0);
    }

    @Override
    public int getItemViewType(int position) {
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
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card_view, parent, false);

        final ProductViewHolder holder = new ProductViewHolder(v);

//        holder.mVideoRowRootLinearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int adapterPos = holder.getAdapterPosition();
//                if(adapterPos != RecyclerView.NO_POSITION){
//                    if (mOnItemClickListener != null) {
//                        mOnItemClickListener.onItemClick(adapterPos, holder.itemView);
//                    }
//                }
//            }
//        });

        return holder;
    }

    private RecyclerView.ViewHolder createLoadingViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more, parent, false);
        return new MoreViewHolder(v);
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
        //MoreViewHolder holder = (MoreViewHolder) viewHolder;
        //holder.mLoadingImageView.setMaskOrientation(LoadingImageView.MaskOrientation.LeftToRight);
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
                }else{
                    mTextProductMrp.setVisibility(View.GONE);
                    mTextProductDiscount.setVisibility(View.GONE);
                    mImgDisc.setVisibility(View.GONE);
                    title.append("₹ " + productPrice);
                }

            }
            mTextProductPrice.setText(title.toString());
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

        }
    }

    public static class MoreViewHolder extends RecyclerView.ViewHolder {
       // LoadingImageView mLoadingImageView;

        public MoreViewHolder(View view) {
            super(view);
            //mLoadingImageView = (LoadingImageView)view.findViewById(R.id.loading_iv);
        }
    }
}