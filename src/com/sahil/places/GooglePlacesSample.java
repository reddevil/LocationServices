package com.sahil.places;


import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson.JacksonFactory;

public class GooglePlacesSample {

	// Create our transport.
	private static final HttpTransport transport = new ApacheHttpTransport();
	
	// Fill in the API key you want to use.
	private static final String API_KEY = "AIzaSyB3P-xYQCtCl1dsIxSMLPvT3lRXNvoH8Ec";
	
	// The different Places API endpoints.
	private static final String PLACES_SEARCH_URL =  "https://maps.googleapis.com/maps/api/place/search/json?";
	//private static final String PLACES_SEARCH_URL =  "https://maps.googleapis.com/maps/api/place/textsearch/json?";
	private static final String PLACES_AUTOCOMPLETE_URL = "https://maps.googleapis.com/maps/api/place/autocomplete/json?";
	private static final String PLACES_DETAILS_URL = "https://maps.googleapis.com/maps/api/place/details/json?";
	
	private static final String PLACES_SEARCH_QUERY_URL =  "https://maps.googleapis.com/maps/api/place/search/json?";
	
	
	private static final boolean PRINT_AS_STRING = false;
	
	// Moscone Center, Howard Street, San Francisco, CA, United States
	//double latitude = 37.784147;
	//double longitude = -122.402115;
	
	double latitude = 33.7875972;
	double longitude = -84.4045127;
	
	// telenet
	//double latitude = 51.034823;
	//double longitude = 4.483774;

	// home
	//double latitude = 50.963062;
	//double longitude = 3.522255;
	
	public static void main(String[] args) throws Exception {
		double latitude = 33.7875972;
		double longitude = -84.4045127;
		GooglePlacesSample sample = new GooglePlacesSample();
		PlacesList places = sample.performQuerySearch("Target", latitude, longitude);
		for (Place place : places.results) {
			//place.convertLonLat();
			System.out.println(place);
			//performDetails(place.reference);
		}
		//sample.performSearch();
		//sample.performDetails("CnRtAAAATk9IL_xAKeSvHXp8_HgRIeYBg4WEKXPdaTp1SbYumSWBQOXsxCSIe1vE8wb3V4beQymGJrKXTUgpWXlnYIxoLCTijO-aMyObxzS_aQOAxTFQqfQohb9YuBddllTaeiDhNeTh8sB4LUP7BOYfu1o0zRIQpdJKnwdPABlgFUs3BIVTkhoUdmJJq1AIbISzW2JpY497I5lYIqo");
		//sample.performAutoComplete();
	}
	
	
	public PlacesList performQuerySearch(String query, double latitude, double longitude) throws Exception {
		PlacesList places = null;
		try {
			System.out.println("Perform Query Search ....");
			System.out.println("-------------------");
			HttpRequestFactory httpRequestFactory = createRequestFactory(transport);
			HttpRequest request = httpRequestFactory.buildGetRequest(new GenericUrl(PLACES_SEARCH_QUERY_URL));
			request.getUrl().put("key", API_KEY);
			request.getUrl().put("location", latitude + "," + longitude);
			request.getUrl().put("radius", 500);
			request.getUrl().put("sensor", "false");
			request.getUrl().put("name", query);
			
			if (PRINT_AS_STRING) {
				//System.out.println(request.execute().parseAsString());
			} else {
				
				places = request.execute().parseAs(PlacesList.class);
				//System.out.println("STATUS = " + places.status);
				for (Place place : places.results) {
					place.convertLonLat();
					//System.out.println(place);
					//performDetails(place.reference);
				}
			}
			

		} catch (HttpResponseException e) {
			//System.err.println(e.response.parseAsString());
			throw e;
		}		
		
		return places;
		
	}
	
	
	public void performSearch() throws Exception {
		try {
			System.out.println("Perform Search ....");
			System.out.println("-------------------");
			HttpRequestFactory httpRequestFactory = createRequestFactory(transport);
			HttpRequest request = httpRequestFactory.buildGetRequest(new GenericUrl(PLACES_SEARCH_URL));
			request.getUrl().put("key", API_KEY);
			request.getUrl().put("location", latitude + "," + longitude);
			request.getUrl().put("radius", 500);
			request.getUrl().put("sensor", "false");
			
			if (PRINT_AS_STRING) {
				System.out.println(request.execute().parseAsString());
			} else {
				
				PlacesList places = request.execute().parseAs(PlacesList.class);
				System.out.println("STATUS = " + places.status);
				for (Place place : places.results) {
					System.out.println(place);
				}
			}
			

		} catch (HttpResponseException e) {
			//System.err.println(e.response.parseAsString());
			throw e;
		}
	}
	
	public void performDetails(String reference) throws Exception {
		try {
			System.out.println("Perform Place Detail....");
			System.out.println("-------------------");
			HttpRequestFactory httpRequestFactory = createRequestFactory(transport);
			HttpRequest request = httpRequestFactory.buildGetRequest(new GenericUrl(PLACES_DETAILS_URL));
			request.getUrl().put("key", API_KEY);
			request.getUrl().put("reference", reference);
			request.getUrl().put("sensor", "false");
			
			if (PRINT_AS_STRING) {
				System.out.println(request.execute().parseAsString());
			} else {
				PlaceDetail place = request.execute().parseAs(PlaceDetail.class);
				System.out.println(place);
			}

		} catch (HttpResponseException e) {
			//System.err.println(e.response.parseAsString());
			throw e;
		}
	}
	
	
	
	/*public void performAutoComplete() throws Exception {
		try {
			System.out.println("Perform Autocomplete ....");
			System.out.println("-------------------------");
			
			HttpRequestFactory httpRequestFactory = createRequestFactory(transport);
			HttpRequest request = httpRequestFactory.buildGetRequest(new GenericUrl(PLACES_AUTOCOMPLETE_URL));
			request.getUrl().put("key", API_KEY);
			request.getUrl().put("input", "mos");
			request.getUrl().put("location", latitude + "," + longitude);
			request.getUrl().put("radius", 500);
			request.getUrl().put("sensor", "false");
			PlacesAutocompleteList places = request.execute().parseAs(PlacesAutocompleteList.class);
			if (PRINT_AS_STRING) {
				System.out.println(request.execute().parseAsString());
			} else {
				for (PlaceAutoComplete place : places.predictions) {
					System.out.println(place);
				}
			}

		} catch (HttpResponseException e) {
			//System.err.println(e.response.parseAsString());
			throw e;
		}
	}	*/
	
	public static HttpRequestFactory createRequestFactory(final HttpTransport transport) {
			   
		  return transport.createRequestFactory(new HttpRequestInitializer() {
		   public void initialize(HttpRequest request) {
		    GoogleHeaders headers = new GoogleHeaders();
		    headers.setApplicationName("Google-Places-DemoApp");
		    request.setHeaders(headers);
		    //request.headers=headers;
		    JsonObjectParser parser = new JsonObjectParser(new JacksonFactory());
		    
		    //parser.jsonFactory = new JacksonFactory();
		    request.setParser(parser);
		    //request.addParser(parser);
		   }
		});
	}
}
