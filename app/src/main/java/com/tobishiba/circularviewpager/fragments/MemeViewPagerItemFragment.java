package com.tobishiba.circularviewpager.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.tobishiba.circularviewpager.R;
import com.tobishiba.circularviewpager.models.Meme;

/**
 * User: tobiasbuchholz
 * Date: 18.09.14 | Time: 11:02
 */
public class MemeViewPagerItemFragment extends Fragment {
    private static final String BUNDLE_KEY_TITLE                = "bundle_key_title";
    private static final String BUNDLE_KEY_IMAGE_RESOURCE_ID    = "bundle_key_image_resource_id";
    private String              mTitle;
    private int                 mImageResourceId;

    public static MemeViewPagerItemFragment instantiateWithArgs(final Context context, final Meme meme) {
        final MemeViewPagerItemFragment fragment = (MemeViewPagerItemFragment) instantiate(context, MemeViewPagerItemFragment.class.getName());
        final Bundle args = new Bundle();
        args.putString(BUNDLE_KEY_TITLE, meme.mTitle);
        args.putInt(BUNDLE_KEY_IMAGE_RESOURCE_ID, meme.mImageResourceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initArguments();
    }

    private void initArguments() {
        final Bundle arguments = getArguments();
        if(arguments != null) {
            mTitle = arguments.getString(BUNDLE_KEY_TITLE);
            mImageResourceId = arguments.getInt(BUNDLE_KEY_IMAGE_RESOURCE_ID);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.meme_view_pager_item_fragment, container, false);
        initViews(view);
        return view;
    }

    private void initViews(final View view) {
        initText(view);
        initImage(view);
    }

    private void initText(final View view) {
        final TextView titleText = (TextView) view.findViewById(R.id.meme_view_pager_item_fragment_text);
        titleText.setText(mTitle);
    }

    private void initImage(final View view) {
        final ImageView backgroundImage = (ImageView) view.findViewById(R.id.meme_view_pager_item_fragment_image);
        backgroundImage.setImageResource(mImageResourceId);
    }
}
