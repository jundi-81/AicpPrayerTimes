package com.aicpa.main;

import com.aicpa.R;
import com.aicpa.manager.Constants;
import com.aicpa.manager.MobileManager;
import com.aicpa.manager.MyMediaPlayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

	private MyMediaPlayer mPlayer;

	@Override
	public void onReceive(Context context, Intent intent) {
		// Log.i("Alarm Receiver", intent.getBooleanExtra("boot", false) + "");
		if (!intent.getBooleanExtra("boot", false))
			playAzan(context);
		createAnotherTask(context);
	}

	private void createAnotherTask(final Context context) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				android.os.SystemClock.sleep(60000);
				MobileManager.createTask(context);
			}
		};
		new Thread(r).start();
	}

	public void playAzan(Context context) {
		try {
			int player_id = MobileManager.getAzanType(context);
			mPlayer = new MyMediaPlayer(context);
			switch (player_id) {
			case -1:
				return;
			case 0:
				mPlayer.start(R.raw.beep);
				break;
			case 1:
				mPlayer.start(R.raw.notification1);
				break;
			case 2:
				mPlayer.start(R.raw.notification2);
				break;
			case 3:
				mPlayer.startPlaying(context.getFilesDir() + "/"
						+ Constants.KHEIR_AZAN_ALL_FILE_1);
				break;
			case 4:
				mPlayer.startPlaying(context.getFilesDir() + "/"
						+ Constants.KARANOUH_AZAN_ALL_FILE_1);
				break;
			case 5:
				mPlayer.startPlaying(context.getFilesDir() + "/"
						+ Constants.FAYED_AZAN_ALL_FILE_1);
				break;
			case 6:
				mPlayer.startPlaying(context.getFilesDir() + "/"
						+ Constants.DABLIZ_AZAN_ALL_FILE_1);
				break;
			case 7:
				mPlayer.startPlaying(context.getFilesDir() + "/"
						+ Constants.JUNDI_AZAN_ALL_FILE_1);
				break;
			}
			try {
				int prayer = MobileManager.getCurrentPrayer();
				String title = context.getString(R.string.PrayerEntered), message = "";
				if (prayer != -1) {
					message = context.getResources().getStringArray(
							R.array.prayers)[prayer];
				}
				new MyNotification()
						.createNotification(context, title, message);
			} catch (Exception ex) {
			}
		} catch (Exception e) {
		}
	}

}