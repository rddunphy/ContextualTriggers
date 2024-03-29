package uk.ac.strath.contextualtriggers.actions;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import uk.ac.strath.contextualtriggers.MainApplication;
import uk.ac.strath.contextualtriggers.R;
import uk.ac.strath.contextualtriggers.managers.NotificationDataManager;

public class SimpleNotificationAction implements Action {

    private static final String CHANNEL_ID = "contextualtriggers";
    private String message;

    public SimpleNotificationAction(String message) {
        this.message = message;
        createNotificationChannel();
    }

    @Override
    public void execute() {
        Intent cs = new Intent(MainApplication.getAppContext(), NotificationDataManager.class);
        MainApplication.getAppContext().startService(cs);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainApplication.getAppContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.round_directions_walk_24)
                .setContentTitle("Notification")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainApplication.getAppContext());
        Intent resultIntent = new Intent(Intent.ACTION_PICK);
        resultIntent.setType("activity/keepfit");
        PendingIntent resultPendingIntent = PendingIntent.getActivity(MainApplication.getAppContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
// notificationId is a unique int for each notification that you must define

        notificationManager.notify(0, builder.build());
        Intent cns = new Intent(MainApplication.getAppContext(), NotificationDataManager.class);
        MainApplication.getAppContext().startService(cns);
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

}
