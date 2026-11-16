package com.stylesync.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Service for fetching weather information
 */
public class WeatherService {
    private static final String API_KEY = ConfigUtil.getProperty("weather.api.key");
    private static final String API_URL = ConfigUtil.getProperty("weather.api.url");
    private final OkHttpClient client;

    public WeatherService() {
        this.client = new OkHttpClient();
    }

    public WeatherInfo getWeather(String city) {
        if (API_KEY == null || API_KEY.equals("YOUR_API_KEY_HERE")) {
            return createDemoWeather(city);
        }

        try {
            String url = API_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric";
            Request request = new Request.Builder().url(url).build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String jsonData = response.body().string();
                    return parseWeatherData(jsonData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return createDemoWeather(city);
    }

    private WeatherInfo parseWeatherData(String jsonData) {
        JsonObject json = JsonParser.parseString(jsonData).getAsJsonObject();
        
        String description = json.getAsJsonArray("weather")
                .get(0).getAsJsonObject()
                .get("description").getAsString();
        
        double temp = json.getAsJsonObject("main")
                .get("temp").getAsDouble();

        return new WeatherInfo(description, temp);
    }

    private WeatherInfo createDemoWeather(String city) {
        // Return demo weather data when API key is not configured
        return new WeatherInfo("partly cloudy", 22.0);
    }

    public static class WeatherInfo {
        private final String description;
        private final double temperature;

        public WeatherInfo(String description, double temperature) {
            this.description = description;
            this.temperature = temperature;
        }

        public String getDescription() {
            return description;
        }

        public double getTemperature() {
            return temperature;
        }

        public String getTemperatureString() {
            return String.format("%.1f°C", temperature);
        }

        @Override
        public String toString() {
            return description + ", " + getTemperatureString();
        }
    }
}
