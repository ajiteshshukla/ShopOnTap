package com.lphaindia.dodapp.dodapp.uiAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.lphaindia.dodapp.dodapp.Product.Product;
import com.lphaindia.dodapp.dodapp.R;
import com.lphaindia.dodapp.dodapp.overlays.CarouselOverlay;
import com.lphaindia.dodapp.dodapp.overlays.FullScreenOverlay;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FullscreenAdapter extends RecyclerView.Adapter<FullscreenAdapter.VerticalItemHolder> {

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
    public VerticalItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View root = inflater.inflate(R.layout.fullscreen_view_item, container, false);
        return new VerticalItemHolder(root, this);
    }

    @Override
    public void onBindViewHolder(VerticalItemHolder itemHolder, int position) {
        Product item = mProductList.get(position);
        itemHolder.setContext(this.mCtxt);
        itemHolder.setImgProductImage(item.imageUrl);
        itemHolder.setProductName(item.title);
        itemHolder.setProductPrice(item.maximumRetailPrice);
        itemHolder.setBrandName(item.brand);
        itemHolder.setOfferPrice(item.sellingPrice);
        itemHolder.setMRP(item.maximumRetailPrice);
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
        private TextView mTextOfferPrice;
        private TextView mTextMaxPrice;
        private String mUrl;
        private FullscreenAdapter mAdapter;
        private LinearLayout mMaxPriceLayout;
        private LinearLayout mOfferPriceLayout;
        private Context mCtxt;
        public VerticalItemHolder(View itemView, FullscreenAdapter adapter) {
            super(itemView);
            itemView.setOnClickListener(this);
            mAdapter = adapter;
            mTextProductName = (TextView) itemView.findViewById(R.id.product_name);
            mImgProductImage = (ImageView) itemView.findViewById(R.id.product_image);
            mTextBrandName = (TextView)itemView.findViewById(R.id.product_brand);
            mTextOfferPrice = (TextView)itemView.findViewById(R.id.product_offer_price);
            mTextMaxPrice = (TextView)itemView.findViewById(R.id.product_max_price);
            mTextMaxPrice.setPaintFlags(mTextMaxPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            mOfferPriceLayout = (LinearLayout)itemView.findViewById(R.id.layout_offer_price);
            mMaxPriceLayout = (LinearLayout)itemView.findViewById(R.id.layout_max_price);
            mBtnProductPrice = (Button) itemView.findViewById(R.id.product_price);
            mBtnProductPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mUrl != null){
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(mUrl));
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mCtxt.startActivity(i);
                        FullScreenOverlay.getInstance(mCtxt).removeOverlay();
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
        public void setOfferPrice(CharSequence color) {
            if(null == color) {
                mOfferPriceLayout.setVisibility(View.GONE);
            }
            else {
                mOfferPriceLayout.setVisibility(View.VISIBLE);
                mTextOfferPrice.setText(color);
            }
        }
        public void setMRP(CharSequence size) {
            if(null == size) {
                mMaxPriceLayout.setVisibility(View.GONE);
            }else {
                mMaxPriceLayout.setVisibility(View.VISIBLE);
                mTextMaxPrice.setText(size);
            }
        }
        public void setContext(Context ctxt){
            mCtxt = ctxt;
        }
        public void setImgProductImage(String productImage){
            Picasso.with(mCtxt).load(productImage).into(mImgProductImage);
        }
        public void setUrl(String url){
            mUrl = url;
        }
    }
}
