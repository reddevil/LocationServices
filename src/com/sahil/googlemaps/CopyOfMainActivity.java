package com.sahil.googlemaps;

import android.R;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.sahil.others.MyLoc;
import com.sahil.others.MyLoc.LocationResult;
import com.sahil.others.MyLocation;

public class CopyOfMainActivity extends MapActivity implements LocationListener {

	private MapView mapView;
    private LocationManager locManager;
	private MyLoc my_location;
	
	private String locations;
	MyLocation myLocation = new MyLocation();
	
	double lon = 0;
    double lat = 0;
	
	private void setLatLon(Location location) {
		// TODO Auto-generated method stub
		lon = location.getLongitude();
		lat = location.getLatitude();
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.);
        
        double lon = 0;
        double lat = 0;
        
        
        
        /*my_location = new MyLoc();
        my_location.init(this, locationResult);   
        */
        
   
        //fetch the map view from the layout
        //MapView mapView = (MapView) findViewById(R.id.mapview);

        LocationResult locationResult = new LocationResult(){
            public double lon1,lat1;
        	public void gotLocation(Location location){
                //Got the location!
            	setLatLon (location);
        		lon1 = location.getLongitude();
            	lat1 = location.getLatitude();
            	String strloc  = location.getLatitude() + ","
                        + location.getLongitude();
            	locations = new String (strloc);
            	System.out.println ("Inside inner class: " + strloc);
            }
        	
			public double getLat () {
        		return lat1;
        	}
        	public double getLon () {
        		return lon1;
        	}        	
        	
        };
        
        
        
        
        myLocation.getLocation(this, locationResult);
        
        System.out.println ("Outside:" + locations);
        
        //System.out.println ("Outside: " + myLocation.lat + "," + myLocation.lon);
       
        
        
        
        //System.out.println (lon);
        //System.out.println (lat);
        
        //make available zoom controls
        mapView.setBuiltInZoomControls(true);

        //latitude and longitude of Rome
        
        /*double lat = 41.889882;
        double lon = 12.479267;

        GeoPoint point = new GeoPoint(0,0);
        
        //create geo point
        point = new GeoPoint((int)(lat * 1E6), (int)(lon *1E6));

        //get the MapController object
        MapController controller = mapView.getController();

        //animate to the desired point
        controller.animateTo(point);

        //set the map zoom to 13
        // zoom 1 is top world view
        controller.setZoom(13); */

        //invalidate the map in order to show changes
        mapView.invalidate();
        
        
        
      
        
     // Use the location manager through GPS
        /*locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        
        if (!gpsEnabled) {
            // Build an alert dialog here that requests that the user enable
            // the location services, then when the user clicks the "OK" button,
            enableLocationSettings();
        }        
        
        LocationProvider provider =
                locManager.getProvider(LocationManager.GPS_PROVIDER);
        
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        //locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        //get the current location (last known location) from the location manager
        Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //Location location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        

            //if location found display as a toast the current latitude and longitude
        if (location != null) {

            Toast.makeText(
                    this,
                    "Current location:\nLatitude: " + location.getLatitude()
                            + "\n" + "Longitude: " + location.getLongitude(),
                    Toast.LENGTH_LONG).show();
        } else {

            Toast.makeText(this, "Cannot fetch current location!",
                    Toast.LENGTH_LONG).show();
        }

//when the current location is found – stop listening for updates (preserves battery)
        locManager.removeUpdates(this); */
    }

    private void setup () {
    	LocationResult locationResult = new LocationResult(){
            public double lon1,lat1;
        	public void gotLocation(Location location){
                //Got the location!
            	lon1 = location.getLongitude();
            	lat1 = location.getLatitude();
            	String strloc  = location.getLatitude() + ","
                        + location.getLongitude();
            	locations = new String (strloc);
            	//System.out.println ("Inside inner class: " + strloc);
            }
        	public double getLat () {
        		return lat1;
        	}
        	public double getLon () {
        		return lon1;
        	}        	
        	
        };
        
        
        
        
        myLocation.getLocation(this, locationResult);
    }
    
    private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }
    
    /* When the activity starts up, request updates */
    @Override
    protected void onResume() {
        super.onResume(); 
        setup ();
        /*if (locManager != null) {
        	locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }*/
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (locManager != null) {
        	locManager.removeUpdates(this); //activity pauses => stop listening for updates
        }
    }
    
    protected void onStop() {
        super.onStop();
        if (locManager != null) {
        	locManager.removeUpdates(this);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
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
