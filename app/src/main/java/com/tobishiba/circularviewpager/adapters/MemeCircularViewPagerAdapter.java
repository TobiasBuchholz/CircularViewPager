package com.tobishiba.circularviewpager.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import com.tobishiba.circularviewpager.fragments.MemeViewPagerItemFragment;
import com.tobishiba.circularviewpager.library.BaseCircularViewPagerAdapter;
import com.tobishiba.circularviewpager.models.Meme;

import java.util.List;

/**
 * User: tobiasbuchholz
 * Date: 18.09.14 | Time: 10:59
 */
public class MemeCircularViewPagerAdapter extends BaseCircularViewPagerAdapter<Meme> {
    private final Context mContext;

    public MemeCircularViewPagerAdapter(final Context context, final FragmentManager fragmentManager, final List<Meme> memes) {
        super(fragmentManager, memes);
        mContext = context;
    }

    @Override
    protected Fragment getFragmentForItem(final Meme meme) {
        return MemeViewPagerItemFragment.instantiateWithArgs(mContext, meme);
    }
}
