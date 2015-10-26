package com.lphaindia.dodapp.dodapp.uiAdapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lphaindia.dodapp.dodapp.R;
import com.lphaindia.dodapp.dodapp.data.Product;

import java.util.List;

/**
 * Created by aasha.medhi on 10/7/15.
 */
public class ProductCardAdapter extends RecyclerView.Adapter<ProductCardAdapter.CustomViewHolder> {
    private List<Product> mProductList;
    private Context mContext;
    private AdapterView.OnItemClickListener mOnItemClickListener;

    public ProductCardAdapter(Context context, List<Product> productList) {
        this.mProductList = productList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_card_view, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view, this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Product product = mProductList.get(position);
        StringBuilder title = new StringBuilder();
        if (Float.parseFloat(product.sellingPrice) <= 0) {
            title.append("FREE");
        } else {
            String sellingPrice = product.sellingPrice;
            if (sellingPrice.contains("."))
                sellingPrice = sellingPrice.substring(0, sellingPrice.indexOf("."));
            if (Float.parseFloat(product.discountPercentage) > 0) {
                int discount = Math.round(Float.parseFloat(product.discountPercentage));
                title.append(product.currency + " " + sellingPrice + "          " + discount + "% OFF");
            } else {
                title.append(product.currency + " " + sellingPrice);
            }
        }
        String productTitle = product.title;
        if (productTitle.length() > 45) {
            productTitle = productTitle.substring(0, 42);
            productTitle = productTitle.concat("...");
        }
        holder.setProductName(productTitle);
        holder.setImgProductImage(product.imageUrl, product.aspectRatio);
        holder.setProductCost(title.toString());
        holder.setProductLandingPage(product.productUrl);
    }

    @Override
    public int getItemCount() {
        return (null != mProductList ? mProductList.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextProductName;
        private TextView mTextProductCost;
        private SimpleDraweeView mImgProductImage;
        private String mProductLandingPage;

        public CustomViewHolder(View itemView, ProductCardAdapter adapter) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTextProductName = (TextView) itemView.findViewById(R.id.product_name);
            mTextProductCost = (TextView) itemView.findViewById(R.id.product_cost);
            mImgProductImage = (SimpleDraweeView) itemView.findViewById(R.id.product_image);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(mProductLandingPage));
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
        }

        public void setProductName(CharSequence productName) {
            mTextProductName.setText(productName);
        }

        public void setProductCost(CharSequence productCost) { mTextProductCost.setText(productCost); }

        public void setProductLandingPage(String productLandingPage) {
            mProductLandingPage = productLandingPage;
        }

        public void setImgProductImage(String productImage, String aspectRatioStr) {
            if (aspectRatioStr != null) {
                float aspectRatio = Float.valueOf(aspectRatioStr);
                mImgProductImage.setAspectRatio(aspectRatio);
                GenericDraweeHierarchyBuilder builder =
                        new GenericDraweeHierarchyBuilder(mContext.getResources());
                GenericDraweeHierarchy hierarchy = builder
                        .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                        .build();
                mImgProductImage.setImageURI(Uri.parse(productImage));
                mImgProductImage.setHierarchy(hierarchy);
            }
        }
    }
}