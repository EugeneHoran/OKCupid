package exercise.okcupid.com.search;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import exercise.okcupid.com.R;
import exercise.okcupid.com.util.Common;

public class SearchPagerAdapter extends FragmentPagerAdapter {


    private String[] titleList;
    private FragmentManager fm;

    private Fragment[] fragments;

    SearchPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.fm = fm;
        titleList = context.getResources().getStringArray(R.array.tab_titles);
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments != null && fragments.length > position && fragments[position] != null) {
            return fragments[position];
        }
        if (fragments == null) {
            fragments = new Fragment[getCount()];
        }
        switch (position) {
            case 0:
                fragments[0] = SearchFragment.newInstance(Common.WHICH_BLEND);
            case 1:
                fragments[1] = SearchFragment.newInstance(Common.WHICH_MATCH);
        }
        return fragments[position];
    }

    @Override
    public int getCount() {
        return titleList.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList[position];
    }


    /**
     * @return all the cached fragments used by this Adapter.
     */
    Fragment[] getFragments() {
        if (fragments == null) {
            // Force creating the fragments
            int count = getCount();
            for (int i = 0; i < count; i++) {
                getItem(i);
            }
        }
        return fragments;
    }

    /**
     * When the device changes orientation, the fragments are recreated
     * by the system, and they have the same tag ids as the ones previously used. Therefore, this
     * sets the cached fragments to the ones recreated by the system. This must be called before any
     * call to {@link #getItem(int)} or {@link #getFragments()} (note that when fragments are
     * recreated after orientation change, the {@link FragmentPagerAdapter} doesn't call {@link
     * #getItem(int)}.)
     *
     * @param tags the tags of the retained fragments. Ignored if null
     *             or empty.
     */
    void setRetainedFragmentsTags(String[] tags) {
        if (tags != null && tags.length > 0) {
            fragments = new SearchFragment[tags.length];
            for (int i = 0; i < tags.length; i++) {
                SearchFragment fragment = (SearchFragment) fm.findFragmentByTag(tags[i]);
                fragments[i] = fragment;
                if (fragment == null) {
                    getItem(i);
                }
            }
        }
    }
}
