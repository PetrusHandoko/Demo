package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "demo")
public class DemoConfig {

    private String GeoCodingAPIBeanName;

    public String getGeoCodingAPIBeanName() {
        return GeoCodingAPIBeanName;
    }

    public void setGeoCodingAPIBeanName(String geoCodingAPIBeanName) {
        GeoCodingAPIBeanName = geoCodingAPIBeanName;
    }
}

