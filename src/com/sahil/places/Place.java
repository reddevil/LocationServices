package com.sahil.places;

import com.google.api.client.util.Key;


public class Place {

	@Key
	public String id;
	
	@Key
	public String name;
	
	@Key
	public String vicinity;
	
	@Key
	public String reference;

	@Key
	public double lat;
	
	@Key
	public double lng;
	
	@Key
	public Object geometry;
	
	@Override
	public String toString() {
		int first,last;
		String loc = geometry.toString();
		
		first = loc.indexOf("lat=");
		first = first + "lat=".length();
		last = loc.indexOf(", ");
		String lat = loc.substring(first, last);
		
		first = loc.indexOf("lng=");
		first = first + "lng=".length();
		last = loc.indexOf("}");		
		String lon = loc.substring(first, last);
		return name + " - " + vicinity + " - " + " - " + lat + " - " + lon + " - " + id + " - " + reference;
	}
	
	public void convertLonLat () {
		int first,last;
		String loc = geometry.toString();
		
		first = loc.indexOf("lat=");
		first = first + "lat=".length();
		last = loc.indexOf(", ");
		String lati = loc.substring(first, last);
		
		first = loc.indexOf("lng=");
		first = first + "lng=".length();
		last = loc.indexOf("}");		
		String loni = loc.substring(first, last);
		
		lat = Double.parseDouble(lati);
		lng = Double.parseDouble(loni);
		
		
	}
	
}
