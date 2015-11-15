/*
 * OnBootService Class is an: Android Service that boots with the System to run 
 * ServiceSetAlarm so the set alarm won't be lost.
 */

package com.aicpa.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

	public class OnBootReceiver extends BroadcastReceiver {

		 public void onReceive(Context context, Intent intent) {
			 Log.d("OnBootService", "onReceive");
			 if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
				 Intent serviceIntent = new Intent(context, AlarmReceiver.class);
				 serviceIntent.putExtra("boot", true);
			     context.startService(serviceIntent);
			 }else{
				 Log.e("OnBootService", "Received unexpected intent " + intent.toString());
			 }
		 }
	}