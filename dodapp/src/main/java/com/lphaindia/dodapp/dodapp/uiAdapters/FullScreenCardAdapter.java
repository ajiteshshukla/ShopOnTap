package com.lphaindia.dodapp.dodapp.uiAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.lphaindia.dodapp.dodapp.R;
import com.lphaindia.dodapp.dodapp.data.Product;

import java.util.List;

/**
 * Created by aasha.medhi on 10/7/15.
 */
public class FullScreenCardAdapter extends PagerAdapter {
    private List<Product> mProductList;
    private Context mCtxt;
    private TextView mTextProductName;
    private TextView mTextProductPrice;
    private TextView mTextProductMrp;
    private TextView mTextProductDiscount;
    private ImageView mImgDisc;
    private SimpleDraweeView mImgProductImage;
    private SimpleDraweeView mLogoMerchantName;
    private ImageView mBtnCta;

    private AdapterView.OnItemClickListener mOnItemClickListener;

    public FullScreenCardAdapter(Context context, List<Product> productList) {
        this.mProductList = productList;
        this.mCtxt = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(mCtxt);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fullscreen_card_view, container, false);
        initView(layout, mProductList.get(position), position);
        container.addView(layout);
        return layout;
    }

    public void initView(ViewGroup itemView, final Product product, final int position) {
        mTextProductName = (TextView) itemView.findViewById(R.id.product_title);
        mTextProductPrice = (TextView) itemView.findViewById(R.id.product_price);
        mLogoMerchantName = (SimpleDraweeView) itemView.findViewById(R.id.product_merchant);
        mTextProductMrp = (TextView) itemView.findViewById(R.id.product_mrp);
        mTextProductDiscount = (TextView) itemView.findViewById(R.id.product_discount);
        mImgProductImage = (SimpleDraweeView) itemView.findViewById(R.id.product_image);
        mImgDisc = (ImageView)itemView.findViewById(R.id.imgDiscount);
        setImgProductImage(product.imageUrl, product.aspectRatio);
        setProductPrice(product.sellingPrice, product.discountPercentage, product.maximumRetailPrice);
        setProductName(product.title);
        setProductMerchantLogo(product.affiliateLogo);
        mBtnCta = (ImageView)itemView.findViewById(R.id.btnCta);
        mBtnCta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(product.productUrl));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mCtxt.startActivity(i);
            }
        });
    }
    @Override
    public void destroyItem(View container, int position, Object obj) {
        ((ViewPager) container).removeView((View) obj);
    }

    @Override
    public int getCount() {
        return mProductList.size();
    }

    @Override
    public boolean isViewFromObject(View container, Object obj) {
        return container == obj;
    }

    public void setProductPrice(String productPrice, String discount, String mrp) {
        if (Float.parseFloat(productPrice) <= 0) {
            mTextProductPrice.setText("FREE");
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
            mTextProductPrice.setText("₹ " + productPrice);

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