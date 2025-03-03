package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "demo")
public class DemoConfig {
    private String MapQuestGeoCodingAPIKey;

    public String getMapQuestGeoCodingAPIKey() {
        return MapQuestGeoCodingAPIKey;
    }

    public void setMapQuestGeoCodingAPIKey(String mapQuestGeoCodingAPIKey) {
        MapQuestGeoCodingAPIKey = mapQuestGeoCodingAPIKey;
    }

    private String MapQuestBaseURL;

    public String getMapQuestBaseURL() {
        return MapQuestBaseURL;
    }
    public void setMapQuestBaseURL(String mapQuestBaseURL) {
        MapQuestBaseURL = mapQuestBaseURL;
    }


}
