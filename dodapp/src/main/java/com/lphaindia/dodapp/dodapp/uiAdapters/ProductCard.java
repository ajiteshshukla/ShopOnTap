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
import it.gmariotti.cardslib.library.view.CardViewNative;

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
        MaterialLargeImageCard card =
                        MaterialLargeImageCard.with(mContext)
                                .setTitle(mProduct.sellingPrice)
                                .setSubTitle(mProduct.sellingPrice)
                                .setTextOverImage(mProduct.title)
                                .useDrawableExternal(new MaterialLargeImageCard.DrawableExternal() {
                                    @Override
                                    public void setupInnerViewElements(ViewGroup parent, View viewImage) {
                                        Picasso.with(mContext)
                                                .load(mProduct.imageUrl)
                                                .into((ImageView) viewImage);
                                    }
                                })
                                .build();
        CardViewNative cardView = (CardViewNative)view.findViewById(R.id.carddemo_largeimage);
        cardView.setCard(card);
    }
}
