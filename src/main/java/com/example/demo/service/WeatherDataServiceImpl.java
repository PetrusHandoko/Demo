package com.example.demo.service;

import com.example.demo.dto.USAddress;
import com.example.demo.dto.WeatherDataInfo;
import com.example.demo.model.TTLCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherDataServiceImpl implements WeatherDataService {

    @Autowired
    ExternalWeatherServiceApi weatherDataService ;

    private final TTLCache<String, WeatherDataInfo> cache = new TTLCache<>(30*60*1000);

    @Override
    public WeatherDataInfo getWeatherDataInfo(USAddress address) {
        var result = cache.get(address.zipCode());
        if ( result == null ){
            result = weatherDataService.getDataFromExternalApi(address);
            cache.put(address.zipCode(), result);
        }
        return result;
    }
}
