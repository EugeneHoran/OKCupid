package exercise.okcupid.com.search;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import exercise.okcupid.com.R;
import exercise.okcupid.com.databinding.FragmentSearchBinding;
import exercise.okcupid.com.util.Common;
import exercise.okcupid.com.util.ItemDecorationSearchColumns;


public class SearchFragment extends Fragment {
    private static final String ARG_WHICH_FRAGMENT = "arg_which_fragment";
    private int whichFragment;

    public SearchFragment() {
    }

    public static SearchFragment newInstance(int whichFragment) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_WHICH_FRAGMENT, whichFragment);
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentSearchBinding binding;
    public SearchFragmentRecyclerAdapter searchRecyclerAdapter;
    public ItemDecorationSearchColumns itemDecorationSearchColumns;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            whichFragment = getArguments().getInt(ARG_WHICH_FRAGMENT);
        }
        searchRecyclerAdapter = new SearchFragmentRecyclerAdapter();
        itemDecorationSearchColumns = new ItemDecorationSearchColumns(getResources().getDimensionPixelSize(R.dimen.space_16), getResources().getInteger(R.integer.grid_span));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        binding.setFragment(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Passing which fragment to SearchFragmentViewModel
        SearchFragmentViewModel.Factory factory = new SearchFragmentViewModel.Factory(whichFragment);
        SearchFragmentViewModel viewModel = ViewModelProviders
                .of(this, factory)
                .get(SearchFragmentViewModel.class);
        // Handle which fragment observers
        switch (whichFragment) {
            case Common.WHICH_BLEND:
                observeAllDataChanges(viewModel);
                break;
            case Common.WHICH_MATCH:
                inflateViewStub(true);
                observeFilterDataChanges(viewModel);
                break;
        }
    }

    /**
     * Inflate No Items ViewStub
     * Only called inside of WHICH_MATCH fragment
     */
    public void inflateViewStub(boolean showStub) {
        if (!binding.stubNoItems.isInflated()) {
            binding.stubNoItems.getViewStub().inflate();
        }
        binding.stubNoItems.getRoot().setVisibility(showStub ? View.VISIBLE : View.GONE);
    }

    /**
     * Observe data change inside of WHICH_BLEND fragment
     *
     * @param viewModel {@link SearchFragmentViewModel}
     */
    private void observeAllDataChanges(final SearchFragmentViewModel viewModel) {
        viewModel.getAllUsersDataChanges().observe(this, updateDataHolder -> {
            if (updateDataHolder != null) {
                searchRecyclerAdapter.notifyDataChangedBlend(updateDataHolder);
            }
        });
    }

    /**
     * Observe data change inside of WHICH_MATCH fragment
     *
     * @param viewModel {@link SearchFragmentViewModel}
     */
    private void observeFilterDataChanges(final SearchFragmentViewModel viewModel) {
        viewModel.getFilteredUsersDataChanges().observe(this, userData -> {
            if (userData != null) {
                inflateViewStub(userData.size() == 0);
                searchRecyclerAdapter.notifyDataChangedMatch(userData);
            } else {
                inflateViewStub(true);
            }
        });
    }

    /**
     * Smooth Scroll to top
     * <p>
     * Called from {@link SearchPagerFragment}
     */
    public void scrollToTop() {
        if (binding.recycler != null) {
            binding.recycler.smoothScrollToPosition(0);
        }
    }
}
