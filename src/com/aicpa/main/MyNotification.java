package com.aicpa.main;

import com.aicpa.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class MyNotification {

	public MyNotification() {

	}

	public void createNotification(Context context, String title, String message) {
		try {
			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification notification = new Notification(R.drawable.ic_launcher,
					message, System.currentTimeMillis());

			Intent notificationIntent = new Intent(context, MainActivity.class);
			notificationIntent.putExtra("where", "notification");
			
			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);

			PendingIntent intent = PendingIntent.getActivity(context, 0,
					notificationIntent, 0);

			notification
					.setLatestEventInfo(context, title, message, intent);
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notificationManager.notify(0, notification);
		} catch (Exception ex) {
		}
	}
}
