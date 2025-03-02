package com.example.demo.service;

import com.example.demo.dto.USAddress;
import com.example.demo.dto.WeatherDataInfo;


public interface WeatherDataService {
    public WeatherDataInfo getWeatherDataInfo (USAddress address );
}

