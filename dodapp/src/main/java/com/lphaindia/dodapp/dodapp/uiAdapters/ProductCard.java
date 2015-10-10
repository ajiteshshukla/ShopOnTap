package com.lphaindia.dodapp.dodapp.uiAdapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.lphaindia.dodapp.dodapp.R;
import com.lphaindia.dodapp.dodapp.data.Category;
import com.lphaindia.dodapp.dodapp.data.Product;
import com.squareup.picasso.Picasso;
import it.gmariotti.cardslib.library.cards.material.MaterialLargeImageCard;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by aasha.medhi on 10/7/15.
 */
public class ProductCard extends MaterialLargeImageCard {
    private Product mProduct;
    public ProductCard(Context context, Product product) {
        super(context, R.layout.product_card_view);
        this.mProduct = product;
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        super.setupInnerViewElements(parent, view);
        setTextOverImage(mProduct.title);
        ImageView image = (ImageView) view.findViewById(R.id.product_image);
        Picasso.with(mContext).load(mProduct.imageUrl).fit().into(image);

        TextView textPrice = (TextView) view.findViewById(R.id.product_text_price);
        textPrice.setText(mProduct.discountPercentage);

        TextView textAffiliate = (TextView) view.findViewById(R.id.product_text_affiliate);
        textAffiliate.setText(mProduct.affiliate);
    }
}
