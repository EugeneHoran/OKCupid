package exercise.okcupid.com.search;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import exercise.okcupid.com.R;
import exercise.okcupid.com.databinding.ActivitySearchBinding;

public class SearchActivity extends AppCompatActivity {
    private static final String STATE_TAG_FRAGMENT = "save_state_tag_fragment";
    private static final String STATE_PAGER_POSITION = "save_state_pager_position";

    private ActivitySearchBinding binding;
    private SearchPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        String[] fragmentTagList = null;
        int currentPagerPosition = 0;
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_TAG_FRAGMENT)) {
                fragmentTagList = savedInstanceState.getStringArray(STATE_TAG_FRAGMENT);
            }
            if (savedInstanceState.containsKey(STATE_PAGER_POSITION)) {
                currentPagerPosition = savedInstanceState.getInt(STATE_PAGER_POSITION);
            }
        }
        setSupportActionBar(binding.toolbar);
        setTitle(R.string.title_activity_search);
        SearchViewModel model = ViewModelProviders.of(this).get(SearchViewModel.class);
        binding.setModel(model);
        adapter = new SearchPagerAdapter(this, getSupportFragmentManager());
        binding.pager.setAdapter(adapter);
        binding.tabs.setupWithViewPager(binding.pager);
        adapter.setRetainedFragmentsTags(fragmentTagList);
        binding.pager.setCurrentItem(currentPagerPosition);
        binding.pager.addOnPageChangeListener(pageListener);
        observeErrorChanges(model);
        if (savedInstanceState == null) {
            model.initData();
        }
    }

    /**
     * Listen for errors in Api call and Saving data
     *
     * @param model {@link SearchViewModel}
     */
    private void observeErrorChanges(final SearchViewModel model) {
        model.getShowError().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean != null) {
                    if (aBoolean) {
                        Snackbar.make(binding.mainContent, "Loading error", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                model.initData();
                            }
                        }).show();
                    }
                }
            }
        });
    }

    /**
     * Save states of ViewPagerAdapter
     */
    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        if (adapter != null && adapter.getFragments() != null) {
            Fragment[] fragment = adapter.getFragments();
            String[] tags = new String[fragment.length];
            for (int i = 0; i < tags.length; i++) {
                tags[i] = fragment[i].getTag();
            }
            outState.putStringArray(STATE_TAG_FRAGMENT, tags);
            outState.putInt(STATE_PAGER_POSITION, binding.pager.getCurrentItem());
        }
    }

    /**
     * Expand AppBar on new page selected
     */
    private ViewPager.SimpleOnPageChangeListener pageListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            binding.appbar.setExpanded(true, true);
        }
    };
}
