package com.tobishiba.circularviewpager.library;

import android.support.v4.app.FixedFragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

/**
 * User: tobiasbuchholz
 * Date: 18.09.14 | Time: 13:18
 */
public abstract class BaseCircularViewPagerAdapter<Item> extends FixedFragmentStatePagerAdapter {
    private List<Item> mItems;

    public BaseCircularViewPagerAdapter(final FragmentManager fragmentManager, final List<Item> items) {
        super(fragmentManager);
        mItems = items;
    }

    protected abstract Fragment getFragmentForItem(final Item item);

    @Override
    public Fragment getItem(final int position) {
        final int itemsSize = mItems.size();
        if(position == 0) {
            return getFragmentForItem(mItems.get(itemsSize - 1));
        } else if(position == itemsSize + 1) {
            return getFragmentForItem(mItems.get(0));
        } else {
            return getFragmentForItem(mItems.get(position - 1));
        }
    }

    @Override
    public int getCount() {
        final int itemsSize = mItems.size();
        return itemsSize > 1 ? itemsSize + 2 : itemsSize;
    }

    public int getCountWithoutFakePages() {
        return mItems.size();
    }

    public void setItems(final List<Item> items) {
        mItems = items;
        notifyDataSetChanged();
    }
}
