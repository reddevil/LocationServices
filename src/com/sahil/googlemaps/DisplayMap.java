package com.sahil.googlemaps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.widget.Toast;


import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.sahil.others.Loc;
import com.sahil.others.MyLoc;
import com.sahil.others.MyLocation;

public class DisplayMap extends MapActivity implements LocationListener {

	private MapView mapView;    
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
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        
        double lon = 0;
        double lat = 0;
        
        if (savedInstanceState != null) {
        	
        }
   
        //fetch the map view from the layout
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.androidmarker);
        HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(drawable, this);
        
        // Get a reference to the LocationManager object.
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        setup();
        
        
        
        
        
        //System.out.println ("Outside:" + locations);
        
        //System.out.println ("Outside: " + myLocation.lat + "," + myLocation.lon);
        
    
    }

    // Restores UI states after rotation.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    
    @Override
    protected void onStart() {
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
    
    private void setup () {
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
    
    private void updateUILocation(Location location) {
        // We're sending the update to a handler which then updates the UI with the new
        // location.
         
        lat = location.getLatitude();
        lon = location.getLongitude();                
        
        
        
        if (mapView == null) {
        	mapView = (MapView) findViewById(R.id.mapview);
        }
        mapView.setBuiltInZoomControls(true);
        
        if (location == null) {
        	return;
        }
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.androidmarker);
        HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(drawable, this);
        GeoPoint point = new GeoPoint((int)(lat * 1E6),(int)(lon * 1E6));
        
        OverlayItem overlayitem = new OverlayItem(point, "Lol!", "Current Location!");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        MapController mapController = mapView.getController();
        mapController.setCenter(point);
        
        //mapView.invalidate();
        // Bypass reverse-geocoding only if the Geocoder service is available on the device.
        //if (mGeocoderAvailable) doReverseGeocoding(location);
    }
    
    private final LocationListener listener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            // A new location update is received.  Do something useful with it.  Update the UI with
            // the location update.
            updateUILocation(location);
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
   public class ReverseGeocodingTask extends AsyncTask<Location, Void, Void> {
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
        
    }
    
    protected void onStop() {
        super.onStop();
        mLocationManager.removeUpdates(listener);        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.display_main, menu);
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
	
	
	public class HelloItemizedOverlay extends ItemizedOverlay {

		private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
		private Context mContext;
		
		public HelloItemizedOverlay(Drawable defaultMarker) {
			  super(boundCenterBottom(defaultMarker));
		}
		
		public HelloItemizedOverlay(Drawable defaultMarker, Context context) {
			  super(boundCenterBottom(defaultMarker));
			  mContext = context;
			}
		
		public void addOverlay(OverlayItem overlay) {
		    mOverlays.add(overlay);
		    populate();
		}
		
		@Override
		protected boolean onTap(int index) {
		  OverlayItem item = mOverlays.get(index);
		  //AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		  AlertDialog.Builder dialog = new AlertDialog.Builder(DisplayMap.this);
		  dialog.setTitle(item.getTitle());
		  dialog.setMessage(item.getSnippet());
		  dialog.show();
		  return true;
		}
		
		/*public HelloItemizedOverlay(Drawable arg0) {
			super(arg0);
			// TODO Auto-generated constructor stub
		}*/

		@Override
		protected OverlayItem createItem(int arg0) {
			// TODO Auto-generated method stub
			 return mOverlays.get(arg0);
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return mOverlays.size();
		}

	}

	
	
}
