package com.rp.joelum.whsAssignment;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class whsAssignment extends ListActivity {
	ListView listView;
	ArrayList<site> sites = new ArrayList<site>();
	ArrayAdapter<site> arrayAdapter;
	String name;
	double lat;
	double lng;
    static final private int DELETE_SITE = Menu.FIRST;
    static final private int VISIT_URL = Menu.FIRST + 1;
    static final private int RESTORE_LIST = Menu.FIRST;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        fetchArrayList();
        
        //Preparing ListView. Transfer list from ArrayList to ListView. Allow context menu function.
        listView = getListView();
        registerForContextMenu(listView);
        
	    arrayAdapter = new ArrayAdapter<site>
	    (this,android.R.layout.simple_list_item_1,sites);
	    
	    arrayAdapter.notifyDataSetChanged();

        listView.setAdapter(arrayAdapter);
        
        //Allow ListView to be click.
        listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				//Get site details from ArrayList
				name = sites.get(position).getName();
				lat = sites.get(position).getLatitude();
				lng = sites.get(position).getLongitude();
				String country = sites.get(position).getCountry();
				
	            //Prepare the alert box
	            AlertDialog.Builder alertbox = new AlertDialog.Builder(whsAssignment.this);
	            alertbox.setTitle(name);
	            alertbox.setIcon(R.drawable.logo);
	 
	            //Set the message to display
	            alertbox.setMessage(
	            	"WHS name: " + name +
	            	"\nCountry: " + country +
	            	"\nLatitude: " + lat +
	            	"\nLongitude: " + lng
	            );
	 
	            //Set a positive/yes button and create a listener
	            alertbox.setPositiveButton("View Map", new DialogInterface.OnClickListener() {
	 
	                //Do something when the button is clicked
	                public void onClick(DialogInterface arg0, int arg1) {
	    				toNextActivity();
	                }
	            });
	 
	            //Set a negative/no button and create a listener
	            alertbox.setNegativeButton("Cancel", null);
	 
	            //Display the dialog box
	            alertbox.show();
			}
        });
    }
    
    public void toNextActivity() {
    	//Create intent to go next activity (mapActivity)
    	Intent intentNewAct = new Intent(getBaseContext(), mapActivity.class);
    	
    	//Store data to be transfered to next activity.
    	intentNewAct.putExtra("whsName", name);
    	intentNewAct.putExtra("latitude", lat);
    	intentNewAct.putExtra("longitude", lng);
    	
    	//Go to the next activity
    	startActivity(intentNewAct);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu,View v,
    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("WHS Option");
        menu.add(0, DELETE_SITE, Menu.NONE, R.string.deleteSite);
        menu.add(1, VISIT_URL, Menu.NONE, R.string.loadURL);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
    super.onContextItemSelected(item);
		switch (item.getItemId()) {
			case (DELETE_SITE): {
				AdapterView.AdapterContextMenuInfo menuInfo;
				menuInfo =(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
				int index = menuInfo.position;
	            
				sites.remove(index);
				listView.setAdapter(arrayAdapter);
				
				return true;
			}
			case (VISIT_URL): {
				AdapterView.AdapterContextMenuInfo menuInfo;
				menuInfo =(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
				int index = menuInfo.position;
	            
				String link = sites.get(index).getUrl();
				
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(link));
				startActivity(intent);
				
				Toast.makeText(whsAssignment.this, link, Toast.LENGTH_LONG).show();
				
				return true;
			}
		}
		return false;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	// Create and add new menu items.
    	MenuItem restore = menu.add(0, RESTORE_LIST, Menu.NONE,
    	R.string.restoreList);
    	restore.setIcon(R.drawable.list);
    	
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);
	    switch (item.getItemId()) {
		    case (RESTORE_LIST): {
				fetchArrayList();
				listView.setAdapter(arrayAdapter);
		    	
			    return true;
		    }
	    }
    	return false;
    }
    
    public void fetchArrayList() {
        //Add sites to ArrayList.
    	sites.clear();
        sites.add(new site("Minaret of Jam", "Afghanistan", "http://en.wikipedia.org/wiki/Minaret_of_Jam", 34.396667, 64.516111));
        sites.add(new site("Buddhas of Bamyan", "Afghanistan", "http://en.wikipedia.org/wiki/Buddhas_of_Bamiyan", 34.831944, 67.826667));
        sites.add(new site("Haghpat Monastery", "Armenia", "http://en.wikipedia.org/wiki/Haghpat_Monastery", 41.093889, 44.711944));
        sites.add(new site("Sanahin", "Armenia", "http://en.wikipedia.org/wiki/Sanahin", 41.087924, 44.667985));
        sites.add(new site("Etchmiadzin Cathedral", "Armenia", "http://en.wikipedia.org/wiki/Etchmiadzin_Cathedral", 40.161769, 44.291164));
        sites.add(new site("Saint Hripsime Church", "Armenia", "http://en.wikipedia.org/wiki/St._Hripsime_Church", 40.166992, 44.309675));
        sites.add(new site("Saint Gayane Church", "Armenia", "http://en.wikipedia.org/wiki/St._Gayane", 40.157419, 44.291986));
        sites.add(new site("Shoghakat", "Armenia", "http://en.wikipedia.org/wiki/Shoghakat", 40.168044, 44.304936));
        sites.add(new site("Great Barrier Reef", "Australia", "http://en.wikipedia.org/wiki/List_of_World_Heritage_Sites_in_Asia_and_Australasia", -18.286111, 147.7));
        sites.add(new site("Yakushima", "Japan", "http://en.wikipedia.org/wiki/Yakushima", 30.358611, 130.528611));
    }
    
    public void tester() {
    	Toast.makeText(whsAssignment.this, "This is the Toast message", Toast.LENGTH_LONG).show();
    }
}