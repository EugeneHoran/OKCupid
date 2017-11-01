package exercise.okcupid.com.search;


import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import exercise.okcupid.com.R;
import exercise.okcupid.com.databinding.RecyclerSearchItemBinding;
import exercise.okcupid.com.model.UpdateDataHolder;
import exercise.okcupid.com.model.UserData;
import exercise.okcupid.com.util.Common;
import exercise.okcupid.com.util.SearchDifferCallback;
import io.realm.Realm;

public class SearchFragmentRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UserData> userDataList = new ArrayList<>();
    private boolean isClickable = true;
    // TODO remove once DiffUtil layout issues are fixed
//    private boolean itemRemoved = false;
//    private Integer itemRemovedPosition = null;

    /**
     * Handle data changes from Blend Fragment
     * <p>
     * Used to animate views and setList
     * <p>
     * TODO get this to work with DiffUtil
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
        // Cool find
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        this.userDataList.clear();
        this.userDataList.addAll(userData);
        diffResult.dispatchUpdatesTo(this);
        isClickable = true;

        /*
         * How I was originally animating layout changes
         *
         * Came across DiffUtil while looking at Recent Support Library Revisions
         * So far I like it, but had flaws while implementing with Blend item changes.
         *
         * One of the problems that DiffUtil brought was, when data changed the layout height would also change.
         * I calculated the height in dp and set the height via xml rather than wrap_content.
         */

        // TODO Remove code after I fix DiffUtl layout issues
/*_______________________________________________________________________________________________*/
       /*
       if (itemRemoved) { // only true if item is removed
            int oldList = getItemCount();
            int newList = userData.size();
            this.userDataList.clear();
            this.userDataList.addAll(userData);
            // Item Removed or No Items
            if (newList == 0 || newList < oldList) {
                notifyItemRemoved(itemRemovedPosition);
                itemRemoved = false;
                itemRemovedPosition = null;
                isClickable = true;
                return; // Return to prevent item insert if old list and new list = 0
            }
            // Item Removed and Item Added
            notifyItemRemoved(itemRemovedPosition);
            notifyItemInserted(getItemCount() - 1);
            itemRemoved = false;
            itemRemovedPosition = null;
            isClickable = true;
        } else {
            this.userDataList.clear();
            this.userDataList.addAll(userData);
            notifyDataSetChanged();
            isClickable = true;
        }
        */
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
            binding.executePendingBindings();
            binding.setImageUrl(userData.getPhoto().getFullPaths().getLarge());
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
//                itemRemoved = true;
//                itemRemovedPosition = getAdapterPosition();

                isClickable = false;
                String uId = userData.getUserId();
                UserData.setOnLike(uId);
            }
        }

        /**
         * Animate CardView on click, elevate on touch
         */
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private void setAnimator() {
            StateListAnimator sla = AnimatorInflater.loadStateListAnimator(binding.getRoot().getContext(), R.drawable.touch_elevation);
            binding.cardView.setStateListAnimator(sla);
        }
    }

    @SuppressWarnings("unused")
    private void setOnLike(String id) {
        if (isClickable) {
            return;
        }
        Realm realm = Realm.getDefaultInstance();
        final UserData userDataUpdate = realm.where(UserData.class).contains("userId", id).findFirst();
        if (userDataUpdate == null) {
            realm.close();
            return;
        }
        try {
            realm.executeTransaction(new Realm.Transaction() {
                @SuppressWarnings("ConstantConditions")
                @Override
                public void execute(@Nonnull Realm realm) {
                    userDataUpdate.setLiked(!userDataUpdate.getLiked());
                    realm.copyToRealmOrUpdate(userDataUpdate);
                }
            });
        } finally {
            realm.close();
        }
    }
}
