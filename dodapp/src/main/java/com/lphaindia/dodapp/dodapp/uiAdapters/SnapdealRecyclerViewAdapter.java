package com.lphaindia.dodapp.dodapp.uiAdapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.lphaindia.dodapp.dodapp.AppConstants;
import com.lphaindia.dodapp.dodapp.Product.Product;
import com.lphaindia.dodapp.dodapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ajitesh.shukla on 9/19/15.
 */
public class SnapdealRecyclerViewAdapter extends RecyclerView.Adapter<SnapdealRecyclerViewAdapter.DataObjectHolder> {

    private List<Product> mDataset;
    private Context context;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {

        private TextView title;
        private TextView discount;
        private Button cost;
        private ImageView imageView;
        private Product product;
        private Context mContext;
        private String productUrl;

        public DataObjectHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.textviewtitle);
            discount = (TextView) itemView.findViewById(R.id.textviewdiscount);
            cost = (Button) itemView.findViewById(R.id.buttoncost);
            imageView = (ImageView) itemView.findViewById(R.id.productimage);
            //Log.d(AppConstants.TAG, "Adding Listener");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (productUrl != null) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(productUrl));
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(i);
                        //TapAnalytics.sendAnalyticsBuyProduct(TapAccessibilityService.mTracker);
                    }
                }
            });
        }

        public void setInfo(Product product, Context context) {
            mContext = context;
            this.product = product;
            title.setText(product.title.substring(0, product.title.length() > 20 ? (20) : (product.title.length()-1)));
            discount.setText("Discount: " + product.discountPercentage + "%");
            cost.setText("Rs. " + product.sellingPrice);
            Picasso.with(mContext).load(product.imageUrl).resize(400, 400).into(imageView);
            productUrl = product.productUrl;
        }

        @Override
        public void onClick(View v) {

        }
    }

    public SnapdealRecyclerViewAdapter(List<Product> myDataset, Context  context) {
        mDataset = myDataset;
        this.context = context;
    }

    @Override
    public SnapdealRecyclerViewAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_item, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.setInfo(mDataset.get(position), context);
    }


    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
