package com.lphaindia.dodapp.dodapp.uiAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import com.lphaindia.dodapp.dodapp.R;
import com.lphaindia.dodapp.dodapp.data.Product;
import com.squareup.picasso.Picasso;

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
        if (Float.parseFloat(product.discountPercentage) > 0) {
            int discount = Math.round(Float.parseFloat(product.discountPercentage));
            title.append(product.currency + " " + product.sellingPrice + "          " + discount + "% OFF");
        } else {
            title.append(product.currency + " " + product.sellingPrice);
        }
        holder.setContext(mContext);
        holder.setImgProductImage(product.imageUrl);
        holder.setProductName(title.toString());
        holder.setProductLandingPage(product.productUrl);
        holder.setAspectRatio(product.aspectRatio);
    }

    @Override
    public int getItemCount() {
        return (null != mProductList ? mProductList.size() : 0);
    }


    public static class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextProductName;
        private ImageView mImgProductImage;
        private String mProductLandingPage;
        private String mAspectRatio;
        private Context mCtxt;
        private ProductCardAdapter mAdapter;
        public CustomViewHolder(View itemView, ProductCardAdapter adapter) {
            super(itemView);
            itemView.setOnClickListener(this);
            mAdapter = adapter;
            mTextProductName = (TextView) itemView.findViewById(R.id.product_title);
            mImgProductImage = (ImageView) itemView.findViewById(R.id.product_image);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(mProductLandingPage));
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mCtxt.startActivity(i);
        }

        public void setProductName(CharSequence productName) {
            mTextProductName.setText(productName);
        }

        public void setProductLandingPage(String productLandingPage) {
            mProductLandingPage = productLandingPage;
        }

        public void setAspectRatio(String aspectRatio) {
            mAspectRatio = aspectRatio;
        }

        public void setContext(Context ctxt) {
            mCtxt = ctxt;
        }

        public void setImgProductImage(String productImage) {
            if(mAspectRatio != null) {
                float aspectRatio = Float.valueOf(mAspectRatio);
                WindowManager windowManager = (WindowManager) mCtxt
                        .getSystemService(Context.WINDOW_SERVICE);
                Display display = windowManager.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                DisplayMetrics metrics = mCtxt.getResources().getDisplayMetrics();
                float width = size.x/metrics.density;
                mImgProductImage.getLayoutParams().height = Math.round(width / aspectRatio);
            }
            Picasso.with(mCtxt).load(productImage).fit().into(mImgProductImage);
            mImgProductImage.requestLayout();
        }
    }
}