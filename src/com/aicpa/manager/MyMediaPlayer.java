package com.aicpa.manager;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;

public class MyMediaPlayer implements OnPreparedListener, OnErrorListener
{
	private MediaPlayer mPlayer = null;
	private Context ctx;
	
	public MyMediaPlayer(Context ctx)
	{
		this.ctx = ctx;
	}
	
	public void stop()
	{
		if (mPlayer != null)
	    {
	    	mPlayer.stop();
	    	mPlayer.release();
	    	mPlayer = null;
	   }
	}

	public void start(int media_file) 
	{
		try
		{
			mPlayer = MediaPlayer.create(ctx, media_file);
			mPlayer.start();
		}
		catch ( Exception ex)
		{
			Log.i("MyPlayer", ex.getMessage());
		}
	}
	
	// this method we may use it for S3, it seems better, but needs testing
	public void startPlaying(String url)
	{
		 try
		 {
			 	mPlayer = new MediaPlayer();
			 	mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		    	mPlayer.setDataSource(url);
		    	mPlayer.setOnErrorListener(this);
		    	mPlayer.setOnPreparedListener(this);
		    	mPlayer.prepareAsync();
		    } catch (IllegalArgumentException e) {
		        e.printStackTrace();
		    } catch (IllegalStateException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }       
		 	catch(Exception e)
		 	{
		 		Log.e("Player", e.getMessage());
		 	}
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		return false;
	}

	// this method is for startPlaying(String url)
	@Override
	public void onPrepared(MediaPlayer arg0) {
		try{
			mPlayer.start();
		}catch (Exception ex){}
	}

}
