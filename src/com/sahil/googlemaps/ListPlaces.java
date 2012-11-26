package com.sahil.googlemaps;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.sahil.others.Loc;
import com.sahil.others.MyLoc;
import com.sahil.others.MyLocation;
import com.sahil.places.PlacesList;

public class ListPlaces extends Activity {

	private MyLoc my_location;	
	private LocationManager mLocationManager;
      
    private static final int TEN_SECONDS = 10000;
    private static final int TEN_METERS = 10;
    private static final int TWO_MINUTES = 1000 * 60 * 2;
	
	private String locations;
	MyLocation myLocation = new MyLocation();
	
	double lon = 0;
    double lat = 0;
	
	Loc allLocs[];
	private com.sahil.places.GooglePlacesSample g;
	private String message;
	private TextView textView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listplaces);
		// Show the Up button in the action bar.
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Get the message from the intent
		Intent intent = getIntent();
		message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		
		g = new com.sahil.places.GooglePlacesSample();
		//g.performQuerySearch(query, latitude, longitude);
		
		// Create the text view
	    textView = new TextView(this);
	    /*textView.setTextSize(40);
	    textView.setText(message);*/

	    // Set the text view as the activity layout
	    setContentView(textView);
	    
	    mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);        
        try {
			setup();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.listplaces, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	protected void onStart () {
		super.onStart();
	     // Check if the GPS setting is currently enabled on the device.
	        // This verification should be done during onStart() because the system calls this method
	        // when the user returns to the activity, which ensures the desired location provider is
	        // enabled each time the activity resumes from the stopped state.
	        LocationManager locationManager =
	                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

	        if (!gpsEnabled) {
	            // Build an alert dialog here that requests that the user enable
	            // the location services, then when the user clicks the "OK" button,
	            // call enableLocationSettings()
	            enableLocationSettings();
	        }
	}
	
	private void enableLocationSettings() {
        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }
	
	private void setup () throws Exception {
    	Location gpsLocation = null;
        Location networkLocation = null;
        mLocationManager.removeUpdates(listener);                
        
     // Request updates from both fine (gps) and coarse (network) providers.
        gpsLocation = requestUpdatesFromProvider(
                LocationManager.GPS_PROVIDER, R.string.not_support_gps);
        networkLocation = requestUpdatesFromProvider(
                LocationManager.NETWORK_PROVIDER, R.string.not_support_network);

        // If both providers return last known locations, compare the two and use the better
        // one to update the UI.  If only one provider returns a location, use it.
        if (gpsLocation != null && networkLocation != null) {
            updateUILocation(getBetterLocation(gpsLocation, networkLocation));
        } else if (gpsLocation != null) {
            updateUILocation(gpsLocation);
        } else if (networkLocation != null) {
            updateUILocation(networkLocation);
        }
        
    }
    
    /**
     * Method to register location updates with a desired location provider.  If the requested
     * provider is not available on the device, the app displays a Toast with a message referenced
     * by a resource id.
     *
     * @param provider Name of the requested provider.
     * @param errorResId Resource id for the string message to be displayed if the provider does
     *                   not exist on the device.
     * @return A previously returned {@link android.location.Location} from the requested provider,
     *         if exists.
     */
    private Location requestUpdatesFromProvider(final String provider, final int errorResId) {
        Location location = null;
        if (mLocationManager.isProviderEnabled(provider)) {
            mLocationManager.requestLocationUpdates(provider, TEN_SECONDS, TEN_METERS, listener);
            location = mLocationManager.getLastKnownLocation(provider);
        } else {
            Toast.makeText(this, errorResId, Toast.LENGTH_LONG).show();
        }
        return location;
    }
    
    private void doReverseGeocoding(Location location) {
        // Since the geocoding API is synchronous and may take a while.  You don't want to lock
        // up the UI thread.  Invoking reverse geocoding in an AsyncTask.
        (new ReverseGeocodingTask(this)).execute(new Location[] {location});
    }
	
	
	private void updateUILocation(Location location) throws Exception {
        // We're sending the update to a handler which then updates the UI with the new
        // location.
        
		if (location == null) {
        	return;
        }
		
        lat = location.getLatitude();
        lon = location.getLongitude();                                           
        
        String name_address = "";
        
        class retrievePlaces extends AsyncTask<String, Void, PlacesList> {

			@Override
			protected PlacesList doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				try {
					com.sahil.places.PlacesList p = g.performQuerySearch(message, lat, lon);
					return p;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
				//return null;
			}
        	
        }
        //com.sahil.places.PlacesList p = g.performQuerySearch(message, lat, lon);
        AsyncTask<String, Void, PlacesList> p = new retrievePlaces().execute(message);
        
        for (com.sahil.places.Place place : p.get().results) {
        	name_address += (place.name + "\n" + place.vicinity + "\n\n");
        }
        
        textView.setTextSize(10);
	    textView.setText(name_address);
        
        
        /*List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.androidmarker);
        HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(drawable, this);
        GeoPoint point = new GeoPoint((int)(lat * 1E6),(int)(lon * 1E6));
        
        OverlayItem overlayitem = new OverlayItem(point, "Lol!", "Current Location!");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        MapController mapController = mapView.getController();
        mapController.setCenter(point);*/
        
        //mapView.invalidate();
        // Bypass reverse-geocoding only if the Geocoder service is available on the device.
        //if (mGeocoderAvailable) doReverseGeocoding(location);
    }
	
	private final LocationListener listener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            // A new location update is received.  Do something useful with it.  Update the UI with
            // the location update.
            try {
				updateUILocation(location);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    
    /** Determines whether one Location reading is better than the current Location fix.
     * Code taken from
     * http://developer.android.com/guide/topics/location/obtaining-user-location.html
     *
     * @param newLocation  The new Location that you want to evaluate
     * @param currentBestLocation  The current Location fix, to which you want to compare the new
     *        one
     * @return The better Location object based on recency and accuracy.
     */
   protected Location getBetterLocation(Location newLocation, Location currentBestLocation) {
       if (currentBestLocation == null) {
           // A new location is always better than no location
           return newLocation;
       }

       // Check whether the new location fix is newer or older
       long timeDelta = newLocation.getTime() - currentBestLocation.getTime();
       boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
       boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
       boolean isNewer = timeDelta > 0;

       // If it's been more than two minutes since the current location, use the new location
       // because the user has likely moved.
       if (isSignificantlyNewer) {
           return newLocation;
       // If the new location is more than two minutes older, it must be worse
       } else if (isSignificantlyOlder) {
           return currentBestLocation;
       }

       // Check whether the new location fix is more or less accurate
       int accuracyDelta = (int) (newLocation.getAccuracy() - currentBestLocation.getAccuracy());
       boolean isLessAccurate = accuracyDelta > 0;
       boolean isMoreAccurate = accuracyDelta < 0;
       boolean isSignificantlyLessAccurate = accuracyDelta > 200;

       // Check if the old and new location are from the same provider
       boolean isFromSameProvider = isSameProvider(newLocation.getProvider(),
               currentBestLocation.getProvider());

       // Determine location quality using a combination of timeliness and accuracy
       if (isMoreAccurate) {
           return newLocation;
       } else if (isNewer && !isLessAccurate) {
           return newLocation;
       } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
           return newLocation;
       }
       return currentBestLocation;
   }

   /** Checks whether two providers are the same */
   private boolean isSameProvider(String provider1, String provider2) {
       if (provider1 == null) {
         return provider2 == null;
       }
       return provider1.equals(provider2);
   }
   
// AsyncTask encapsulating the reverse-geocoding API.  Since the geocoder API is blocked,
   // we do not want to invoke it from the UI thread.
   private class ReverseGeocodingTask extends AsyncTask<Location, Void, Void> {
       Context mContext;

       public ReverseGeocodingTask(Context context) {
           super();
           mContext = context;
       }

       protected Void doInBackground(Location... params) {
           Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

           Location loc = params[0];
           List<Address> addresses = null;
           try {
               addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
           } catch (IOException e) {
               e.printStackTrace();
           }
           if (addresses != null && addresses.size() > 0) {
               Address address = addresses.get(0);
               // Format the first line of address (if available), city, and country name.
               String addressText = String.format("%s, %s, %s",
                       address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                       address.getLocality(),
                       address.getCountryName());
           }
           return null;
       }
   }
	
	
	/* When the activity starts up, request updates */
    @Override
    protected void onResume() {
        super.onResume(); 
        try {
			setup ();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        /*if (locManager != null) {
        	locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }*/
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        
    }
    
    protected void onStop() {
        super.onStop();
        mLocationManager.removeUpdates(listener);        
    }
       

	

}
