package main.java.com.atmos;

import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AppGUI extends JFrame {
    private JSONObject weatherData;

    Cursor hand = new Cursor(Cursor.HAND_CURSOR);

    JLabel exit;
    public AppGUI() {
        super("ATMOS - Weather App");

        setSize(450, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(loadImage("src/main/resources/Imgs/DP.png").getImage());
//        setUndecorated(true);
//        setBackground(new Color(255, 255, 255, 120));

        setLayout(null);
        setResizable(false);

        addGuiComponents();

    }

    private void addGuiComponents() {
        // Search Text Field
        JTextField searchTextField = new JTextField("Enter location...");
        searchTextField.setBounds(17, 15, 351, 45);
        searchTextField.setFont(new Font("Dialog", Font.PLAIN, 18));
        searchTextField.setForeground(Color.GRAY); // hint text color
        searchTextField.setBackground(new Color(236, 236, 236));
        searchTextField.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10)); // padding
        add(searchTextField);

        // Remove hint when clicked
        searchTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (searchTextField.getText().equals("Enter location...")) {
                    searchTextField.setText("");
                    searchTextField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchTextField.getText().isEmpty()) {
                    searchTextField.setText("Enter location...");
                    searchTextField.setForeground(Color.GRAY);
                }
            }
        });


        JLabel locationLabel = new JLabel("EARTH");
        locationLabel.setFont(new Font("Dialog", Font.BOLD, 24));
        locationLabel.setBounds(0, 85, 450, 30); // Adjust vertical position
        locationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(locationLabel);


        // Weather Image
        JLabel weatherConditionImage = new JLabel(loadImage("src/main/resources/Imgs/earth.png"));
        weatherConditionImage.setBounds(0, 125, 450, 217);
        add(weatherConditionImage);


        // Temperature text
        JLabel temperatureText = new JLabel("15℃");
        temperatureText.setFont(new Font("Dialog", Font.BOLD, 48));
        temperatureText.setBounds(0, 350, 450, 54);
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);  // position at center
        add(temperatureText);


        // Weather condition description
        JLabel weatherCondDesc = new JLabel("Earthy");
        weatherCondDesc.setFont(new Font("Dialog", Font.PLAIN, 36));
        weatherCondDesc.setBounds(0, 410, 450, 45);
        weatherCondDesc.setHorizontalAlignment(SwingConstants.CENTER);  // Center Position
        add(weatherCondDesc);

        // Humidity Image
        JLabel humidityImage = new JLabel(loadImage("src/main/resources/Imgs/humidity.png"));
        humidityImage.setBounds(20, 502, 74, 66);
        add(humidityImage);

        // Humidity Text
        JLabel humidityText = new JLabel("<html><b>Humidity</b> _% </html>");
        humidityText.setBounds(105 , 500, 85, 55);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(humidityText);

        // windSpeed image
        JLabel windSpeedImage = new JLabel(loadImage("src/main/resources/Imgs/windspeed.png"));
        windSpeedImage.setBounds(220, 499, 75, 66);
        add(windSpeedImage);

        // WindSpeed text
        JLabel windSpeedText = new JLabel("<html><b>WindSpeed</b> _km/h</html>");
        windSpeedText.setBounds(310, 500, 90, 55);
        windSpeedText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(windSpeedText);




        // Search Button
        JButton searchButton = new JButton(loadImage("src/main/resources/Imgs/search.png"));
        searchButton.setBounds(377, 15, 47, 43);
        searchButton.setBackground(new Color(236, 236, 236));
        searchButton.setCursor(hand);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get location from user
                String userInput = searchTextField.getText();

                // validate input (remove empty space to ensure empty text)
                if (userInput.replaceAll("\\s", "").length() <= 0) {
                    return;
                }

                // retrieve weather data
                weatherData = WeatherApp.getWeatherData(userInput);
                if (weatherData == null) {
                    // Show 404 image
                    weatherConditionImage.setIcon(loadImage("src/main/resources/Imgs/404.png"));

                    // Update texts to reflect error
                    temperatureText.setText("N/A");
                    weatherCondDesc.setText("Location not found");
                    humidityText.setText("<html><b>Humidity</b> N/A</html>");
                    windSpeedText.setText("<html><b>WindSpeed</b> N/A</html>");
                    locationLabel.setText(userInput.toUpperCase());

                    return;
                }


                locationLabel.setText(userInput.toUpperCase());

                // ----update gui----

                // update weather image
                String weatherCondition = (String) weatherData.get("weather_condition");

                // image change corresponding to weather
                switch (weatherCondition) {
                    case "Clear" :
                        weatherConditionImage.setIcon(loadImage("src/main/resources/Imgs/clear.png"));
                        break;
                    case "Cloudy" :
                        weatherConditionImage.setIcon(loadImage("src/main/resources/Imgs/cloudy.png"));
                        break;
                    case "Rain" :
                        weatherConditionImage.setIcon(loadImage("src/main/resources/Imgs/rain.png"));
                        break;
                    case "Snow" :
                        weatherConditionImage.setIcon(loadImage("src/main/resources/Imgs/snow.png"));
                        break;
                }

                // update Location Text
                locationLabel.setText(userInput.toUpperCase());


                // update temperature text
                double temperature = (double) weatherData.get("temperature");
                temperatureText.setText(temperature+ "℃");

                // update weather condition text
                weatherCondDesc.setText(weatherCondition);

                // update humidity text
                long humidity = (long) weatherData.get("humidity");
                humidityText.setText("<html><b>Humidity </b>"+humidity+"%</html>");

                // update wind speed
                double windSpeed = (double) weatherData.get("windSpeed");
                windSpeedText.setText("<html><b>wind Speed </b>"+windSpeed+"km/h</html>");

            }
        });
        add(searchButton);

    }

    private ImageIcon loadImage(String resourcePath) {
        try {
            BufferedImage image = ImageIO.read(new File(resourcePath));
            return new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Could not find resource");
        return null;
    }
}