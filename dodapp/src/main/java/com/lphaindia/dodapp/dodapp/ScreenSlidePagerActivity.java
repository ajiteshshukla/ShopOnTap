package com.lphaindia.dodapp.dodapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * Created by ajitesh.shukla on 9/10/15.
 */
public class ScreenSlidePagerActivity extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 3;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    public static ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    public static PagerAdapter mPagerAdapter;
    public static boolean AccessibilityExplored = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setPageTransformer(true, new DepthPageTransformer());
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        /*LinePageIndicator linePageIndicator = (LinePageIndicator) findViewById(R.id.titleindicator);
        linePageIndicator.setSelectedColor(Color.RED);
        linePageIndicator.setStrokeWidth(2);
        linePageIndicator.setCentered(true);
        linePageIndicator.setGapWidth(30);
        linePageIndicator.setAlpha((float) 0.5);
        linePageIndicator.setLineWidth(50);
        linePageIndicator.setViewPager(mPager);*/
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            this.finish();
            super.onBackPressed();
        } else if (mPager.getCurrentItem() == 2 && AccessibilityExplored == true) {
            this.finish();
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return new ScreenSlidePageFragment();
                case 1: return new ScreenSlidePageFragment2();
                case 2: return new ScreenSlidePageFragment3();
                default: return new ScreenSlidePageFragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
