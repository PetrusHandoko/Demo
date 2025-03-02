package com.example.demo.service;

import com.example.demo.dto.USAddress;
import com.example.demo.dto.WeatherDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherDataServiceImpl implements WeatherDataService {

    @Autowired
    ExternalWeatherServiceApi weatherDataService ;

    @Override
    public WeatherDataInfo getWeatherDataInfo(USAddress address) {
        return weatherDataService.getDataFromExternalApi(address);
    }
}
