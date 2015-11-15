/*
 * AlertActivity Class : implements OnCompletionListener beside extending Activity
 * because we need to finish() the activity once the Sound is completed
 *   
 */
package com.aicpa.main;

import com.aicpa.helper.Typefaces;
import com.aicpa.manager.MobileManager;
import com.aicpa.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class AlertActivity extends Activity implements OnCompletionListener {
	private MediaPlayer mPlayer;
	WakeLock wakeLock ;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.alert);

		/*
		 * This code together with the one in onDestroy() will make the screen
		 * be always on until this Activity destroyed.
		 */
		
		Intent intent = this.getIntent();
		if(intent.getBooleanExtra("runFromService", false) == false)
		{
			Intent intent2 = new Intent(this,MainActivity.class);
			this.startActivity(intent2);
			this.finish();
		}else{
			intent.putExtra("runFromService", true);
		}
		 PowerManager pm = (PowerManager) this.getApplicationContext().getSystemService(Context.POWER_SERVICE);
	        wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
	       wakeLock.acquire();
	       getWindow().addFlags(
                   WindowManager.LayoutParams.FLAG_FULLSCREEN
                           | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//Manager.acquireScreen(this);

		String ardroid = getString(R.string.azandoaa);

		TextView azanDoaa = (TextView) findViewById(R.id.azandoaa);
		azanDoaa.setTypeface(Typefaces.get(this.getBaseContext(), "fonts/DroidNaskh-Regular.ttf"));
		azanDoaa.setText(ardroid);

		try {
			int sound = MobileManager.getAzanType(getApplicationContext());
	/*		mPlayer = MediaPlayer.create(AlertActivity.this, R.raw.notification);
			mPlayer.setOnCompletionListener(AlertActivity.this);
			mPlayer.start();*/
			

		} catch (Exception e) {

		}

		Button b = (Button) findViewById(R.id.button2);
		b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try{
				mPlayer.stop();
				}catch (Exception ex){}
				finish();
			}
		});

	}

	@Override
	public void onDestroy() {
		//Manager.releaseScreen(this);
		wakeLock.release();
		try{
			this.mPlayer.stop();
		}catch (Exception ex){}
		super.onDestroy();
	}

	public void onCompletion(MediaPlayer mp) {
		Intent intent2 = new Intent(this,MainActivity.class);
		this.startActivity(intent2);
		this.finish();
		}

	public void onStop(){
		super.onStop();
		// TODO :
		// we might need to finish() this activity and set a flag runFromService from here.. ?
	}
}
