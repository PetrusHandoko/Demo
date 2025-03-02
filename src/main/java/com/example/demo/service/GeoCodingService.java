package com.example.demo.service;

import com.example.demo.dto.USAddress;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

public interface GeoCodingService {
    public Pair<Double, Double> getLocationFromAddress(USAddress address);
}

@Service
class MapQuestGeoCodingAPIService implements GeoCodingService {

    private static final String API_KEY = "UuYY2hIbN0YHB0s6aPWrIwMZIQ0kjjc9";
    private static final String GEOCODING_URL = "http://www.mapquestapi.com/geocoding/v1/address?key=" + API_KEY + "&location=";
    private static final Pair<Double, Double> SARATOGA = Pair.of(37.26736, -122.0284);

    @Autowired
    public RestTemplateBuilder restTemplateBuilder;


    @Override
    public  Pair<Double, Double>  getLocationFromAddress(USAddress address) {
        if ( API_KEY.isEmpty() ){
            return SARATOGA; // Hardcoded to saratoga
        }
        try {
            JSONObject location = geocodeAddress(address.formattedAddress());
            if (location != null) {
                double latitude = location.getDouble("latitude") ;
                double longitude = location.getDouble("longitude");
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