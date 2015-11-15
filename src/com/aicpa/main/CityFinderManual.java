package com.aicpa.main;

import java.util.ArrayList;
import java.util.List;

import com.aicpa.R;
import com.aicpa.helper.Typefaces;
import com.aicpa.manager.MobileManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class CityFinderManual extends Activity {

	private Spinner countrySpinner;
	private Spinner citySpinner;
	private Button saveButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			this.setContentView(R.layout.cityfindermanual);

			TextView textView = (TextView) findViewById(R.id.textView1);
			textView.setTypeface(Typefaces.get(this.getBaseContext(),
					"fonts/DroidNaskh-Regular.ttf"));

			countrySpinner = (Spinner) findViewById(R.id.countrySpinner);
			this.fillCountrySpinner();
			
			countrySpinner.setSelection(MobileManager.getCountryIndex(getApplicationContext()));

			this.saveButton = (Button) findViewById(R.id.saveButton);
			this.saveButton.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					try {
//						long id = countrySpinner.getSelectedItemId();
						String country = countrySpinner.getSelectedItem().toString();
						MobileManager.saveCountry(getApplicationContext(), country);
						
//						Intent intent = new Intent(CityFinderManual.this,
//								MainActivity.class);
//						startActivity(intent);
						setResult(Activity.RESULT_OK);
						finish();
					} catch (Exception e) {
					}
				}

			});
		} catch (Exception e) {
		}
	}

	private void fillCountrySpinner() 
	{
		List<String> list = new ArrayList<String>();
		for(int i =0; i<MobileManager.countries.length; i++)
			list.add(MobileManager.countries[i]);
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		countrySpinner.setAdapter(dataAdapter);
	}

}
