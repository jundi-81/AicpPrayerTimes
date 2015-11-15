package com.aicpa.helper;

public class Prayer {
	
	private String fajr, sunrise, zuhr, asr, maghrib, isha;
	private int next_prayer_index =-1;
	private long next_prayer_delay =-1;
	
	public Prayer(String fajr, String sunrise,String  zuhr,String  asr, String maghrib, String isha){
		this.asr = asr;
		this.fajr = fajr;
		this.zuhr = zuhr;
		this.maghrib = maghrib;
		this.isha = isha;
		this.sunrise = sunrise;
	}
	
	public String getAsr() {
		return asr;
	}
	public String getFajr() {
		return fajr;
	}
	public String getIsha() {
		return isha;
	}
	public String getMaghrib() {
		return maghrib;
	}
	public String getSunrise() {
		return sunrise;
	}
	public String getZuhr() {
		return zuhr;
	}
	public void setNextPrayerIndex(int next_prayer_index) {
		this.next_prayer_index = next_prayer_index;
	}
	
	public int getNextPrayerIndex() {
		return next_prayer_index;
	}
	
	public String getNextPrayerTime(){
		switch ( next_prayer_index )
		{
		case 0:
			return fajr;
		case 1:
			return zuhr;
		case 2:
			return asr;
		case 3:
			return maghrib;
		case 4:
			return isha;
			default:
				return "";			
		}
	}
	
	public int getNextPrayerHour(){
		String npt = getNextPrayerTime();
		if ( npt == null || npt.length() ==0 )
			return -1;
		
		return Integer.parseInt(npt.split(":")[0]);
	}
	
	public int getNextPrayerMinute(){
		String npt = getNextPrayerTime();
		if ( npt == null || npt.length() ==0 )
			return -1;
		
		return Integer.parseInt(npt.split(":")[1]);
	}
	
	public long getNextPrayerDelay() {
		return next_prayer_delay;
	}
	
	public void setNextPrayerDelay(long next_prayer_delay) {
		this.next_prayer_delay = next_prayer_delay;
	}

}
