package main.java.com.atmos;

import javax.swing.*;

public class AppLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AppGUI().setVisible(true);

//                System.out.println(weatherApp.getLocationData("Mumbai"));

//                System.out.println(weatherApp.getCurrentTime());
            }
        });
    }
}