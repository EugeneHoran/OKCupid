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
    private Fragment[] fragments;

    SearchPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
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
}
