package com.tobishiba.circularviewpager.library;

import android.support.v4.view.ViewPager;

/**
 * User: tobiasbuchholz
 * Date: 18.09.14 | Time: 12:08
 */
public class CircularViewPagerHandler implements ViewPager.OnPageChangeListener {
    public static final int                 SET_ITEM_DELAY = 300;

    private ViewPager                       mViewPager;
    private ViewPager.OnPageChangeListener  mListener;

    public CircularViewPagerHandler(final ViewPager viewPager) {
        mViewPager = viewPager;
        mViewPager.setCurrentItem(1, false);
    }

    public void setOnPageChangeListener(final ViewPager.OnPageChangeListener listener) {
        mListener = listener;
    }

    @Override
    public void onPageSelected(final int position) {
        handleSetCurrentItemWithDelay(position);
        invokeOnPageSelected(position);
    }

    private void handleSetCurrentItemWithDelay(final int position) {
        mViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                handleSetCurrentItem(position);
            }
        }, SET_ITEM_DELAY);
    }

    private void handleSetCurrentItem(final int position) {
        final int lastPosition = mViewPager.getAdapter().getCount() - 1;
        if(position == 0) {
            mViewPager.setCurrentItem(lastPosition - 1, false);
        } else if(position == lastPosition) {
            mViewPager.setCurrentItem(1, false);
        }
    }

    private void invokeOnPageSelected(final int position) {
        if(mListener != null) {
            mListener.onPageSelected(position - 1);
        }
    }

    @Override
    public void onPageScrollStateChanged(final int state) {
        invokeOnPageScrollStateChanged(state);
    }

    private void invokeOnPageScrollStateChanged(final int state) {
        if(mListener != null) {
            mListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
        invokeOnPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    private void invokeOnPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
        if(mListener != null) {
            mListener.onPageScrolled(position - 1, positionOffset, positionOffsetPixels);
        }
    }
}
