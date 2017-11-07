package exercise.okcupid.com;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

public class TestingLifecycle implements LifecycleObserver {
    private Lifecycle lifecycle;

    public TestingLifecycle(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        Log.e("testing", "onResume");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        Log.e("testing", "onPause");
    }
}
