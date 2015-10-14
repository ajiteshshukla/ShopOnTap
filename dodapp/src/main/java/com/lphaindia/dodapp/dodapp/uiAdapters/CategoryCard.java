package com.lphaindia.dodapp.dodapp.uiAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.lphaindia.dodapp.dodapp.R;
import com.lphaindia.dodapp.dodapp.data.Category;
import com.squareup.picasso.Picasso;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;

/**
 * Created by aasha.medhi on 10/7/15.
 */
public class CategoryCard extends Card {
    private Category mCategory;
    public CategoryCard(Context context, Category category) {
        super(context, R.layout.category_card_view);
        this.mCategory = category;
        //init();
    }

    private void init() {

        //CardThumbnail thumbnail = new CardThumbnail(getContext());
//        if(mCategory.getImage() != null)
//            thumbnail.setUrlResource(mCategory.getImage());
//        //addCardThumbnail(thumbnail);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        super.setupInnerViewElements(parent, view);
        ImageView image = (ImageView) view.findViewById(R.id.category_image);
        Picasso.with(mContext).load(mCategory.getImage()).fit().into(image);
    }

    public Category getCategory(){
        return mCategory;
    }

}
