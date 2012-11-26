package com.sahil.others;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * @author dwivedi ji     * 
 *        */
public class CheckInActivity extends ListActivity {

private String[] placeName;
private String[] imageUrl;
@Override
protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);




    new GetPlaces(this,getListView()).execute();
}

class GetPlaces extends AsyncTask<Void, Void, Void>{
    Context context;
    private ListView listView;
    private ProgressDialog bar;
    public GetPlaces(Context context, ListView listView) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.listView = listView;
    }

    @Override
    protected void onPostExecute(Void result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        bar.dismiss();
          this.listView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, placeName));

    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
          bar =  new ProgressDialog(context);
        bar.setIndeterminate(true);
        bar.setTitle("Loading");
        bar.show();


    }

    @Override
    protected Void doInBackground(Void... arg0) {
        // TODO Auto-generated method stub
        findNearLocation();
        return null;
    }

}
public void findNearLocation()   {

    PlacesService service = new PlacesService("AIzaSyB3P-xYQCtCl1dsIxSMLPvT3lRXNvoH8Ec");

   /* 
    Hear you should call the method find nearst place near to central park new delhi then we pass the lat and lang of central park. hear you can be pass you current location lat and lang.The third argument is used to set the specific place if you pass the atm the it will return the list of nearest atm list. if you want to get the every thing then you should be pass "" only   
   */

/* hear you should be pass the you current location latitude and langitude, */
      List<Place> findPlaces = service.findPlaces(28.632808,77.218276,"");

        placeName = new String[findPlaces.size()];
        imageUrl = new String[findPlaces.size()];

      for (int i = 0; i < findPlaces.size(); i++) {

          Place placeDetail = findPlaces.get(i);
          placeDetail.getIcon();

        System.out.println(  placeDetail.getName());
        placeName[i] =placeDetail.getName();

        imageUrl[i] =placeDetail.getIcon();

    }





}


}
