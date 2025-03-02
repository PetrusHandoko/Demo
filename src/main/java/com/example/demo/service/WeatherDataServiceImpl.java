package com.example.demo.service;

import com.example.demo.dto.USAddress;
import com.example.demo.dto.WeatherDataInfo;
import org.springframework.stereotype.Service;

@Service
public class WeatherDataServiceImpl implements WeatherDataService {


    @Override
    public WeatherDataInfo getWeatherDataInfo(USAddress address) {
        return new WeatherDataInfo(50,70,45, "Cloudy");
    }
}
