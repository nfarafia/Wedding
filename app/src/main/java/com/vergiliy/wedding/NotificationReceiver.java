package com.vergiliy.wedding;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.vergiliy.wedding.budget.BudgetActivity;
import com.vergiliy.wedding.budget.payment.Payment;
import com.vergiliy.wedding.budget.payment.PaymentDatabase;

import java.util.Calendar;
import java.util.List;


import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationReceiver extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        Log.e("AlarmNotification", "NotificationReceiver -> onReceive");

        // Set chosen locale
        BaseLocale locale = new BaseLocale(context);
        if (locale.isChangeLanguage(locale.getLanguage())) {
            locale.setLocale(); // Set chosen locale
        }

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        // Check Notification between 9:00 and 21:00
        if (hour >= 9 && hour <= 21) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

            Boolean notificationPayment = preferences.getBoolean("notification_payment", true);
            Log.e("notificationPayment", notificationPayment + "");
            if (notificationPayment) {
                sendPaymentNotification(); // Send payment notification
            }
        }
    }

    // Send payment notification
    private void sendPaymentNotification() {
        PaymentDatabase db_payment = new PaymentDatabase(context);

        // Get Payments
        List<Payment> payments = db_payment.getAllForNotification();

        int count = payments.size();
        String ending =  count > 1 ? "s" : "";

        if (count == 0)
            return;

        String title = context.getString(R.string.payment_notification_title, count, ending);
        String text = context.getString(R.string.payment_notification_text, ending);
        String[] id = new String[count];
        String[] name = new String[count];
        for (int i = 0; i < count; i++) {
            Payment payment = payments.get(i);
            id[i] = String.valueOf(payment.getId());
            name[i] = payment.getLocaleName().toLowerCase();
        }
        text += " " + TextUtils.join(", ", name);

        // Send notification
        sendNotification(BudgetActivity.class, title, text, count);

        // Update Payments (mark as notified)
        db_payment.setNotification(id);
    }

    private void sendNotification(Class<?> cls, String title, String text, int count) {
        sendNotification(cls, title, title, text, count);
    }

    // Send notification
    private void sendNotification(Class<?> cls, String ticker, String title, String text, int count) {
        // Create Intent
        Intent notificationIntent = new Intent(context, cls);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification)
                .setTicker(ticker)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(text)
                .setDefaults(Notification.DEFAULT_ALL)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
                .setNumber(count);

        // If lengths bigger than 30 letters, change the style to multiline
        if (text.length() > 30) {
            builder.setStyle(new NotificationCompat.BigTextStyle().bigText(text));
        }

        // Sent notification
        NotificationManager manager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(1, builder.build());

        // Turn on screen
        wakeUp();
    }

    // Wake up display
    private void wakeUp() {
        PowerManager manager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock =
                manager.newWakeLock(PowerManager.FULL_WAKE_LOCK
                        | PowerManager.ACQUIRE_CAUSES_WAKEUP
                        | PowerManager.ON_AFTER_RELEASE, "Tag");
        wakeLock.acquire();
        wakeLock.release();
    }
}