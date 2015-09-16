package android.support.v4.app;

import android.os.Bundle;
import android.view.ViewGroup;

public abstract class FixedFragmentStatePagerAdapter extends FragmentStatePagerAdapter {
    
    public FixedFragmentStatePagerAdapter(final FragmentManager fm) {
        super(fm);
    }
    
    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final Fragment f = (Fragment) super.instantiateItem(container, position);
        final Bundle savedFragmentState = f.mSavedFragmentState;
        if (savedFragmentState != null) {
            savedFragmentState.setClassLoader(f.getClass().getClassLoader());
        }
        return f;
    }
    
}
