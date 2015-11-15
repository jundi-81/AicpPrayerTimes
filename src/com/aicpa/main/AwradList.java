package com.aicpa.main;

import java.util.ArrayList;
import java.util.Arrays;

import com.aicpa.R;
import com.aicpa.manager.MobileManager;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AwradList extends Activity {
  
  private ListView mainListView ;
  private ArrayAdapter<String> listAdapter ;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.list_day);
    
    // Find the ListView resource. 
    mainListView = (ListView) findViewById( R.id.list_day );

    // Create and populate a List of planet names.
    String[] planets = MobileManager.getMorningAwrad(this.getApplicationContext());
    ArrayList<String> planetList = new ArrayList<String>();
    planetList.addAll( Arrays.asList(planets) );
    
    // Create ArrayAdapter using the planet list.
    listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, planetList);
    
    // Add more planets. If you passed a String[] instead of a List<String> 
    // into the ArrayAdapter constructor, you must not add more items. 
    // Otherwise an exception will occur.
//    listAdapter.add( "Ceres" );
    
    // Set the ArrayAdapter as the ListView's adapter.
    mainListView.setAdapter( listAdapter );      
  }
}