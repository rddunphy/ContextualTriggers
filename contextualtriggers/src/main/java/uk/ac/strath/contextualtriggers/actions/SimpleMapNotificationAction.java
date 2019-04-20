package uk.ac.strath.contextualtriggers.actions;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import uk.ac.strath.contextualtriggers.Logger;
import uk.ac.strath.contextualtriggers.MainApplication;
import uk.ac.strath.contextualtriggers.R;
import uk.ac.strath.contextualtriggers.conditions.NotificationHistoryCondition;
import uk.ac.strath.contextualtriggers.managers.NotificationDataManager;

//import static android.support.v4.content.ContextCompat.getSystemService;

public class SimpleMapNotificationAction implements Action {

    private static final String CHANNEL_ID = "contextualtriggers";
    private String message;
    private Logger logger;
    private NotificationHistoryCondition notifyCondition;

    public SimpleMapNotificationAction(String message) {
        this.message = message;
        logger = Logger.getInstance();
        createNotificationChannel();
    }



    @Override
    public void execute() {
        Intent cs = new Intent(MainApplication.getAppContext(), NotificationDataManager.class);
        MainApplication.getAppContext().startService(cs);
        if (notifyCondition != null) {
            notifyCondition.notifyUpdate(null);
        }
        Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194?z=10&q=restaurants");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        PendingIntent pIntent = PendingIntent.getActivity(MainApplication.getAppContext(),0,mapIntent,0);
        logger.log("*** SENDING NOTIFICATION ***\n\"" + message + "\"");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainApplication.getAppContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.powered_by_google_dark)
                .setContentTitle("Notification")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainApplication.getAppContext());
// notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "contextualtriggers";
            String description = "contextualtriggers channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) MainApplication.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void attachCondition(NotificationHistoryCondition notifyCondition) {
        this.notifyCondition = notifyCondition;
    }

}
