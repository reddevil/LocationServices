package com.sahil.others;

import java.util.List;

public class TestingNearbyPlaces {

	private String[] placeName;
	private String[] imageUrl;

	public TestingNearbyPlaces() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestingNearbyPlaces t = new TestingNearbyPlaces();
		t.findNearLocation ();
		
	}
	
	public void findNearLocation()   {

	    PlacesService service = new PlacesService("AIzaSyB3P-xYQCtCl1dsIxSMLPvT3lRXNvoH8Ec");

	   /* 
	    Hear you should call the method find nearst place near to central park new delhi then we pass the lat and lang of central park. hear you can be pass you current location lat and lang.The third argument is used to set the specific place if you pass the atm the it will return the list of nearest atm list. if you want to get the every thing then you should be pass "" only   
	   */

	/* hear you should be pass the you current location latitude and langitude, */
	      //List<Place> findPlaces = service.findPlaces(28.632808,77.218276,"");
	      List<Place> findPlaces = service.findPlaces(33.7875972,-84.4045127,"");

	        placeName = new String[findPlaces.size()];
	        imageUrl = new String[findPlaces.size()];

	      for (int i = 0; i < findPlaces.size(); i++) {

	        Place placeDetail = findPlaces.get(i);
	        placeDetail.getIcon();

	        System.out.println(placeDetail.getName());
	        placeName[i] =placeDetail.getName();

	        imageUrl[i] =placeDetail.getIcon();

	    }
	}
}

