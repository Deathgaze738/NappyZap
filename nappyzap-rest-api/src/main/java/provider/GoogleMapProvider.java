package provider;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import dto.RoadRouteDTO;
import model.Address;

@Component("googleMapProvider")
public class GoogleMapProvider implements IMapProvider{

	private static final String GEOCODE_API_KEY = "AIzaSyCChtFbpBEODeTROR3jBLWY7Rvb6QgYWiI";
	private static final String GEOCODE_REQ_URL = "https://maps.googleapis.com/maps/api/geocode/json?";
	
	private static final String DISTANCE_API_KEY = "AIzaSyClD4n-mgVF0mL39HI9Fez1qAODSttwuOk";
	private static final String DISTANCE_REQ_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?";
	
	@Override
	public Address getLatLng(Address address) {
		try {
			//Build Param string
			StringBuilder sb = new StringBuilder();
			sb.append(GEOCODE_REQ_URL);
			sb.append("address=" + address.toGMapParam() + "&");
			sb.append("key=" + GEOCODE_API_KEY);
			//Create connection to Google Maps Geocoding API.
			URL url = new URL(sb.toString());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setRequestMethod("GET");
			
			//Send Request
			DataOutputStream out = new DataOutputStream(con.getOutputStream());
			out.close();
			
			//Get Geocode response
			if(con.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST){
				InputStream is = con.getInputStream();
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(is, "UTF-8"));
				if(jsonObject.get("status").equals("OK")){
					JSONArray results = (JSONArray) jsonObject.get("results");
					JSONObject resultsObj = (JSONObject) results.get(0);
					JSONObject geometry = (JSONObject) resultsObj.get("geometry");
					JSONObject location = (JSONObject) geometry.get("location");
					address.setLat((Double) location.get("lat"));
					address.setLng((Double) location.get("lng"));
				}
				else{
					return null;
				}
			}
			else{
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return address;
	}

	@Override
	public RoadRouteDTO getRoadRoute(Address address1, Address address2) {
		RoadRouteDTO roadRouteDto = new RoadRouteDTO();
		roadRouteDto.setRoadDistance(Long.MAX_VALUE);
		roadRouteDto.setTimeTaken(Long.MAX_VALUE);
		try {
			//Build Param string
			StringBuilder sb = new StringBuilder();
			sb.append(DISTANCE_REQ_URL);
			sb.append("origins=" + address1.toGMapParam() + "&");
			sb.append("destinations=" + address2.toGMapParam() + "&");
			sb.append("key=" + DISTANCE_API_KEY);
			//Create connection to Google Maps Distance Matrix API.
			URL url = new URL(sb.toString());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setRequestMethod("GET");
			
			//Send Request
			DataOutputStream out = new DataOutputStream(con.getOutputStream());
			out.close();
			
			//Get Geocode response
			if(con.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST){
				InputStream is = con.getInputStream();
				JSONParser jsonParser = new JSONParser();
				JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(is, "UTF-8"));
				if(jsonObject.get("status").equals("OK")){
					JSONArray rows = (JSONArray) jsonObject.get("rows");
					JSONObject rowsObj = (JSONObject) rows.get(0);
					JSONArray elements = (JSONArray) rowsObj.get("elements");
					JSONObject elementsObj = (JSONObject) elements.get(0);
					roadRouteDto.setRoadDistance((Long)((JSONObject)elementsObj.get("distance")).get("value"));
					roadRouteDto.setTimeTaken((Long)((JSONObject)elementsObj.get("duration")).get("value"));
					roadRouteDto.setTimeRetrieved(new Timestamp(System.currentTimeMillis()));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return roadRouteDto;
	}

}
