// Include package as per your project structure
package com.example.demo;

import com.example.demo.dto.USAddress;
import com.example.demo.dto.WeatherDataInfo;
import com.example.demo.service.WeatherDataService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WeatherDataService weatherService;

    @Test
    void testGetWeatherDataInfo() throws Exception {
        var expectedWeather = new WeatherDataInfo("30","55","25","Sunny");
        var address = new USAddress("12345", "New York", "NY", "10001");
//        if ( weatherService == null ) return ;
        when(weatherService.getWeatherDataInfo(address)).thenReturn(expectedWeather);
        var addressJson = "{\"street\":\"12345\",\"city\":\"New York\",\"state\":\"NY\",\"zipCode\":\"10001\"}";
        var expectedWeatherJson = "{\"Temperature\":\"30\",\"high\":\"55\",\"low\":\"25\",\"description\":\"Sunny\"}";
        // Act & Assert
        mockMvc.perform(post("/weatherData")
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(addressJson))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedWeatherJson));

        verify(weatherService, times(1)).getWeatherDataInfo(address);
    }
}