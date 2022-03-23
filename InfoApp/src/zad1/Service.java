/**
 * @author Siemieniec Jan S22596
 */

package zad1;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Currency;
import java.util.Locale;
import java.util.stream.Collectors;


public class Service {
    String codeOfCountry;
    String weather = "";
    String currencyTo = "";
    String city;

    public Service(String country) {
        Locale.setDefault(Locale.US);
        codeOfCountry = "";
        for (String iso : Locale.getISOCountries()) {
            Locale l = new Locale("", iso);
            if (l.getDisplayCountry().equals(country)) {
                codeOfCountry = iso;
                break;
            }
        }

    }


    public String getWeather(String cityFrom) {
        this.city = cityFrom;
        String weatherUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + cityFrom + "," + codeOfCountry + ", " + codeOfCountry + ",&appid=a90be146c17a07672bcc283883c2c7a1";
        weather = "";
        try (
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(
                                new URL(weatherUrl).openConnection().getInputStream()
                        )
                )
        ) {
            weather = bufferedReader.lines().collect(Collectors.joining());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weather;
    }

    public Double getRateFor(String curr) {
        String currencyString = Currency.getInstance(new Locale("", codeOfCountry)).getCurrencyCode();
        currencyTo=currencyString;
        if(curr.equals(currencyString))return 1.0;
        String url_str = "https://api.exchangerate.host/convert?from=" + currencyString + "&to=" + curr;
        double convertedCurrency = 0;
        try {
            URL url = new URL(url_str);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonObject jsonobj = root.getAsJsonObject();

            String req_result = jsonobj.get("result").getAsString();
            convertedCurrency = Double.parseDouble(req_result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertedCurrency;
    }

    public Double getNBPRate() {
        boolean flag = false;
        String line = "0";
        if(currencyTo.equals("PLN")) return 1.0;
        try {
            URL url = new URL("https://www.nbp.pl/kursy/kursya.html");
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                if (line.contains(currencyTo)) {
                    line = br.readLine();
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                url = new URL("https://www.nbp.pl/kursy/kursyb.html");
                con = url.openConnection();
                is = con.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    if (line.contains(currencyTo)) {
                        line = br.readLine();
                        break;
                    }
                }
            }
            line = line.substring(line.indexOf('>'));
            line = line.substring(1, line.indexOf('<'));
            line = line.replace(',', '.');
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Double.valueOf(line);
    }
}



