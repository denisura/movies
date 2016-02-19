package net.uraganov.rubric.sync;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class RubricSyncService extends Service {

    private final String LOG_TAG = RubricSyncService.class.getSimpleName();

    private static final Object sSyncAdapterLock = new Object();
    private static RubricSyncAdapter sSunshineSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d(LOG_TAG, "onCreate - RubricSyncService");
        synchronized (sSyncAdapterLock) {
            if (sSunshineSyncAdapter == null) {
                sSunshineSyncAdapter = new RubricSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSunshineSyncAdapter.getSyncAdapterBinder();
    }
}