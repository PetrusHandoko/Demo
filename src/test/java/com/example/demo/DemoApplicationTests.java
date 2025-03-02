package com.example.demo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}


	public static void weatherPointTest(String[] args) throws IOException, InterruptedException {
		// Replace with your desired latitude and longitude
		double latitude = 39.7456;
		double longitude = -97.0892;

		// Construct the API URL to get the grid data
		String pointsUrl = String.format("https://api.weather.gov/points/%s,%s", latitude, longitude);

		// Create an HttpClient
		HttpClient client = HttpClient.newHttpClient();

		// Create an HttpRequest for the points URL
		HttpRequest pointsRequest = HttpRequest.newBuilder()
				.uri(URI.create(pointsUrl))
				.header("accept", "application/geo+json")
				.build();

		// Send the request and get the response for points URL
		HttpResponse<String> pointsResponse = client.send(pointsRequest, HttpResponse.BodyHandlers.ofString());

		//Check if the request was successful
		if (pointsResponse.statusCode() == 200) {
			// Extract the forecast URL from the JSON response
			String forecastUrl = pointsResponse.body().substring(pointsResponse.body().indexOf("forecast\":\"") + 11, pointsResponse.body().indexOf("\",\"forecastHourly\""));


			// Create an HttpRequest for the forecast URL
			HttpRequest forecastRequest = HttpRequest.newBuilder()
					.uri(URI.create(forecastUrl))
					.header("accept", "application/geo+json")
					.build();

			// Send the request and get the response for forecast URL
			HttpResponse<String> forecastResponse = client.send(forecastRequest, HttpResponse.BodyHandlers.ofString());

			// Print the response body
			System.out.println(forecastResponse.body());

		} else {
			System.err.println("Failed to retrieve forecast URL. Status code: " + pointsResponse.statusCode());
			System.err.println("Response body: " + pointsResponse.body());
		}
	}



	public static void weatherObeservationTest(String[] args) {
		String apiUrl = "https://api.weather.gov/stations/AW020/observations/latest";
		try {
			URL url = new URL(apiUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				StringBuilder response = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				reader.close();

				JSONObject jsonResponse = null;
				try {
					jsonResponse = new JSONObject(response.toString());
					JSONObject properties = jsonResponse.getJSONObject("properties");
					JSONObject temperature = properties.getJSONObject("temperature");
					double value = temperature.getDouble("value");
// minTemperatureLast24Hours maxTemperatureLast24Hours
					System.out.println("Temperature in Celsius: " + value);
				} catch (JSONException e) {
					throw new RuntimeException(e);
				}
			} else {
				System.out.println("Error: " + responseCode);
			}
			connection.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static JSONObject geocodeAddress(String address) throws IOException, JSONException {
		String encodedAddress = address.replace(" ", "%20");
		URL url = new URL("GEOCODING_URL" + encodedAddress);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}

			JSONObject jsonResponse = new JSONObject(response.toString());
			JSONArray results = jsonResponse.getJSONArray("results");
			if (results.length() > 0) {
				JSONObject locations = results.getJSONObject(0);
				JSONArray locationsArray = locations.getJSONArray("locations");
				if (locationsArray.length() > 0){
					JSONObject location = locationsArray.getJSONObject(0);
					JSONObject latLng = location.getJSONObject("latLng");
					return latLng;
				}
			}
		} finally {
			connection.disconnect();
		}
		return null;
	}

}
