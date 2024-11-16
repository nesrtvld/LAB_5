package com.example.lab_5;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCurrencies;
    private CurrencyAdapter adapter;
    private List<String> currencyList = new ArrayList<>();
    private List<String> filteredList = new ArrayList<>();

    private static final String API_URL = "https://open.er-api.com/v6/latest/USD";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerViewCurrencies = findViewById(R.id.recyclerViewCurrencies);
        recyclerViewCurrencies.setLayoutManager(new LinearLayoutManager(this));


        adapter = new CurrencyAdapter(filteredList);
        recyclerViewCurrencies.setAdapter(adapter);


        EditText editText = findViewById(R.id.editTextFilter);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCurrencies(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        loadCurrencyData();
    }

    private void loadCurrencyData() {
        new CurrencyDataLoader().execute(API_URL);
    }

    private void filterCurrencies(String query) {
        filteredList.clear();
        for (String currency : currencyList) {
            if (currency.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(currency);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private class CurrencyDataLoader extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
            } catch (Exception e) {
                Log.e(TAG, "Error loading data", e);
                return null;
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String data) {
            if (data == null) {
                Toast.makeText(MainActivity.this, "Failed to load data", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONObject rates = jsonObject.getJSONObject("rates");


                currencyList.clear();
                Iterator<String> keys = rates.keys();
                while (keys.hasNext()) {
                    String currency = keys.next();
                    String value = rates.getString(currency);
                    currencyList.add(currency + " - " + value);
                }


                filterCurrencies("");

            } catch (Exception e) {
                Log.e(TAG, "Error parsing data", e);
                Toast.makeText(MainActivity.this, "Error parsing data", Toast.LENGTH_LONG).show();
            }
        }
    }
}
