package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "demo.geocoding")
public class DemoGeoCodingConfig {
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

    private String USCensusGovBaseURL;

    public String getUSCensusGovBaseURL() {
        return USCensusGovBaseURL;
    }
    public void setUSCensusGovBaseURL(String url) {
        USCensusGovBaseURL = url;
    }

}
