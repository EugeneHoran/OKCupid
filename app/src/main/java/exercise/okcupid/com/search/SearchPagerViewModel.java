package exercise.okcupid.com.search;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import exercise.okcupid.com.api.OkCupidService;
import exercise.okcupid.com.model.CupidData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import retrofit2.Response;

public class SearchPagerViewModel extends ViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    private Realm realm = Realm.getDefaultInstance();
    private CupidData cupidData;
    public ObservableField<Boolean> dataLoaded = new ObservableField<>(false);
    public ObservableField<Boolean> showProgress = new ObservableField<>(true);
    private MutableLiveData<Boolean> showError;

    public SearchPagerViewModel() {
    }

    /**
     * Listen for error changes
     *
     * @return boolean showError
     */
    MutableLiveData<Boolean> getShowError() {
        if (showError == null) {
            showError = new MutableLiveData<>();
        }
        return showError;
    }

    /**
     * Load and Cache Data
     */
    void initData() {
        dataInitiated();
        disposables.add(OkCupidService.create()
                .userList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<CupidData>>() {
                    @Override
                    public void onNext(final Response<CupidData> searchResultResponse) {
                        if (searchResultResponse.isSuccessful()) {
                            cupidData = searchResultResponse.body();
                        } else {
                            showError();
                        }
                    }

                    @Override
                    public void onComplete() {
                        // Cache data in Realm
                        if (realm.isClosed()) {
                            realm = Realm.getDefaultInstance();
                        }
                        try {
                            realm.executeTransaction(realm -> {
                                realm.insertOrUpdate(cupidData.getUserList());
                                dataSuccess();
                            });
                        } finally {
                            if (realm != null) {
                                realm.close();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showError();
                    }
                }));
    }

    /**
     * Request data called
     */
    private void dataInitiated() {
        disposables.clear();
        showProgress.set(true);
        dataLoaded.set(false);
    }

    /**
     * Data success
     * Handle Views
     */
    private void dataSuccess() {
        getShowError().setValue(false);
        showProgress.set(false);
        dataLoaded.set(true);
    }

    /**
     * Data Error
     * Handle Views
     */
    private void showError() {
        dataLoaded.set(false);
        showProgress.set(false);
        getShowError().setValue(true);
    }

    /**
     * onDestroy
     */
    @Override
    protected void onCleared() {
        disposables.clear();
        if (!realm.isClosed()) {
            realm.close();
        }
    }
}
