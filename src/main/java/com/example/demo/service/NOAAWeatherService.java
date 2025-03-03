package com.example.demo.service;

import com.example.demo.dto.USAddress;
import com.example.demo.dto.WeatherDataInfo;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

interface ExternalWeatherServiceApi {
    WeatherDataInfo getDataFromExternalApi(USAddress address);
}

@Service
public class NOAAWeatherService implements ExternalWeatherServiceApi{

    @Autowired RestTemplateBuilder restTemplateBuilder;

    @Autowired
    @Qualifier( "USCensus")
    GeoCodingService geoCodingService;

    @Override
    public WeatherDataInfo getDataFromExternalApi(USAddress address) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        // We could map directly using JSON mapper
        String returnData = restTemplate.getForObject(getObservationDataURL(toStationId(address)), String.class);
        return toWeatherDataInfo(returnData);
    }

    private String toStationId(USAddress address){
        // To find station id
        return getObservationStation(address); //KSJC AW020
    }

    private WeatherDataInfo toWeatherDataInfo(String responseData){
        try {
            JSONObject jsonResponse = new JSONObject(responseData);
            JSONObject properties = jsonResponse.getJSONObject("properties");
            JSONObject temperature = properties.getJSONObject("temperature");
            double currentTemp = (temperature.getString("value").equals("null"))? -99: temperature.getDouble("value");

            temperature = properties.getJSONObject("minTemperatureLast24Hours");
            double minTemperature = (temperature.getString("value").equals("null") )? -99: temperature.getDouble("value");

            temperature = properties.getJSONObject("maxTemperatureLast24Hours");
            double maxTemperature = (temperature.getString("value").equals("null") )? -99: temperature.getDouble("value");
            var description = properties.getString("textDescription");
            return new WeatherDataInfo (toStringTemperature(currentTemp, true),
                    toStringTemperature(minTemperature, true),
                    toStringTemperature(maxTemperature, true), description) ;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private String toStringTemperature(double temperature, boolean isCelsius){
        if ( temperature == -99 ) return "N/A";
        if ( isCelsius ) temperature = (temperature * 9.0/5.0) + 32;
        return String.format("%.1f", temperature);
    }

    protected String getObservationDataURL(String stationId ){
        return String.format("https://api.weather.gov/stations/%s/observations/latest", stationId);
    }

    protected String getObservationStation(USAddress address){
        RestTemplate restTemplate = restTemplateBuilder
                .defaultHeader("accept", "application/geo+json")
                .build();
        var location = geoCodingService.getLocationFromAddress(address);
        String pointsUrl = String.format("https://api.weather.gov/points/%s,%s", location.getLeft(), location.getRight());
        // We could map directly using JSON mapper

        String observationStationsURL;
        String returnData = restTemplate.getForObject(pointsUrl, String.class);
        try {
            JSONObject jsonResponse = new JSONObject(returnData);
            var properties = jsonResponse.getJSONObject("properties");
            observationStationsURL = properties.getString("observationStations");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //observationStations
        String observations = restTemplate.getForObject(observationStationsURL, String.class);
        try {
            JSONObject jsonResponse = new JSONObject(observations);
            //var features = jsonResponse.getJSONArray("features");
            var stations = jsonResponse.getJSONArray("observationStations") ;
            // Picked the first station.  In general the closes one? If not do some calc on location
            var tokens = stations.getString(0).split("/");

            System.out.println("Picked" + tokens[tokens.length-1]);
            return tokens[tokens.length-1] ;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

}
