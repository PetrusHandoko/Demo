package com.example.demo.service;

import com.example.demo.config.DemoGeoCodingConfig;
import com.example.demo.dto.USAddress;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

public interface GeoCodingService {
    Pair<Double, Double> getLocationFromAddress(USAddress address);
}

@Service("MapQuest")
@ConditionalOnProperty(value = "demo.GeoCodingAPIBeanName.MapQuest", havingValue = "true")
class MapQuestGeoCodingAPIService implements GeoCodingService {

    private String API_KEY;
    private String GEOCODING_URL;

    private static final Pair<Double, Double> SARATOGA = Pair.of(37.26736, -122.0284);

    @Autowired
    public RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private DemoGeoCodingConfig appConfig;

    public MapQuestGeoCodingAPIService(DemoGeoCodingConfig appConfig, RestTemplateBuilder restTemplateBuilder) {
        API_KEY = appConfig.getMapQuestGeoCodingAPIKey();
        GEOCODING_URL = "https://geocoding.geo.census.gov/geocoder/locations/onelineaddress?key=" + API_KEY + "&location=";
    }

    @Override
    public  Pair<Double, Double>  getLocationFromAddress(USAddress address) {
        if ( API_KEY.isEmpty() ){
            return SARATOGA; // Hardcoded to saratoga
        }
        try {
            JSONObject location = geocodeAddress(address.formattedAddress());
            if (location != null) {
                double latitude = location.getDouble("lat") ;
                double longitude = location.getDouble("lng");
                System.out.println("Latitude: " + latitude);
                System.out.println("Longitude: " + longitude);
                return Pair.of(latitude, longitude);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        // Return default
        return SARATOGA ;
    }

    private JSONObject geocodeAddress(String address) throws IOException, JSONException {
        String encodedAddress = address.replace(" ", "%20");
        String url = GEOCODING_URL + encodedAddress;

        RestTemplate restTemplate = restTemplateBuilder.build();

        // We could map directly using JSON mapper
        String returnData = restTemplate.getForObject(url, String.class);

        JSONObject jsonResponse = new JSONObject(returnData);
        JSONArray results = jsonResponse.getJSONArray("results");
        if (results.length() > 0) {
            JSONObject locations = results.getJSONObject(0);
            JSONArray locationsArray = locations.getJSONArray("locations");
            if (locationsArray.length() > 0){
                JSONObject location = locationsArray.getJSONObject(0);
                JSONObject latLng = location.getJSONObject("latLng");
                return latLng;
            }
        }
        return null;
    }
}

//------
@Service("USCensus")
@ConditionalOnProperty(value = "demo.GeoCodingAPIBeanName.USCensus", havingValue = "true")
class USCensusGovGeoCodingAPIService implements GeoCodingService {

    private String GEOCODING_URL;

    private static final Pair<Double, Double> SARATOGA = Pair.of(37.26736, -122.0284);

    @Autowired
    public RestTemplateBuilder restTemplateBuilder;

    public USCensusGovGeoCodingAPIService(DemoGeoCodingConfig appConfig, RestTemplateBuilder restTemplateBuilder) {
        GEOCODING_URL = appConfig.getUSCensusGovBaseURL();
    }

    @Override
    public  Pair<Double, Double>  getLocationFromAddress(USAddress address) {
        try {
            return geocodeAddress(address.formattedAddress());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        // Return default
        return SARATOGA ;
    }

    private Pair<Double, Double> geocodeAddress(String address) throws IOException, JSONException {
        String encodedAddress = address.replace(" ", "+");
        String url = GEOCODING_URL + encodedAddress ;

        RestTemplate restTemplate = restTemplateBuilder.build();

        // We could map directly using JSON mapper
        String returnData = restTemplate.getForObject(url, String.class);

        JSONObject jsonResponse = new JSONObject(returnData);
        var addresses = jsonResponse.getJSONObject("result").getJSONArray("addressMatches");

        if (addresses.length() > 0) {
            JSONObject addressesJSONObject = addresses.getJSONObject(0);
            JSONObject location = addressesJSONObject.getJSONObject("coordinates");
            Double latitude = (int)(location.getDouble("y")*10000)/10000.0;
            Double longitude = (int)(location.getDouble("x")*10000)/10000.0;
            return Pair.of(latitude, longitude);
        }
        return null;
    }



}