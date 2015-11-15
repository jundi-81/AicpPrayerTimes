package com.aicpa.main;

import java.io.File;

import com.aicpa.R;
import com.aicpa.manager.Constants;
import com.aicpa.manager.DownloadFileFromURL;
import com.aicpa.manager.MobileManager;
import com.aicpa.manager.MyMediaPlayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	private RadioGroup rg;
	private RadioButton rbnothing, rb0, rb1, rb2, rb3, rb4, rb5, rb6, rb7;
	private MyMediaPlayer mPlayer;
	private int selected_azan;

	public static final int progress_bar_type = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.setContentView(R.layout.settings);
		Button ok = (Button) findViewById(R.id.button_ok);
		rg = (RadioGroup) findViewById(R.id.rdbGp1);
		rbnothing = (RadioButton) findViewById(R.id.rdbnothing);
		rb0 = (RadioButton) findViewById(R.id.rdb0);
		rb1 = (RadioButton) findViewById(R.id.rdb1);
		rb2 = (RadioButton) findViewById(R.id.rdb2);
		rb3 = (RadioButton) findViewById(R.id.rdb3);
		rb4 = (RadioButton) findViewById(R.id.rdb4);
		rb5 = (RadioButton) findViewById(R.id.rdb5);
		rb6 = (RadioButton) findViewById(R.id.rdb6);
		rb7 = (RadioButton) findViewById(R.id.rdb7);

		rbnothing.setOnClickListener(myOptionOnClickListener);
		rb0.setOnClickListener(myOptionOnClickListener);
		rb1.setOnClickListener(myOptionOnClickListener);
		rb2.setOnClickListener(myOptionOnClickListener);
		rb3.setOnClickListener(myOptionOnClickListener);
		rb4.setOnClickListener(myOptionOnClickListener);
		rb5.setOnClickListener(myOptionOnClickListener);
		rb6.setOnClickListener(myOptionOnClickListener);
		rb7.setOnClickListener(myOptionOnClickListener);

		mPlayer = new MyMediaPlayer(this.getApplicationContext());

		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int selected = 0;
				int selectedId = rg.getCheckedRadioButtonId();

				// find which radioButton is checked by id
				if (selectedId == rbnothing.getId()) {
					selected = -1;
				} else if (selectedId == rb0.getId()) {
					selected = 0;
				} else if (selectedId == rb1.getId()) {
					selected = 1;
				} else if (selectedId == rb2.getId()) {
					selected = 2;
				} else if (selectedId == rb3.getId()) {
					selected = 3;
				} else if (selectedId == rb4.getId()) {
					selected = 4;
				} else if (selectedId == rb5.getId()) {
					selected = 5;
				} else if (selectedId == rb6.getId()) {
					selected = 6;
				} else if (selectedId == rb7.getId()) {
					selected = 7;
				}
				MobileManager.saveAzanType(getApplicationContext(), selected);
				stopPlaying();
				finish();
			}
		});
		int azan = MobileManager.getAzanType(getApplicationContext());
		selected_azan = azan;
		selectSavedAzan(azan);
		super.onCreate(savedInstanceState);
	}

	private void stopPlaying() {
		try {
			if (mPlayer != null)
				mPlayer.stop();
		} catch (Exception ex) {
		}
	}

	private void startPlaying(int media) {
		mPlayer.start(media);
	}

	private void selectSavedAzan(int index) {
		switch (index) {
		case -1:
			rg.check(R.id.rdbnothing); // no event
			break;
		case 0:
			rg.check(R.id.rdb0); // only beep
			break;
		case 1:
			rg.check(R.id.rdb1); // Mohamad kheir - only the beginning
			break;
		case 2:
			rg.check(R.id.rdb2); // Mohamad Karanouh - only the beginning
			break;
		case 3:
			rg.check(R.id.rdb3); // Mohamad Kheir All
			break;
		case 4:
			rg.check(R.id.rdb4); // Mohamad Karanouh All
			break;
		case 5:
			rg.check(R.id.rdb5); // Hadi fayed
			break;
		case 6:
			rg.check(R.id.rdb6); // Samir dabliz
			break;
		case 7:
			rg.check(R.id.rdb7); // Abdullah El Jundi
			break;
		}
	}

	private void playOrDownload(final String file_name) {
		final File file = new File(this.getFilesDir(), file_name);
		if (!file.exists()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setTitle(R.string.DoYouWantToDownloadThisAzanFile);
			// builder.setMessage("Are you sure?");
			final Context ctx = this;
			builder.setPositiveButton(R.string.yes,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if (MobileManager.isNetworkAvailable(ctx)) {
								DownloadFileFromURL dfu = new DownloadFileFromURL(
										ctx, file,
										ctx.getString(R.string.PleaseWait));
								dfu.execute("http://libanbit.com/azan/"
										+ file_name);
							} else {
								Toast.makeText(ctx,
										"There is no internet available.",
										Toast.LENGTH_SHORT).show();
							}
							dialog.dismiss();
						}
					});

			builder.setNegativeButton(R.string.no,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							selectSavedAzan(selected_azan);
							dialog.dismiss();
						}
					});
			AlertDialog alert = builder.create();
			alert.show();
		} else {
			mPlayer.startPlaying(this.getFilesDir() + "/" + file_name);
		}
	}

	RadioButton.OnClickListener myOptionOnClickListener = new RadioButton.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			stopPlaying();
			if (rb0.isChecked()) {
				startPlaying(R.raw.beep);
			} else if (rb1.isChecked()) {
				startPlaying(R.raw.notification1);
			} else if (rb2.isChecked()) {
				startPlaying(R.raw.notification2);
			} else if (rb3.isChecked()) {
				playOrDownload(Constants.KHEIR_AZAN_ALL_FILE_1);
			} else if (rb4.isChecked()) {
				playOrDownload(Constants.KARANOUH_AZAN_ALL_FILE_1);
			} else if (rb5.isChecked()) {
				playOrDownload(Constants.FAYED_AZAN_ALL_FILE_1);
			} else if (rb6.isChecked()) {
				playOrDownload(Constants.DABLIZ_AZAN_ALL_FILE_1);
			} else if (rb7.isChecked()) {
				playOrDownload(Constants.JUNDI_AZAN_ALL_FILE_1);
			}
		}
	};

	// if user press back to another view, the voice should be stop
	public void onBackPressed() {
		stopPlaying();
		super.onBackPressed();
	};
}
