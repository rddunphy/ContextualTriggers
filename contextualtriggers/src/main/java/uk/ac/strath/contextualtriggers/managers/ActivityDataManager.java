package uk.ac.strath.contextualtriggers.managers;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.DetectedActivityResult;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import uk.ac.strath.contextualtriggers.ContextualTriggersService;
import uk.ac.strath.contextualtriggers.Logger;
import uk.ac.strath.contextualtriggers.MainApplication;
import uk.ac.strath.contextualtriggers.data.WeatherData;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.google.android.gms.internal.zzs.TAG;

public class ActivityDataManager extends DataManager<DetectedActivity> implements IDataManager<DetectedActivity> {
    Logger logger;
    private final IBinder binder = new ActivityDataManager.LocalBinder();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        setup();
        return binder;
    }

    public class LocalBinder extends Binder {
        public IDataManager getInstance() {
            return ActivityDataManager.this;
        }
    }
    ActivityDataManager()
    {
        setup();
    }

    private void setup() {
        logger = Logger.getInstance();
    }
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            super.onStart(intent, startId);
            monitor();
            return START_STICKY;
        }


        /*This Could be setup to fire on a transition, instead of a poll*/
        private void monitor() {
                // Permission has already been granted
                Awareness.SnapshotApi.getDetectedActivity(ContextualTriggersService.getGoogleAPIClient())
                        .setResultCallback(new ResultCallback<DetectedActivityResult>() {
                            @Override
                            public void onResult(@NonNull DetectedActivityResult detectedActivityResult) {
                                if (!detectedActivityResult.getStatus().isSuccess()) {
                                    Log.d("ActivityDataManager", "Could not get the current activity.");
                                    return;
                                }
                                 ActivityRecognitionResult ar = detectedActivityResult.getActivityRecognitionResult();
                                DetectedActivity probableActivity = ar.getMostProbableActivity();
                                Log.d("ActivityDataManager", probableActivity.toString());
                                sendUpdate(probableActivity);
                            }
                        });
            }

    }
