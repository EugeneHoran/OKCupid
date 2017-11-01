package exercise.okcupid.com.search;


import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import exercise.okcupid.com.model.UpdateDataHolder;
import exercise.okcupid.com.model.UserData;
import exercise.okcupid.com.util.Common;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class SearchFragmentViewModel extends ViewModel implements OrderedRealmCollectionChangeListener<RealmResults<UserData>> {
    private int whichFragment;
    private final Realm realm = Realm.getDefaultInstance();
    private RealmResults<UserData> userData;
    private MutableLiveData<UpdateDataHolder> liveAllUsers;
    private MutableLiveData<List<UserData>> liveFilteredUsers;


    SearchFragmentViewModel(int whichFragment) {
        this.whichFragment = whichFragment;
        userData = realm.where(UserData.class).findAll();
        userData.addChangeListener(this);
        if (whichFragment == Common.WHICH_BLEND) {
            getAllUsersDataChanges().setValue(new UpdateDataHolder(userData, Common.NOTIFY_CHANGE, null, null));
        }
        if (whichFragment == Common.WHICH_MATCH) {
            getFilteredUsersDataChanges().setValue(filter(userData));
        }
    }

    /**
     * LiveData for Blend Fragment
     *
     * @return UpdateDataHolder
     */
    MutableLiveData<UpdateDataHolder> getAllUsersDataChanges() {
        if (liveAllUsers == null) {
            liveAllUsers = new MutableLiveData<>();
        }
        return liveAllUsers;
    }

    /**
     * LiveData for Match Fragment
     *
     * @return List<UserData>
     */
    MutableLiveData<List<UserData>> getFilteredUsersDataChanges() {
        if (liveFilteredUsers == null) {
            liveFilteredUsers = new MutableLiveData<>();
        }
        return liveFilteredUsers;
    }

    /**
     * Listen for Realm Database changes on RealmResults<UserData>
     *
     * @param userData  {@link UserData}
     * @param changeSet handles where data was changed
     */
    @Override
    public void onChange(@NonNull RealmResults<UserData> userData, @Nullable OrderedCollectionChangeSet changeSet) {
        switch (whichFragment) {
            case Common.WHICH_BLEND:
                handleChangeAll(userData, changeSet);
                break;
            case Common.WHICH_MATCH:
                handleFilterChanges(userData);
                break;
        }
    }

    /**
     * Update MutableLiveData<UpdateDataHolder>  getAllUsersDataChanges()
     * Listener inside of {@link SearchFragment}
     *
     * @param userData  RealmResults<UserData>
     * @param changeSet handles where data was changed
     */
    private void handleChangeAll(@NonNull RealmResults<UserData> userData, @Nullable OrderedCollectionChangeSet changeSet) {
        UpdateDataHolder updateDataHolder = null;
        if (changeSet == null) {
            updateDataHolder = new UpdateDataHolder(userData, Common.NOTIFY_CHANGE, null, null);
            getAllUsersDataChanges().setValue(updateDataHolder);
            return;
        }
        OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
        for (int i = deletions.length - 1; i >= 0; i--) {
            OrderedCollectionChangeSet.Range range = deletions[i];
            updateDataHolder = new UpdateDataHolder(userData, Common.NOTIFY_RANGE_REMOVE, range.startIndex, range.length);
        }
        OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
        for (OrderedCollectionChangeSet.Range range : insertions) {
            updateDataHolder = new UpdateDataHolder(userData, Common.NOTIFY_RANGE_INSERT, range.startIndex, range.length);
        }
        OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
        for (OrderedCollectionChangeSet.Range range : modifications) {
            updateDataHolder = new UpdateDataHolder(userData, Common.NOTIFY_RANGE_CHANGE, range.startIndex, range.length);
        }
        getAllUsersDataChanges().setValue(updateDataHolder);
    }

    /**
     * Update  MutableLiveData<List<UserData>> getFilteredUsersDataChanges()
     * Listener inside of {@link SearchFragment}
     *
     * @param userData RealmResults<UserData>
     */
    private void handleFilterChanges(@NonNull RealmResults<UserData> userData) {
        getFilteredUsersDataChanges().setValue(filter(userData));
    }

    /**
     * Sort and Query liked items
     *
     * @param userData all users
     * @return filtered list of likes
     */
    private List<UserData> filter(RealmResults<UserData> userData) {
        userData = userData.sort("match", Sort.DESCENDING);
        RealmQuery<UserData> query = userData.where();
        query = query.equalTo("isLiked", true);
        int listSize = query.findAll().size() > 6 ? 6 : query.findAll().size();
        return query.findAll().subList(0, listSize);
    }

    /**
     * OnDestroy
     */
    @Override
    protected void onCleared() {
        userData.removeAllChangeListeners();
        if (!realm.isClosed()) {
            realm.close();
        }
    }

    /**
     * A creator is used to inject the whichFragment into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        private final int whichFragment;

        Factory(int whichFragment) {
            this.whichFragment = whichFragment;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new SearchFragmentViewModel(whichFragment);
        }
    }
}
