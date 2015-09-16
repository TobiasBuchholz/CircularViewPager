package com.tobishiba.circularviewpager.activities;

import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.tobishiba.circularviewpager.R;
import com.tobishiba.circularviewpager.adapters.MemeCircularViewPagerAdapter;
import com.tobishiba.circularviewpager.inappbilling.GoogleIabHelper;
import com.tobishiba.circularviewpager.library.CircularViewPagerHandler;
import com.tobishiba.circularviewpager.models.Meme;


public class MainActivity extends FragmentActivity {
    private GoogleIabHelper mIabHelper;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initInAppBillingHelper();
        initActionBar();
        initViews();
    }

    private void initInAppBillingHelper() {
        mIabHelper = new GoogleIabHelper(this);
        mIabHelper.setUpServiceConnection();
    }

    private void initActionBar() {
        final ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.blue)));
        }
    }

    private void initViews() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.activity_main_view_pager);
        viewPager.setAdapter(new MemeCircularViewPagerAdapter(this, getFragmentManager(), Meme.createSampleMemes()));
        final CircularViewPagerHandler circularViewPagerHandler = new CircularViewPagerHandler(viewPager);
        circularViewPagerHandler.setOnPageChangeListener(createOnPageChangeListener());
        viewPager.addOnPageChangeListener(circularViewPagerHandler);
    }

    private ViewPager.OnPageChangeListener createOnPageChangeListener() {
        final TextView currentPageText = (TextView) findViewById(R.id.activity_main_current_page_text);
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
                currentPageText.setText(getString(R.string.current_page, position));
            }
            @Override
            public void onPageSelected(final int position) {}
            @Override
            public void onPageScrollStateChanged(final int state) {}
        };
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_beer) {
            mIabHelper.purchaseItem(this, "beer_donation");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIabHelper.unbindServiceConnection();
    }
}
