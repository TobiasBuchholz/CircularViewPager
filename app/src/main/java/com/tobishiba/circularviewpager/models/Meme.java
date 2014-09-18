package com.tobishiba.circularviewpager.models;

import com.tobishiba.circularviewpager.R;

import java.util.ArrayList;
import java.util.List;

/**
 * User: tobiasbuchholz
 * Date: 18.09.14 | Time: 11:02
 */
public class Meme {
    public String   mTitle;
    public int      mImageResourceId;

    public static List<Meme> createSampleMemes() {
        final List<Meme> memes = new ArrayList<Meme>();
        memes.add(new Meme("This ViewPager", R.drawable.meme_wondering));
        memes.add(new Meme("is scrolling", R.drawable.meme_determined));
        memes.add(new Meme("endless", R.drawable.meme_crying));
        return memes;
    }

    public Meme(final String title, final int imageResourceId) {
        mTitle = title;
        mImageResourceId = imageResourceId;
    }
}
