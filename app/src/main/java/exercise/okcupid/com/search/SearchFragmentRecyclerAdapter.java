package exercise.okcupid.com.search;


import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import exercise.okcupid.com.R;
import exercise.okcupid.com.databinding.RecyclerSearchItemBinding;
import exercise.okcupid.com.image.details.ImageViewActivity;
import exercise.okcupid.com.model.UpdateDataHolder;
import exercise.okcupid.com.model.UserData;
import exercise.okcupid.com.util.Common;
import exercise.okcupid.com.util.SearchDifferCallback;

public class SearchFragmentRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UserData> userDataList = new ArrayList<>();
    private boolean isClickable = true;

    /**
     * Handle data changes from Blend Fragment
     * <p>
     * Used to animate views and setList
     *
     * @param updateDataHolder {@link UpdateDataHolder}
     */
    void notifyDataChangedBlend(UpdateDataHolder updateDataHolder) {
        this.userDataList.clear();
        this.userDataList.addAll(updateDataHolder.userDataList);
        switch (updateDataHolder.getWhichUpdate()) {
            case Common.NOTIFY_CHANGE:
                notifyDataSetChanged();
                break;
            case Common.NOTIFY_RANGE_REMOVE:
                notifyItemRangeRemoved(updateDataHolder.startRange, updateDataHolder.rangeLength);
                break;
            case Common.NOTIFY_RANGE_INSERT:
                notifyItemRangeInserted(updateDataHolder.startRange, updateDataHolder.rangeLength);
                break;
            case Common.NOTIFY_RANGE_CHANGE:
                notifyItemRangeChanged(updateDataHolder.startRange, updateDataHolder.rangeLength);
                break;
            default:
                notifyDataSetChanged();
                break;
        }
        isClickable = true;
    }

    /**
     * Handle data changes from Match Fragment
     * <p>
     * Used to animate views and setList
     */
    void notifyDataChangedMatch(List<UserData> userData) {
        final SearchDifferCallback diffCallback = new SearchDifferCallback(this.userDataList, userData);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        this.userDataList.clear();
        this.userDataList.addAll(userData);
        diffResult.dispatchUpdatesTo(this);
        isClickable = true;
    }

    @Override
    public int getItemCount() {
        return userDataList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new ViewHolderSearch(RecyclerSearchItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ViewHolderSearch mHolder = (ViewHolderSearch) holder;
        mHolder.bindView();
        holder.itemView.setTag(this);
    }

    /**
     * Search ViewHolder
     */
    public class ViewHolderSearch extends RecyclerView.ViewHolder {
        private RecyclerSearchItemBinding binding;
        private UserData userData;

        private ViewHolderSearch(RecyclerSearchItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bindView() {
            userData = userDataList.get(getAdapterPosition());
            binding.setObject(userData);
            binding.setHolder(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setAnimator();
            }
        }

        /**
         * On CardView click
         * Save Like via Realm @ {@link UserData}
         *
         * @param view CardView
         */
        @SuppressWarnings("unused") // Unused View is required for data binding
        public void onLikeClicked(View view) {
            if (isClickable) {
                isClickable = false;
                String uId = userData.getUserId();
                UserData.setOnLike(uId);
            }
        }

        public View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SearchActivity searchActivity = (SearchActivity) v.getContext();
                Intent intent = new Intent(searchActivity, ImageViewActivity.class);
                intent.putExtra(ImageViewActivity.ARG_IMAGE_URL, userData.getPhoto().getFullPaths().getLarge());
                Pair<View, String> p1 = Pair.create(binding.imageView, ViewCompat.getTransitionName(binding.imageView));
//                Pair<View, String> p2 = Pair.create(binding.cardView, ViewCompat.getTransitionName(binding.cardView));

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(searchActivity, p1);
                searchActivity.startActivity(intent, options.toBundle());
                return true;
            }
        };

        /**
         * Animate CardView on click, elevate on touch
         */
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private void setAnimator() {
            StateListAnimator sla = AnimatorInflater.loadStateListAnimator(binding.getRoot().getContext(), R.drawable.touch_elevation);
            binding.cardView.setStateListAnimator(sla);
        }
    }
}
