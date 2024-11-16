package com.example.lab_5;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Parser {

    public static Map<String, String> parseJson(String jsonData) {
        Map<String, String> currencyRates = new HashMap<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject rates = jsonObject.getJSONObject("rates");

            Iterator<String> keys = rates.keys();
            while (keys.hasNext()) {
                String currency = keys.next();
                String value = rates.getString(currency);
                currencyRates.put(currency, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return currencyRates;
    }
}
