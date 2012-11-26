package com.sahil.others;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class HelloItemizedOverlay1 extends ItemizedOverlay {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	
	public HelloItemizedOverlay1(Drawable defaultMarker) {
		  super(boundCenterBottom(defaultMarker));
	}
	
	public HelloItemizedOverlay1(Drawable defaultMarker, Context context) {
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
	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	  //AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
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
