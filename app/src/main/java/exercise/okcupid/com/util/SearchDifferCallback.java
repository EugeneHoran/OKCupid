package exercise.okcupid.com.util;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;

import exercise.okcupid.com.model.UserData;


/**
 * Came across this after looking at the latest Support library updates
 * <p>
 * https://developer.android.com/reference/android/support/v17/leanback/widget/DiffCallback.html
 * <p>
 * another reference
 * https://github.com/mrmike/DiffUtil-sample/blob/master/app/src/main/java/com/moczul/diffutilsample/ActorDiffCallback.java
 */
public class SearchDifferCallback extends DiffUtil.Callback {

    private final List<UserData> oldList;
    private final List<UserData> newList;

    public SearchDifferCallback(List<UserData> oldList, List<UserData> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getUserId().equals(newList.get(newItemPosition).getUserId());
    }


    // This method is called by DiffUtil only if areItemsTheSame()
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final UserData oldItem = oldList.get(oldItemPosition);
        final UserData newItem = newList.get(newItemPosition);
        return oldItem.getLiked() == newItem.getLiked();
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}