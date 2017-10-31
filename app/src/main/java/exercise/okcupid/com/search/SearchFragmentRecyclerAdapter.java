package exercise.okcupid.com.search;


import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import exercise.okcupid.com.R;
import exercise.okcupid.com.databinding.RecyclerSearchItemBinding;
import exercise.okcupid.com.model.UpdateDataHolder;
import exercise.okcupid.com.model.UserData;
import exercise.okcupid.com.util.Common;

public class SearchFragmentRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<UserData> userDataList = new ArrayList<>();
    private boolean itemRemoved = false;
    private Integer itemRemovedPosition = null;

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
    }

    /**
     * Handle data changes from Match Fragment
     * <p>
     * Used to animate views and setList
     */
    void notifyDataChangedMatch(List<UserData> userData) {
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
                return; // Return to prevent item insert if old list and new list = 0
            }
            // Item Removed and Item Added
            notifyItemRemoved(itemRemovedPosition);
            notifyItemInserted(getItemCount() - 1);
            itemRemoved = false;
            itemRemovedPosition = null;
        } else {
            this.userDataList.clear();
            this.userDataList.addAll(userData);
            notifyDataSetChanged();
        }
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
            String uId = userData.getUserId();
            itemRemovedPosition = getAdapterPosition();
            itemRemoved = true;
            UserData.setOnLike(uId);
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
}
