package de.iweinzierl.easyprofiles.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;

import de.iweinzierl.easyprofiles.ProfileListActivity;
import de.iweinzierl.easyprofiles.R;
import de.iweinzierl.easyprofiles.persistence.Profile;

public class NotificationHelper {

    private final Context context;

    public NotificationHelper(Context context) {
        this.context = context;
    }

    public void publishProfileNotification(Profile profile) {
        Intent startApplicationIntent = new Intent(context, ProfileListActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ProfileListActivity.class);
        stackBuilder.addNextIntent(startApplicationIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(context)
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText(context.getResources().getString(R.string.notification_active_profile) + " " + profile.getName())
                .setSmallIcon(R.drawable.ic_settings_black_24dp)
                .setContentIntent(resultPendingIntent)
                .setOngoing(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
