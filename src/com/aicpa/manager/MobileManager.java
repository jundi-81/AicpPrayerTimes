package com.aicpa.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.aicpa.R;
import com.aicpa.helper.Prayer;
import com.aicpa.helper.TimeHelper;
import com.aicpa.main.AlarmReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

public class MobileManager {
	public final static String[] countries = { "Lebanon" };

	public static String ReadFromfile(String fileName, Context context) {
		StringBuilder returnString = new StringBuilder();
		InputStream fIn = null;
		InputStreamReader isr = null;
		BufferedReader input = null;
		try {
			fIn = context.getResources().getAssets()
					.open(fileName, Context.MODE_WORLD_READABLE);
			isr = new InputStreamReader(fIn);
			input = new BufferedReader(isr);
			String line = "";
			while ((line = input.readLine()) != null) {
				returnString.append(line);
			}
		} catch (Exception e) {
			e.getMessage();
		} finally {
			try {
				if (isr != null)
					isr.close();
				if (fIn != null)
					fIn.close();
				if (input != null)
					input.close();
			} catch (Exception e2) {
				e2.getMessage();
			}
		}
		return returnString.toString();
	}

	public static Prayer readAndParseJSON(Context c, int month, int day) {
		// month = 9;//TODO TO REMOVE IT
		String in = ReadFromfile(getCountryName(c) + ".txt", c);
		try {
			JSONArray array = new JSONArray(in);
			JSONObject oneObject = array.getJSONObject(month - 1);// not month
																	// but array
																	// starting
																	// from 0
			JSONObject days = oneObject.getJSONObject("days");
			JSONObject times = new JSONObject(days.get("" + day).toString());
			String fajr = times.getString("fajr");
			String sunrise = times.getString("sunrise");
			String zuhr = times.getString("zuhr");
			String asr = times.getString("asr");
			String maghrib = times.getString("maghrib");
			String isha = times.getString("isha");
			return new Prayer(fajr, sunrise, zuhr, asr, maghrib, isha);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String[] getMorningAwrad(Context c) {
		// month = 9;//TODO TO REMOVE IT
		String in = ReadFromfile("Awrad.txt", c);
		try {
			JSONArray array = new JSONArray(in);
			String[] awrads = new String[array.length()];
			for (int i = 0; i < array.length(); i++) {
				JSONObject awrad = (JSONObject) array.get(i);
				String name = (String) awrad.get("subject");
				String description = (String) awrad.get("description");
				String audio_file_name = (String) awrad.get("audio_file_name");
				awrads[i] = name;
			}
			return awrads;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// get nearest prayer time based on current time

	public static int computeNearestPrayerTime(Context context, int hour,
			int min, int sec, int year, int month, int day, Prayer p)
			throws IOException {
		ArrayList<String> prayerTimes = new ArrayList<String>();
		prayerTimes.add(p.getFajr());
		prayerTimes.add(p.getZuhr());
		prayerTimes.add(p.getAsr());
		prayerTimes.add(p.getMaghrib());
		prayerTimes.add(p.getIsha());
		int[] prayerTimeInSeconds = new int[5];
		prayerTimes.get(0);
		// Convert prayer times to seconds
		prayerTimeInSeconds[0] = TimeHelper.getSec(
				TimeHelper.to24(prayerTimes.get(0)),
				TimeHelper.getMinute(prayerTimes.get(0)),
				TimeHelper.getSecond(prayerTimes.get(0)));
		prayerTimeInSeconds[1] = TimeHelper.getSec(
				TimeHelper.to24(prayerTimes.get(1)),
				TimeHelper.getMinute(prayerTimes.get(1)),
				TimeHelper.getSecond(prayerTimes.get(1)));
		prayerTimeInSeconds[2] = TimeHelper.getSec(
				TimeHelper.to24(prayerTimes.get(2)),
				TimeHelper.getMinute(prayerTimes.get(2)),
				TimeHelper.getSecond(prayerTimes.get(2)));
		prayerTimeInSeconds[3] = TimeHelper.getSec(
				TimeHelper.to24(prayerTimes.get(3)),
				TimeHelper.getMinute(prayerTimes.get(3)),
				TimeHelper.getSecond(prayerTimes.get(3)));
		prayerTimeInSeconds[4] = TimeHelper.getSec(
				TimeHelper.to24(prayerTimes.get(4)),
				TimeHelper.getMinute(prayerTimes.get(4)),
				TimeHelper.getSecond(prayerTimes.get(4)));
		// sort ascending
		Arrays.sort(prayerTimeInSeconds);
		// default value is the first prayer in the day
		int nearestPrayer = prayerTimeInSeconds[0];
		// convert current time to seconds
		int currentTime = hour * 3600 + min * 60 + sec;
		int counter = 0;
		for (Integer prayertime : prayerTimeInSeconds) {
			int pt = prayertime;
			if (pt >= currentTime) { // return first prayer after this time
										// (nearest prayer)
				p.setNextPrayerDelay(pt - currentTime);
				saveMyCurrentPrayer(counter);
				return pt;
			}
			counter++;
		}
		return nearestPrayer;
	}

	private static void saveMyCurrentPrayer(int prayer) {
		try {
			MyProperties.getInstance().someValueIWantToKeep = prayer;
		} catch (Exception ex) {
		}
	}

	public static int getCurrentPrayer() {
		try {
			return MyProperties.getInstance().someValueIWantToKeep;
		} catch (Exception ex) {
			return -1;
		}
	}

	/*
	 * public static void readAndParseJSON(Context c) {
	 * 
	 * String in = ReadFromfile("file.txt", c);
	 * 
	 * try { JSONArray array = new JSONArray(in); for (int i=0; i <
	 * array.length(); i++) { try { JSONObject oneObject =
	 * array.getJSONObject(i); int month = oneObject.getInt("month"); String
	 * name = oneObject.getString("name"); JSONObject days =
	 * oneObject.getJSONObject("days"); for (int j=1; j < days.length()-1; j++)
	 * { JSONObject times = new JSONObject( days.get("" + j).toString() );
	 * String fajr = times.getString("fajr"); String sunrise =
	 * times.getString("sunrise"); String zuhr = times.getString("zuhr"); String
	 * asr = times.getString("asr"); String maghrib =
	 * times.getString("maghrib"); String isha = times.getString("isha"); } }
	 * catch (JSONException e) { // Oops } }
	 * 
	 * } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * }
	 */

	public static void createTask(Context context) {

		Date date = new Date();
		int dd = date.getDate();
		int mm = date.getMonth() + 1;
		int yy = date.getYear() + 1900;
		int h = date.getHours();
		int m = date.getMinutes();
		int s = date.getSeconds();

		Prayer prayer = getPrayerTimesData(context, dd, mm);
		AlarmManager alarmmanager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		try {
			MobileManager.computeNearestPrayerTime(context, h, m, s, yy, mm,
					dd, prayer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		long nextmin = prayer.getNextPrayerDelay() * 1000;
		if (prayer.getNextPrayerDelay() == -1) {
			prayer = getNextDate(context, prayer);
			nextmin = prayer.getNextPrayerDelay() * 1000;
		}
		Log.i("createTask", "nextmin=" + nextmin);
		Intent intent0 = new Intent(context, AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				intent0, 0);
		try {
			alarmmanager.cancel(pendingIntent);
		} catch (Exception ex) {
		}
		alarmmanager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
				+ nextmin, pendingIntent);
	}

	private static Prayer getNextDate(Context context, Prayer prayer) {
		Date today = new Date();
		Date tomorrow = new Date(today.getTime() + (1000 * 60 * 60 * 24));

		int dd = tomorrow.getDate();
		int mm = tomorrow.getMonth() + 1;
		int yy = tomorrow.getYear() + 1900;
		int h = tomorrow.getHours() - 24;//
		int m = tomorrow.getMinutes();
		int s = tomorrow.getSeconds();

		try {
			MobileManager.computeNearestPrayerTime(context, h, m, s, yy, mm,
					dd, prayer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prayer;
	}

	private static Prayer getPrayerTimesData(Context context, int dd, int mm) {
		return MobileManager.readAndParseJSON(context, mm, dd);
	}

	public static void saveCountry(Context context, String country) {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sharedPrefs.edit();
		editor.putString("country", country);
		editor.commit();
	}

	public static String getCountryName(Context context) {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPrefs.getString("country", "");
	}

	public static int getCountryIndex(Context context) {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		String country = sharedPrefs.getString("country", "");
		for (int i = 0; i < countries.length; i++) {
			if (countries[i].equals(country))
				return i;
		}
		return -1;
	}

	public static void saveAzanType(Context context, int type) {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sharedPrefs.edit();
		editor.putString("azan", type + "");
		editor.commit();
	}

	public static int getAzanType(Context context) {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		String azan = sharedPrefs.getString("azan", "0");
		return Integer.parseInt(azan);
	}

	public static boolean isActivated(Context context) {
		if (getCountryIndex(context) == -1)
			return false;
		return true;
	}

	public static boolean isArabicLanguageExists() {
		Locale[] locals = Locale.getAvailableLocales();
		for (int i = 0; i < locals.length; i++)
			if (locals[i].getLanguage().equalsIgnoreCase("ar"))
				return true;
		return false;
	}

	public static boolean isLebanonCountryExists() {
		Locale[] locals = Locale.getAvailableLocales();
		for (int i = 0; i < locals.length; i++)
			if (locals[i].getCountry().equalsIgnoreCase("LB"))
				return true;
		return false;
	}

	public static String getDisplayDay() {
		if (isArabicLanguageExists()) {
			DateFormat df = new SimpleDateFormat("EEEE   d", new Locale("ar"));
			return df.format(Calendar.getInstance().getTime());
		} else {
			DateFormat df = new SimpleDateFormat("EEEE   d");
			return df.format(Calendar.getInstance().getTime());
		}

	}

	public static String getDisplayMonth(Context context) {
		if (isArabicLanguageExists()) {
			DateFormat df = new SimpleDateFormat("MMM");
			String month = df.format(Calendar.getInstance().getTime());

			String months[] = context.getResources().getStringArray(
					R.array.months);
			String monthsValues[] = context.getResources().getStringArray(
					R.array.monthsValues);
			for (int i = 0; i < months.length; i++) {
				if (months[i].equalsIgnoreCase(month)) {
					month = monthsValues[i];
					break;
				}
			}
			return month;
		}
		DateFormat df = new SimpleDateFormat("MMM");
		return df.format(Calendar.getInstance().getTime());
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}
}
