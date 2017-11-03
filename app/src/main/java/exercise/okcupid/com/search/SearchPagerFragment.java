package exercise.okcupid.com.search;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import exercise.okcupid.com.R;
import exercise.okcupid.com.databinding.FragmentSearchPagerBinding;

public class SearchPagerFragment extends Fragment {
    public SearchPagerFragment() {
    }

    public static SearchPagerFragment newInstance() {
        return new SearchPagerFragment();
    }

    private FragmentSearchPagerBinding binding;
    public SearchPagerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new SearchPagerAdapter(getActivity(), getChildFragmentManager());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_pager, container, false);
        binding.setFragment(this);
        binding.setModel(ViewModelProviders.of(this).get(SearchPagerViewModel.class));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeErrorChanges(binding.getModel());
        if (savedInstanceState == null) {
            binding.getModel().initData();
        }
    }

    /**
     * Listen for errors in Api call and Saving data
     *
     * @param model {@link SearchPagerViewModel}
     */
    private void observeErrorChanges(final SearchPagerViewModel model) {
        model.getShowError().observe(this, aBoolean -> {
            if (aBoolean != null) {
                if (aBoolean) {
                    Snackbar.make(binding.mainContent, "Loading error", Snackbar.LENGTH_INDEFINITE).setAction("Retry", view -> model.initData()).show();
                }
            }
        });
    }

    private SearchFragment getSearchFragment(int position) {
        SearchFragment searchFragment = (SearchFragment) adapter.getFragments()[position];
        if (searchFragment != null) {
            return searchFragment;
        }
        return null;
    }

    /**
     * Expand AppBar on new page selected
     * Scroll to top if current tab is selected
     */
    public TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            binding.appbar.setExpanded(true, true);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            binding.appbar.setExpanded(true, true);
            if (getSearchFragment(tab.getPosition()) != null) {
                //noinspection ConstantConditions
                getSearchFragment(tab.getPosition()).scrollToTop();
            }
        }
    };
}
