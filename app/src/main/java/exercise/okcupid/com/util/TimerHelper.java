package exercise.okcupid.com.util;

import android.databinding.ObservableField;
import android.os.CountDownTimer;

import exercise.okcupid.com.model.UserData;
import io.realm.Realm;

public class TimerHelper {
    private static final int TIMER_LENGTH = 6000;
    private static final int TIMER_INTERVAL = 1000;
    private String uId;
    public ObservableField<String> timeRemaining = new ObservableField<>("5");
    public boolean isRunning = false;

    public TimerHelper(String uId) {
        this.uId = uId;
    }

    public void startTimer() {
        isRunning = true;
        timer.start();
    }

    public void cancelTimer() {
        timer.cancel();
        Realm realm = Realm.getDefaultInstance();
        UserData userDataUpdate = realm.where(UserData.class).contains("userId", uId).findFirst();
        if (userDataUpdate == null) {
            return;
        }
        realm.beginTransaction();
        userDataUpdate.setTimerStarted(false);
        realm.copyToRealmOrUpdate(userDataUpdate);
        realm.commitTransaction();
        realm.close();
        isRunning = false;
    }


    public CountDownTimer timer = new CountDownTimer(TIMER_LENGTH, TIMER_INTERVAL) {

        public void onTick(long millisUntilFinished) {
            isRunning = true;
            timeRemaining.set("" + millisUntilFinished / 1000);
        }

        public void onFinish() {
            Realm realm = Realm.getDefaultInstance();
            UserData userDataUpdate = realm.where(UserData.class).contains("userId", uId).findFirst();
            if (userDataUpdate == null) {
                return;
            }
            realm.beginTransaction();
            userDataUpdate.setLiked(true);
            userDataUpdate.setTimerStarted(false);
            realm.copyToRealmOrUpdate(userDataUpdate);
            realm.commitTransaction();
            realm.close();
            isRunning = false;
        }
    };

    public void onDestroy() {
        timer.cancel();
        isRunning = false;
    }
}
