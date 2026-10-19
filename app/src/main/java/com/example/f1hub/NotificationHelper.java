package com.example.f1hub;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import com.example.f1hub.data.model.jolpica.Race;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class NotificationHelper {

    static final String CHANNEL_ID    = "f1_race_reminders";
    static final String CHANNEL_NAME  = "Race Reminders";
    static final int    NOTIFICATION_ID = 1001;

    static final String EXTRA_RACE_NAME = "race_name";

    public static void createChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Upcoming F1 race weekend reminders");
            context.getSystemService(NotificationManager.class)
                   .createNotificationChannel(channel);
        }
    }

    public static void scheduleRaceNotification(Context context, Race race) {
        if (race == null || race.date == null || race.raceName == null) return;

        try {
            LocalDate raceDate    = LocalDate.parse(race.date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate notifyDate  = raceDate.minusDays(1);

            if (!notifyDate.isAfter(LocalDate.now())) return;

            long triggerMs = LocalDateTime.of(
                    notifyDate.getYear(), notifyDate.getMonth(), notifyDate.getDayOfMonth(), 9, 0)
                    .toInstant(ZoneOffset.UTC)
                    .toEpochMilli();

            Intent intent = new Intent(context, RaceAlarmReceiver.class);
            intent.putExtra(EXTRA_RACE_NAME, race.raceName);

            PendingIntent pending = PendingIntent.getBroadcast(
                    context, NOTIFICATION_ID, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (am == null) return;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !am.canScheduleExactAlarms()) {
                am.set(AlarmManager.RTC_WAKEUP, triggerMs, pending);
            } else {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerMs, pending);
            }
        } catch (Exception ignored) { }
    }
}
