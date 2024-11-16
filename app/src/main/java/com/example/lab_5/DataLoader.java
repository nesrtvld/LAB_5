package com.example.lab_5;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataLoader extends AsyncTask<String, Void, String> {

    private DataLoaderCallback callback;

    public interface DataLoaderCallback {
        void onDataLoaded(String data);
        void onError(Exception e);
    }

    public DataLoader(DataLoaderCallback callback) {
        this.callback = callback;
    }

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
            if (callback != null) {
                callback.onError(e);
            }
            return null;
        }
        return result.toString();
    }

    @Override
    protected void onPostExecute(String data) {
        if (callback != null) {
            if (data != null) {
                callback.onDataLoaded(data);
            } else {
                callback.onError(new Exception("Failed to load data"));
            }
        }
    }
}
