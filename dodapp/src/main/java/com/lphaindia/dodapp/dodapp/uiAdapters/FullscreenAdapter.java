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
import android.widget.TextView;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lphaindia.dodapp.dodapp.R;
import com.lphaindia.dodapp.dodapp.data.Product;
import com.lphaindia.dodapp.dodapp.overlays.FullScreenOverlay;

import java.util.ArrayList;

public class FullscreenAdapter extends RecyclerView.Adapter<FullscreenAdapter.CustomViewHolder> {

    private ArrayList<Product> mProductList;
    private Context mCtxt;
    private AdapterView.OnItemClickListener mOnItemClickListener;

    public FullscreenAdapter(Context ctxt) {
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
    public CustomViewHolder onCreateViewHolder(ViewGroup container, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View root = inflater.inflate(R.layout.fullscreen_view_item, container, false);
        return new CustomViewHolder(root);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Product product = mProductList.get(position);
        holder.setImgProductImage(product.imageUrl, product.aspectRatio);
        holder.setProductPrice(product.sellingPrice, product.discountPercentage, product.maximumRetailPrice);
        holder.setProductName(product.title);
        holder.setProductLandingPage(product.productUrl);
        holder.setProductMerchant(product.affiliate);
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextProductName;
        private TextView mTextProductPrice;
        private TextView mTextProductMrp;
        private SimpleDraweeView mImgProductImage;
        private String mProductLandingPage;
        private SimpleDraweeView mLogoMerchantName;

        public CustomViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTextProductName = (TextView) itemView.findViewById(R.id.product_title);
            mTextProductPrice = (TextView) itemView.findViewById(R.id.product_price);
            mLogoMerchantName = (SimpleDraweeView) itemView.findViewById(R.id.product_merchant);
            mTextProductMrp = (TextView) itemView.findViewById(R.id.product_mrp);
            mImgProductImage = (SimpleDraweeView) itemView.findViewById(R.id.product_image);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(mProductLandingPage));
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mCtxt.startActivity(i);
            FullScreenOverlay.getInstance(mCtxt).removeOverlay();
        }

        public void setProductPrice(String productPrice, String discount, String mrp) {
            StringBuilder title = new StringBuilder();
            if (Float.parseFloat(productPrice) <= 0) {
                title.append("FREE");
            } else {
                if (Float.parseFloat(discount) > 0) {
                    mTextProductMrp.setVisibility(View.VISIBLE);
                    mTextProductMrp.setText("₹ " + mrp);
                    mTextProductMrp.setPaintFlags(mTextProductMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    title.append(productPrice);
                }else{
                    mTextProductMrp.setVisibility(View.GONE);
                    title.append("₹ " + productPrice);
                }

            }
            mTextProductPrice.setText(title.toString());
        }

        public void setProductMerchant(String merchant) {
            GenericDraweeHierarchyBuilder builder =
                    new GenericDraweeHierarchyBuilder(mCtxt.getResources());
            GenericDraweeHierarchy hierarchy = builder
                    .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                    .build();
            String hardcoded = "http://techclones.com/wp-content/uploads/2014/11/Flipkart-app.png";
            mLogoMerchantName.setImageURI(Uri.parse(hardcoded));
            mLogoMerchantName.setHierarchy(hierarchy);
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
}
