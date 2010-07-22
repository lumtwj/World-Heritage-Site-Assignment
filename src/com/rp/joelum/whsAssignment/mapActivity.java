package com.rp.joelum.whsAssignment;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class mapActivity extends MapActivity implements LocationListener {
	MapView mapView;
	GeoPoint geoPoint;
	MapController myMapController;
	String getName;
	double getLat;
	double getLng;
    static final private int SWITCH_VIEW = Menu.FIRST;
    static final private int LOCATE_ME = Menu.FIRST + 1;
    static final private int FEEDBACK_SITE = Menu.FIRST + 2;
    boolean enabled;
    Location location;
	Location whsCal;
	TextView disp;
	LocationManager locationManager;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        
        disp = (TextView)findViewById(R.id.dispInfo);
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        myMapController = mapView.getController();
        myMapController.setZoom(10);
        
        enabled = mapView.isSatellite();
        
        getDataFromPrevAct();
        
		disp.setText("Name of WHSite: " + getName +
				"\nDistance to site: " + "N.A." +
				"\nBearing to site: " + "N.A."
			);
        
        displayPin();
    }
    
    public void bearingAndLocation() {
		getCurrentLocation();
		
		//Put the latitude and longitude from GeoPoint in to a location to calculate the distance and bearing difference
		whsCal = new Location("");
			
		whsCal.setLatitude(getLat);
		whsCal.setLongitude(getLng);
		
		disp.setText("Name of WHSite: " + getName +
			"\nDistance to site: " + whsCal.distanceTo(location)/1000 + " km" +
			"\nBearing to site: " + whsCal.bearingTo(location) + " °"
		);
    }
    
    public void getDataFromPrevAct() {
    	//Retrieve stored data from previous activity
    	getName = getIntent().getStringExtra("whsName");
    	getLat = getIntent().getDoubleExtra("latitude", 1.399498);
    	getLng = getIntent().getDoubleExtra("longitude", 103.745382);
    	
       	geoPoint = new GeoPoint((int)(getLat * 1E6),(int)(getLng * 1E6));
    }
    
    public void displayPin() {
    	//Retrieve coordinates of the site. Move maps to the coordinates.
        myMapController.animateTo(geoPoint);
        
	    //Create a list to store the pin location. Draw the pin and display message.
        List<Overlay> mapOverlays = mapView.getOverlays();
        mapOverlays.clear();
        Drawable drawable = this.getResources().getDrawable(R.drawable.pin);
        HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(drawable, this);
        
        OverlayItem overlayitem = new OverlayItem(geoPoint, getName, "Latitude: " + getLat + "\nLongitude: " + getLng);
        
        itemizedoverlay.addOverlay(overlayitem);
        
        mapOverlays.add(itemizedoverlay);
    }
    
    public void toNewActivity() {
    	Intent intentNewAct = new Intent(getBaseContext(), feedbackActivity.class);
    	
    	intentNewAct.putExtra("whsName", getName);
    	intentNewAct.putExtra("latitude", getLat);
    	intentNewAct.putExtra("longitude", getLng);
    	
    	startActivity(intentNewAct);
    }
    
    public void getCurrentLocation() {
    	//Use LocationManager to get current location from different service (network or gps)
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		//Define on how accurate the current location will be
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		
		//Find out which are the best location provider and get the location
		String provider = locationManager.getBestProvider(criteria, true);
		location = locationManager.getLastKnownLocation(provider);
		
		//Define the time interval for updates of the location
		locationManager.requestLocationUpdates(provider, 0, 0, mapActivity.this);
    }
    
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	// Create and add new menu items.
    	MenuItem switchView = menu.add(0, SWITCH_VIEW, Menu.NONE,
    	R.string.switchView);
    	MenuItem locateMe = menu.add(0, LOCATE_ME, Menu.NONE,
    	    	R.string.locate);
    	MenuItem feedbackSite = menu.add(0, FEEDBACK_SITE, Menu.NONE,
    	    	R.string.feedbackTxt);
    	switchView.setIcon(R.drawable.map);
    	locateMe.setIcon(R.drawable.locate);
    	feedbackSite.setIcon(R.drawable.feedback);
    	
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);
	    switch (item.getItemId()) {
		    case (SWITCH_VIEW): {
		    	if (enabled == false) {
		    		mapView.setSatellite(true);
		    		enabled = true;
		    	}
		    	else {
		    		mapView.setSatellite(false);
		    		enabled = false;
		    	}
		    	
			    return true;
		    }
			case (LOCATE_ME): {
				getCurrentLocation();
				onLocationChanged(location);
				
		        return true;
			}
			case (FEEDBACK_SITE): {
				toNewActivity();
				
		        return true;
			}
	    }
    	return false;
    }

    
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		if (location != null) {
			//Obtain the current location latitude and longitude
			double lat = location.getLatitude() * 1E6;
			double lng = location.getLongitude() * 1E6;
			geoPoint = new GeoPoint((int) lat, (int) lng);
			
			//Display the pin, bearing and distance details
			displayPin();
			bearingAndLocation();
		}
		else {
			//Message to show when GPS is unavailable
			Toast.makeText(this, "GPS may not be available", Toast.LENGTH_LONG).show();
			locationManager.removeUpdates(this);
		}

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}
