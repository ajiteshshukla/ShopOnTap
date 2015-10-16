package com.lphaindia.dodapp.dodapp.uiAdapters;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lphaindia.dodapp.dodapp.R;
import com.lphaindia.dodapp.dodapp.data.Category;
import it.gmariotti.cardslib.library.internal.Card;

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
        SimpleDraweeView image = (SimpleDraweeView) view.findViewById(R.id.category_image);
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(mContext.getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                .build();
        image.setImageURI(Uri.parse(mCategory.getImage()));
        image.setHierarchy(hierarchy);
    }

    public Category getCategory() {
        return mCategory;
    }

}
