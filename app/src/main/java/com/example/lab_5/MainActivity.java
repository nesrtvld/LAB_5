package com.example.lab_5;

import android.os.Bundle;
import android.util.Log; // Добавлено для логирования
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements DataLoader.DataLoaderCallback {

    private static final String TAG = "MainActivity";

    private EditText editTextFilter;
    private ListView listViewCurrencies;
    private ProgressBar progressBar;
    private ArrayAdapter<String> adapter;
    private List<String> currencyList = new ArrayList<>();
    private List<String> filteredList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextFilter = findViewById(R.id.editTextFilter);
        listViewCurrencies = findViewById(R.id.listViewCurrencies);
        progressBar = findViewById(R.id.progressBar);


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filteredList);
        listViewCurrencies.setAdapter(adapter);


        editTextFilter.addTextChangedListener(new TextWatcher() {
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
        Log.d(TAG, "Starting data load...");
        progressBar.setVisibility(ProgressBar.VISIBLE);
        String apiUrl = "https://open.er-api.com/v6/latest/USD\n";
        new DataLoader(this).execute(apiUrl);
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

    @Override
    public void onDataLoaded(String data) {
        Log.d(TAG, "Data loaded: " + data);
        progressBar.setVisibility(ProgressBar.GONE);


        Map<String, String> parsedData = Parser.parseJson(data);

        if (parsedData.isEmpty()) {
            Log.e(TAG, "Parsed data is empty!");
            Toast.makeText(this, "No data available", Toast.LENGTH_LONG).show();
            return;
        }


        currencyList.clear();
        for (Map.Entry<String, String> entry : parsedData.entrySet()) {
            currencyList.add(entry.getKey() + " - " + entry.getValue());
        }


        filteredList.clear();
        filteredList.addAll(currencyList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(Exception e) {
        progressBar.setVisibility(ProgressBar.GONE);
        Log.e(TAG, "Error loading data", e);
        Toast.makeText(this, "Error loading data: " + e.getMessage(), Toast.LENGTH_LONG).show();
    }
}
