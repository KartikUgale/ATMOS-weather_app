# 🌤️ ATMOS (Weather App)

A sleek desktop weather application built with Java Swing that fetches real-time weather data using the [Open-Meteo API](https://open-meteo.com/). This app provides temperature, weather conditions, humidity, and wind speed based on user-inputted location.

<div align="center">
  <img src="https://github.com/KartikUgale/ATMOS-weather_app/blob/main/src/main/resources/SC/sc_earth.png?raw=true" width="300">
  <img src="https://github.com/KartikUgale/ATMOS-weather_app/blob/main/src/main/resources/SC/sc_ind.png?raw=true" width="300">
  <img src="https://github.com/KartikUgale/ATMOS-weather_app/blob/main/src/main/resources/SC/sc_NA.png?raw=true" width="300">
</div>

---

## 🚀 Features

- 🔍 Search for any city worldwide
- 🌡️ Displays current temperature, weather condition, humidity, and wind speed
- 🎨 Modern and clean UI with custom weather icons
- ❌ Graceful error handling for invalid or unknown locations
- 🔄 Dynamically updates images based on weather (sun, cloud, rain, snow)
- 📦 Fully packaged as a runnable JAR

---

## 🛠️ Technologies Used

- Java
- Swing
- JSON.simple for JSON parsing
- Open-Meteo Weather and Geocoding APIs
- IntelliJ IDEA (used for development and JAR packaging)

---

## ⚙️ How to Run

### Option 1: Using IntelliJ IDEA

1. Clone the repository
2. Open the project in IntelliJ IDEA
3. Run `AppGUI.java` from the IDE
4. Or create an artifact:
   - Go to `File > Project Structure > Artifacts`
   - Select `Extract to the target JAR` option
   - Include resources like images in the output directory
   - Build the artifact and run the JAR

### Option 2: Run Anywhere via JAR

1. Make sure JDK is installed
2. Navigate to the JAR file directory
3. Run using:

```bash
java -jar WeatherApp.jar            
```
