// retrieve weather data from API.
// this backed logic will fetch latest weather data from external API and return it;
// then GUI will display this data to user

package main.java.com.atmos;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class WeatherApp {
    // fetch weather data for given location
    public static JSONObject getWeatherData(String locationName) {
        // get location co-ordinates using geolocation API
        JSONArray locationData = getLocationData(locationName);

        // check if location data is empty or null
        if (locationData == null || locationData.isEmpty()) {
            System.out.println("Error: Location data put wrong --> "+locationName);
            return null;
        }

        // Extract latitude and longitude data
        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        if (latitude == 0.0 && longitude == 0.0) {
            System.out.println("Invalid coordinates returned.");
            return null;
        }

        // build API request URL with location coordinates
        String urlStr = "https://api.open-meteo.com/v1/forecast?"+
                "latitude=" + latitude + "&longitude="+ longitude +
                "&hourly=temperature_2m,weather_code,relative_humidity_2m,wind_speed_10m&timezone=Asia%2FSingapore";

        try {
            // Call API and get response
            HttpsURLConnection conn = fetchApiResponse(urlStr);

            // Check for response status
            // 200 for successful connection
            if (conn.getResponseCode() != 200) {
                System.out.println("Error : Could not connect to API");
                return null;
            }

            StringBuilder resultJson = new StringBuilder();
            Scanner sc = new Scanner(conn.getInputStream());

            while (sc.hasNext()) {
                //read and store into the stringBuilder
                resultJson.append(sc.nextLine());
            }

            sc.close();     // close scanner
            conn.disconnect();  // close url connection

            // parse through our data
            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

            // retrieve hourly data
            JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");

            // want to get current hour data
            // so we need to get index of current hour
            JSONArray time = (JSONArray) hourly.get("time");
            int index = findIndexOfCurrentTime(time);

            // get temperature
            JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
            double temperature = (double) temperatureData.get(index);

            // get weather code
            JSONArray weathercode = (JSONArray) hourly.get("weather_code");
            String weatherCondition = convertWeatherCode((long) weathercode.get(index));

            // get humidity
            JSONArray relativeHumidity = (JSONArray) hourly.get("relative_humidity_2m");
            long humidity = (long) relativeHumidity.get(index);

            // get wind speed
            JSONArray windSpeedData = (JSONArray) hourly.get("wind_speed_10m");
            double windspeed = (double) windSpeedData.get(index);

            // build the weather JSON data object that will access in frontend
            JSONObject weatherData = new JSONObject();
            weatherData.put("temperature", temperature);
            weatherData.put("weather_condition", weatherCondition);
            weatherData.put("humidity", humidity);
            weatherData.put("windSpeed", windspeed);

            return weatherData;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // retrieves geographic coordinates for given location name
    public static JSONArray getLocationData(String locationName) {
        // replace location name "space" in "+" to adhere API request
        locationName = locationName.replaceAll(" ", "+");

        // Build API url with location parameter
        String urlStr = "https://geocoding-api.open-meteo.com/v1/search?name="+locationName+"&count=10&language=en&format=json";

        try { // for cal API & get a response
            HttpsURLConnection conn = fetchApiResponse(urlStr);

            // check response status
            // 200 for successful connection
            if (conn.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to API");
                return null;
            } else {
                // Store the API result
                StringBuilder resultJSON = new StringBuilder();
                Scanner sc = new Scanner(conn.getInputStream());

                // read and store the resulting json data into our string builder
                while (sc.hasNext()) {
                    resultJSON.append(sc.nextLine());
                }

                sc.close(); // close scanner
                conn.disconnect(); // close url connection

                // parse the JSON string into a json obj
                JSONParser parser = new JSONParser();
                JSONObject resultsJSONObj = (JSONObject) parser.parse(String.valueOf(resultJSON));

                // get the list of Location data the API generated from the Location name
                JSONArray locationData = (JSONArray) resultsJSONObj.get("results");
                return locationData;
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        // if location not found
        return null;
    }

    private static HttpsURLConnection fetchApiResponse(String urlStr) {
        try {   //  try to create connection
            URL url = new URL(urlStr);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            // set request method to get
            conn.setRequestMethod("GET");

            // connection to API
            conn.connect();
            return conn;
        } catch (IOException e) {
            e.printStackTrace();
        }

        // unnable to make connection
        return null;
    }

    private static int findIndexOfCurrentTime(JSONArray timeList) {
        String currentTime = getCurrentTime();

        // iterate through the time list and see which one matches our current time
        for (int i=0; i<timeList.size(); i++) {
            String time = (String) timeList.get(i);
            if (time.equalsIgnoreCase(currentTime)) {
                // return the index
                return i;
            }
        }

        return 0;
    }

    public static String getCurrentTime() {
        // get current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // formate data time (yyyy-mm-dd)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");

        // formate and print the current date and time
        String formatedDateTime = currentDateTime.format(formatter);

        return formatedDateTime;

    }

    // convert weather code to something more readable (WMO Weather interpretation Codes (WW))
    private static String convertWeatherCode(long weathercode) {
        String weatherCondition = "";

        if (weathercode == 0L) {    // 0
            weatherCondition = "Clear";
        } else if (weathercode > 0L && weathercode <= 3L) { // 1, 2, 3
            weatherCondition = "Cloudy";
        } else if ((weathercode >= 51L && weathercode <= 67L) ||
                (weathercode >= 80L && weathercode <= 99L)) {
            weatherCondition = "Rain";
        } else if (weathercode >= 71L && weathercode <= 77L) {
            weatherCondition = "Snow";
        }

        return weatherCondition;
    }
}









