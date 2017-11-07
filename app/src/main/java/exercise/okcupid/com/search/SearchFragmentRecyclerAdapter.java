package exercise.okcupid.com.search;


import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import exercise.okcupid.com.databinding.RecyclerSearchItemBinding;
import exercise.okcupid.com.databinding.RecyclerSearchTimerItemBinding;
import exercise.okcupid.com.model.UpdateDataHolder;
import exercise.okcupid.com.model.UserData;
import exercise.okcupid.com.util.Common;
import exercise.okcupid.com.util.SearchDifferCallback;
import exercise.okcupid.com.util.TimerHelper;
import io.realm.Realm;

public class SearchFragmentRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int HOLDER_NORMAL = 0;
    private static final int HOLDER_COUNTDOWN = 1;

    private List<UserData> userDataList = new ArrayList<>();
    private long mLastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL = 250;
    private HashMap<String, TimerHelper> helperHashMap = new HashMap<>();
    private SearchFragment searchFragment;

    void setTimerMap(HashMap<String, TimerHelper> helperHashMap) {
        this.helperHashMap = helperHashMap;
    }

    SearchFragmentRecyclerAdapter(SearchFragment searchFragment) {
        this.searchFragment = searchFragment;
    }

    @Override
    public int getItemViewType(int position) {
        if (userDataList.get(position).getTimerStarted()) {
            return HOLDER_COUNTDOWN;
        } else {
            return HOLDER_NORMAL;
        }
    }

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
        final SearchDifferCallback diffCallback = new SearchDifferCallback(this.userDataList, userData);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        this.userDataList.clear();
        this.userDataList.addAll(userData);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemCount() {
        return userDataList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        switch (viewType) {
            case HOLDER_NORMAL:
                return new ViewHolderSearch(RecyclerSearchItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            case HOLDER_COUNTDOWN:
                return new SearchTimerViewHolder(RecyclerSearchTimerItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolderSearch) {
            ViewHolderSearch mHolder = (ViewHolderSearch) holder;
            mHolder.bindView();
        } else if (holder instanceof SearchTimerViewHolder) {
            SearchTimerViewHolder mHolder = (SearchTimerViewHolder) holder;
            mHolder.bindView();
        }
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
        }

        /**
         * On CardView click
         * Save Like via Realm @ {@link UserData}
         *
         * @param view CardView
         */
        @SuppressWarnings("unused") // Unused View is required for data binding
        public void onLikeClicked(View view) {
            long now = System.currentTimeMillis();
            if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                return;
            }
            mLastClickTime = now;
            if (userData.getLiked()) {
                UserData.setOnLike(userData.getUserId());
                return;
            }
            String uId = userData.getUserId();
            searchFragment.addTimer(uId);
            Realm realm = Realm.getDefaultInstance();
            UserData userDataUpdate = realm.where(UserData.class).contains("userId", uId).findFirst();
            if (userDataUpdate == null) {
                return;
            }
            realm.beginTransaction();
            userDataUpdate.setTimerStarted(true);
            realm.copyToRealmOrUpdate(userDataUpdate);
            realm.commitTransaction();
            realm.close();
        }
    }

    public class SearchTimerViewHolder extends RecyclerView.ViewHolder {
        private RecyclerSearchTimerItemBinding binding;
        private UserData userData;
        private TimerHelper timerHelper;

        private SearchTimerViewHolder(RecyclerSearchTimerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bindView() {
            userData = userDataList.get(getAdapterPosition());
            timerHelper = helperHashMap.get(userData.getUserId());
            binding.setTimer(timerHelper);
            binding.setObject(userData);
            binding.setHolder(this);
            binding.cancelLike.setOnClickListener(v -> {
                timerHelper.cancelTimer();
                helperHashMap.remove(userData.getUserId());
            });
            if (timerHelper != null) {
                if (!timerHelper.isRunning) {
                    timerHelper.startTimer();
                }
            }
        }
    }
}
