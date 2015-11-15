package com.aicpa.main;

import java.sql.Date;

import com.aicpa.R;
import com.aicpa.helper.Prayer;
import com.aicpa.helper.TimeHelper;
import com.aicpa.manager.MobileManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarActivity{} /*extends Activity implements CalendarView.OnCellTouchListener {
	public static final String MIME_TYPE = "vnd.android.cursor.dir/vnd.exina.android.calendar.date";
	CalendarView calendar;
	Handler mHandler = new Handler();
	TextView fajrTV, sunriseTV, duhrTV, asrTV, magribTV, ishaTV;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.calendar);
		
		calendar = ( CalendarView ) findViewById (R.id.calendarView1);
		calendar.setOnCellTouchListener(this);
		
		fajrTV = (TextView) findViewById(R.id.txt_fajr1);
//		sunriseTV = (TextView) findViewById(R.id.sunriseTV);
		duhrTV = (TextView) findViewById(R.id.txt_zuhur1);
		asrTV = (TextView) findViewById(R.id.txt_asr1);
		magribTV = (TextView) findViewById(R.id.txt_maghrib1);
		ishaTV = (TextView) findViewById(R.id.txt_ishaa1);
		
		java.util.Date d = new java.util.Date();
		int month = d.getMonth()+1;
		int day   = d.getDay();
		showTimes(month, day);
		
//		if (Intent.ACTION_PICK.equals(getIntent().getAction()))
//			findViewById(R.id.hint).setVisibility(View.INVISIBLE);
//        if(getIntent().getAction().equals(Intent.ACTION_PICK))
//        	findViewById(R.id.hint).setVisibility(View.INVISIBLE);
	}

	@Override
	public void onTouch(Cell cell) {
		Intent intent = getIntent();
		String action = intent.getAction();
		
		
//		if(action != null && (action.equals(Intent.ACTION_PICK) || action.equals(Intent.ACTION_GET_CONTENT))) {
			int year  = calendar.getYear();
			int month = calendar.getMonth();
			int day   = cell.getDayOfMonth();
			
			// FIX issue 6: make some correction on month and year
			if(cell instanceof CalendarView.GrayCell) {
				// oops, not pick current month...				
				if (day < 15) {
					// pick one beginning day? then a next month day
					if(month==11)
					{
						month = 0;
						year++;
					} else {
						month++;
					}
					
				} else {
					// otherwise, previous month
					if(month==0) {
						month = 11;
						year--;
					} else {
						month--;
					}
				}
//			}
			
			Intent ret = new Intent();
			ret.putExtra("year", year);
			ret.putExtra("month", month);
			ret.putExtra("day", day);
			this.setResult(RESULT_OK, ret);
			finish();
			return;
		}
		int day1 = cell.getDayOfMonth();
		if(calendar.firstDay(day1))
			calendar.previousMonth();
		else if(calendar.lastDay(day1))
			calendar.nextMonth();
		showTimes(month, day1);
	}
	
	private void showTimes(final int month, final int day){
		mHandler.post(new Runnable() {
			public void run() {
				try{
				Prayer prayer = MobileManager.readAndParseJSON(getApplicationContext(), month, day);
				String fajr = prayer.getFajr(), sunrise = prayer.getSunrise(), duhr = prayer.getZuhr(), asr = prayer.getAsr(), maghrib = prayer.getMaghrib(), isha = prayer.getIsha();
				fajrTV.setText(TimeHelper.getTimeWithoutSeconds(fajr));
//				sunriseTV.setText(TimeHelper.getTimeWithoutSeconds(sunrise));
				duhrTV.setText(TimeHelper.getTimeWithoutSeconds(duhr));
				asrTV.setText(TimeHelper.getTimeWithoutSeconds(asr));
				magribTV.setText(TimeHelper.getTimeWithoutSeconds(maghrib));
				ishaTV.setText(TimeHelper.getTimeWithoutSeconds(isha));
//				finish();
				}catch (Exception ex){}
			}
		});
	}

}
*/