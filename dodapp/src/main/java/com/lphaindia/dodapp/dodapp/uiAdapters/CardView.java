package com.lphaindia.dodapp.dodapp.uiAdapters;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.lphaindia.dodapp.dodapp.R;

public class CardView extends RelativeLayout {
    private ImageView mProductImage;
    private TextView mProductName;
    private Button mProductPrice;
    private TextView mBrandName;

    public CardView(Context context) {
        super(context);
    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mProductImage = (ImageView) findViewById(R.id.product_image);
        mProductName = (TextView) findViewById(R.id.product_name);
        mProductPrice = (Button)findViewById(R.id.product_price);
        mBrandName = (TextView)findViewById(R.id.product_brand);
    }

}
