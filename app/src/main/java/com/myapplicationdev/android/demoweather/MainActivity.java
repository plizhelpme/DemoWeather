package com.myapplicationdev.android.demoweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lvWeather;
    AsyncHttpClient client;
    ArrayAdapter<Weather> aaWeather;
    ArrayList<Weather> alWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvWeather = findViewById(R.id.lvWeather);
        client = new AsyncHttpClient();
        alWeather = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();



        client.get("https://api.data.gov.sg/v1/environment/2-hour-weather-forecast",
                new JsonHttpResponseHandler() {

                    String area;
                    String forecast;

                    @Override
                    public void onSuccess(int statusCode, PreferenceActivity.Header[] headers, JSONObject response) {
                        try {
                            JSONArray jsonArrItems = response.getJSONArray("items");
                            JSONObject firstObj = jsonArrItems.getJSONObject(0);
                            JSONArray jsonArrForecasts = firstObj.getJSONArray("forecasts");
                            for (int i = 0; i < jsonArrForecasts.length(); i++) {
                                JSONObject jsonObjForecast = jsonArrForecasts.getJSONObject(i);
                                area = jsonObjForecast.getString("area");
                                forecast = jsonObjForecast.getString("forecast");
                                Weather weather = new Weather(area, forecast);
                                alWeather.add(weather);
                            }
                        } catch (JSONException e) {

                        }

                        //POINT X – Code to display List View
                        aaWeather = new ArrayAdapter<Weather>(MainActivity.this, android.R.layout.simple_list_item_1, alWeather);
                        lvWeather.setAdapter(aaWeather);


                    }//end onSuccess
                });
    }//end onResume
}