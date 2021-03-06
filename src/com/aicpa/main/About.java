package com.aicpa.main;

import com.aicpa.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class About extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.about);// TODO create a new layout
		Button feedback = (Button) findViewById(R.id.feadbackButton);
		TextView tvVersion = (TextView) findViewById(R.id.tvVersion);
		String version = "1.2";
		try {
			version = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tvVersion.setText(version);
		feedback.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822");
				i.putExtra(Intent.EXTRA_EMAIL, new String[] { About.this
						.getString(R.string.feedbackEmail) });
				i.putExtra(Intent.EXTRA_SUBJECT,
						About.this.getString(R.string.feedbackSubject));
				i.putExtra(Intent.EXTRA_TEXT,
						About.this.getString(R.string.feedbackBody));
				try {
					startActivity(Intent.createChooser(i, About.this
							.getString(R.string.feedbackChooserString)));
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(About.this,
							About.this.getString(R.string.feedbackFailed),
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		Button contactus = (Button) findViewById(R.id.contactUsButton);
		contactus.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822");
				i.putExtra(Intent.EXTRA_EMAIL, new String[] { About.this
						.getString(R.string.contactusEmail) });
				i.putExtra(Intent.EXTRA_SUBJECT,
						About.this.getString(R.string.contactusSubject));
				i.putExtra(Intent.EXTRA_TEXT,
						About.this.getString(R.string.contactusBody));
				try {
					startActivity(Intent.createChooser(i, About.this
							.getString(R.string.contactusChooserString)));
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(About.this,
							About.this.getString(R.string.contactusFailed),
							Toast.LENGTH_SHORT).show();
				}
			}
		});

	}
}
