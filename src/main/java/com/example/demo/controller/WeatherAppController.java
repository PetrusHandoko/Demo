package com.example.demo.controller;

import com.example.demo.dto.USAddress;
import com.example.demo.dto.WeatherDataInfo;
import com.example.demo.service.WeatherDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherAppController  {

    @Autowired WeatherDataService weatherDataService ;

    @PostMapping("/weatherData")
    public WeatherDataInfo getWeatherInfo(@RequestBody USAddress address) {
        return weatherDataService.getWeatherDataInfo(address);
    }
}
