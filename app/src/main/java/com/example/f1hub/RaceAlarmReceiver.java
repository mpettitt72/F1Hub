package com.example.f1hub;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class RaceAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String raceName = intent.getStringExtra(NotificationHelper.EXTRA_RACE_NAME);
        if (raceName == null) raceName = "The next race";

        NotificationHelper.createChannel(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationHelper.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_flag)
                .setContentTitle("Race Weekend This Weekend!")
                .setContentText(raceName + " is coming up. Don't miss it!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(NotificationHelper.NOTIFICATION_ID, builder.build());
    }
}
