/**
 * @author Siemieniec Jan S22596
 */

package zad1;


import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Main {
    public static void main(String[] args) {
        Service s = new Service("Italy");
        String weatherJson = s.getWeather("Rome");
        Double rate1 = s.getRateFor("THB");
        Double rate2 = s.getNBPRate();
        // ...
        // część uruchamiająca GUI
        //new GUI();
        JFrame frame = new JFrame("TPO2");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(920, 740));
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.yellow);

        JTextArea weatherForecast = new JTextArea(weatherJson);
        weatherForecast.setLineWrap(true);
        frame.getContentPane().add(BorderLayout.NORTH, weatherForecast);

        JTextField currencyConverted = new JTextField(String.valueOf(rate1));
        frame.getContentPane().add(BorderLayout.WEST, currencyConverted);

        JTextField currencyConvertedToPLN = new JTextField(String.valueOf(rate2));
        frame.getContentPane().add(BorderLayout.EAST, currencyConvertedToPLN);

        JPanel jPanel = new JPanel();
        JFXPanel jfxPanel = new JFXPanel();
        jPanel.add(jfxPanel);
        Platform.runLater(() -> {
            WebView webView = new WebView();
            jfxPanel.setScene(new Scene(webView));
            webView.getEngine().load("https://en.wikipedia.org/wiki/" + s.city);
        });
        frame.getContentPane().add(BorderLayout.CENTER, jPanel);


        JPanel panel = new JPanel();
        JLabel label = new JLabel("Wpisz miasto");
        JTextField tf = new JTextField(10);
        JButton ok = new JButton("ok");
        ok.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                weatherForecast.setText(s.getWeather(tf.getText()));
                Platform.runLater(() -> {
                    WebView webView = new WebView();
                    jfxPanel.setScene(new Scene(webView));
                    webView.getEngine().load("https://en.wikipedia.org/wiki/" + s.city);
                });
            }
        });
        JLabel labelCountry = new JLabel("Waluta");
        JTextField tfCountry = new JTextField(10);
        JButton ok2 = new JButton("ok");
        ok2.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                s.currencyTo = tfCountry.getText();
                currencyConverted.setText(String.valueOf(s.getRateFor(s.currencyTo)));
                currencyConvertedToPLN.setText(String.valueOf(s.getNBPRate()));
            }
        });
        panel.add(label); // Components Added using Flow Layout
        panel.add(tf);
        panel.add(ok);
        panel.add(labelCountry);
        panel.add(tfCountry);
        panel.add(ok2);
        frame.getContentPane().add(BorderLayout.SOUTH, panel);

        frame.setResizable(false);
        frame.setVisible(true);
    }
}

