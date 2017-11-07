package exercise.okcupid.com.search;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import exercise.okcupid.com.R;
import exercise.okcupid.com.TestingLifecycle;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG_FRAGMENT_PAGER = "tag_fragment_pager";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_search);
        FragmentManager manager = getSupportFragmentManager();
        SearchPagerFragment searchPagerFragment = (SearchPagerFragment) manager.findFragmentByTag(TAG_FRAGMENT_PAGER);
        if (searchPagerFragment == null) {
            manager.beginTransaction()
                    .replace(R.id.container, SearchPagerFragment.newInstance(), TAG_FRAGMENT_PAGER)
                    .commit();
        }
        TestingLifecycle testingLifecycle = new TestingLifecycle(getLifecycle());
        getLifecycle().addObserver(testingLifecycle);
    }
}
