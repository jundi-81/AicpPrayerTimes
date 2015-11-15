package com.aicpa.main;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.aicpa.helper.Prayer;
import com.aicpa.helper.TimeHelper;
import com.aicpa.helper.Typefaces;
import com.aicpa.manager.MobileManager;
import com.aicpa.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Prayer prayer;
	private String country;
	private TextView remainingTime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);

		country = MobileManager.getCountryName(getApplicationContext());
		if (country.isEmpty())
			showCityFinder();
		else
			init();
//		NotificationManager notifManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//		notifManager.cancelAll();
	}

	private void showCityFinder() {
		Intent myIntent = new Intent(getApplicationContext(),
				CityFinderManual.class);
		startActivityForResult(myIntent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == 0)
				init();
		} else {
			finish();
			System.exit(0);
		}
	}

	public void init() {
		Date date = new Date();
		int dd = date.getDate();
		int mm = date.getMonth() + 1;
		final int yy = date.getYear() + 1900;

		prayer = getPrayerTimesData(dd, mm);

		TextView dayView = (TextView) findViewById(R.id.day);
		dayView.setTypeface(Typefaces.get(this.getBaseContext(),
				"fonts/DroidNaskh-Regular.ttf"));
		try {
			dayView.setText(MobileManager.getDisplayDay() + " "
					+ MobileManager.getDisplayMonth(getApplicationContext()));
		} catch (Exception ex) {
			DateFormat df = new SimpleDateFormat("EEEE d MMM");
			dayView.setText(df.format(Calendar.getInstance().getTime()));
		}

		TextView fajrTV = (TextView) findViewById(R.id.fajrTV);
		TextView sunriseTV = (TextView) findViewById(R.id.sunriseTV);
		TextView duhrTV = (TextView) findViewById(R.id.duhrTV);
		TextView asrTV = (TextView) findViewById(R.id.asrTV);
		TextView magribTV = (TextView) findViewById(R.id.magribTV);
		TextView ishaTV = (TextView) findViewById(R.id.ishaTV);

		fajrTV.setTypeface(Typefaces.get(this.getBaseContext(),
				"fonts/KacstTitle.ttf"));
		sunriseTV.setTypeface(Typefaces.get(this.getBaseContext(),
				"fonts/KacstTitle.ttf"));
		duhrTV.setTypeface(Typefaces.get(this.getBaseContext(),
				"fonts/KacstTitle.ttf"));
		asrTV.setTypeface(Typefaces.get(this.getBaseContext(),
				"fonts/KacstTitle.ttf"));
		magribTV.setTypeface(Typefaces.get(this.getBaseContext(),
				"fonts/KacstTitle.ttf"));
		ishaTV.setTypeface(Typefaces.get(this.getBaseContext(),
				"fonts/KacstTitle.ttf"));

		try {

			TextView fajrTime = (TextView) findViewById(R.id.fajrTime);
			TextView sunriseTime = (TextView) findViewById(R.id.sunriseTime);
			TextView duhrTime = (TextView) findViewById(R.id.duhrTime);
			TextView asrTime = (TextView) findViewById(R.id.asrTime);
			TextView magribTime = (TextView) findViewById(R.id.magribTime);
			TextView ishaTime = (TextView) findViewById(R.id.ishaTime);
			// remainingTime = (TextView) findViewById(R.id.remaining_time);

			String fajr = prayer.getFajr(), sunrise = prayer.getSunrise(), duhr = prayer
					.getZuhr(), asr = prayer.getAsr(), maghrib = prayer
					.getMaghrib(), isha = prayer.getIsha();
			fajrTime.setTypeface(Typefaces.get(this.getBaseContext(),
					"fonts/Roboto-Regular.ttf"));
			fajrTime.setText(TimeHelper.getTimeWithoutSeconds(fajr));
			sunriseTime.setTypeface(Typefaces.get(this.getBaseContext(),
					"fonts/Roboto-Regular.ttf"));
			sunriseTime.setText(TimeHelper.getTimeWithoutSeconds(sunrise));
			duhrTime.setTypeface(Typefaces.get(this.getBaseContext(),
					"fonts/Roboto-Regular.ttf"));
			duhrTime.setText(TimeHelper.getTimeWithoutSeconds(duhr));
			asrTime.setTypeface(Typefaces.get(this.getBaseContext(),
					"fonts/Roboto-Regular.ttf"));
			asrTime.setText(TimeHelper.getTimeWithoutSeconds(asr));
			magribTime.setTypeface(Typefaces.get(this.getBaseContext(),
					"fonts/Roboto-Regular.ttf"));
			magribTime.setText(TimeHelper.getTimeWithoutSeconds(maghrib));
			ishaTime.setTypeface(Typefaces.get(this.getBaseContext(),
					"fonts/Roboto-Regular.ttf"));
			ishaTime.setText(TimeHelper.getTimeWithoutSeconds(isha));

			// remainingTime.setTypeface(Typefaces.get(this.getBaseContext(),
			// "fonts/Roboto-Regular.ttf"));
			// remainingTime.setText("");
			// updateRemainingTime(yy, mm, dd, getPrayerTimesData(dd, mm));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// Log.i("onCreate","calling create task");
		createTask(prayer);
	}

	private void createTask(Prayer p) {
		MobileManager.createTask(getApplicationContext());
	}

	private Prayer getPrayerTimesData(int dd, int mm) {
		return MobileManager.readAndParseJSON(getApplicationContext(), mm, dd);
	}

	public void updateRemainingTime(int yy, int mm, int dd, Prayer p)
			throws IOException {
		Date date = new Date();

		int h = date.getHours();
		int m = date.getMinutes();
		int s = 0;	// date.getSeconds();

		int time = MobileManager.computeNearestPrayerTime(
				getApplicationContext(), h, m, s, yy, mm, dd, p);
		int def = TimeHelper.different((h * 3600 + m * 60 + s), time);
		remainingTime.setText(TimeHelper.secondsToTime(def));
		remainingTime.setTypeface(Typefaces.get(this.getBaseContext(),
				"fonts/Roboto-Regular.ttf"));
	}

	private void azan() {
		Intent intent = new Intent(getApplicationContext(), AlertActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_NO_HISTORY);
		intent.putExtra("runFromService", true);
		getApplicationContext().startActivity(intent);
	}

	// add main menu items
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 3, 4, getString(R.string.about));
		menu.add(0, 2, 3, getString(R.string.settings));
		menu.add(0, 4, 2, getString(R.string.calendartxt1));
		menu.add(0, 5, 1, getString(R.string.tahsin));
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		MainActivity.this.openOptionsMenu();
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case 2:
			// Settings
			Intent myIntent = new Intent(this, SettingsActivity.class);
			startActivity(myIntent);
			return true;
		case 3:
			// About
			Intent MyIntent = new Intent(this, About.class);
			startActivity(MyIntent);
			return true;
		case 4:
			Toast.makeText(MainActivity.this,
					MainActivity.this.getString(R.string.soon),
					Toast.LENGTH_SHORT).show();
			break;
		case 5:
			Intent TahsinIntent = new Intent(this, AwradList.class);
			startActivity(TahsinIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// this method is triggered at the first time
	// put here what you want to execute at the first run for the app on this
	// device
	public void onFirstStart() {
		Intent cityFinderActivity = new Intent(this, CityFinder.class);
		startActivity(cityFinderActivity);
	}
}