package com.example.bhadoria.engagechallenge;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class Measurement extends AppCompatActivity {
    private ProgressBar measuring;
    private TextView measuringMsg;
    private TextView measurement;
    private static final String TAG = "MeasurementActivity";
    private String name;
    private String deviceId;
    private String url;
    private class ReadMeasurements extends AsyncTask<Void, Integer, Reading> {
        private String name;
        private String deviceId;
        private RestTemplate restTemplate;
        private Reading[] readings;
        public ReadMeasurements(String name, String deviceId) {
            this.name = name;
            this.deviceId = deviceId;
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        }
        @Override
        protected void onPreExecute() {
            measuringMsg.setText("Reading measurements...");
            measuringMsg.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate (Integer... values) {
            measuringMsg.setText(readings[values[0]-1].getMeasurement());
            measuring.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute (Reading reading) {
            measuringMsg.setVisibility(View.GONE);
            measuring.setVisibility(View.GONE);
            measurement.setText("Weight: " + reading.getMeasurement() + " Lbs!");
            measurement.setVisibility(View.VISIBLE);

        }
        @Override
        protected Reading doInBackground(Void... params) {
            // simulate reading by waiting for 5 seconds
            Log.d(TAG, "starting simulation to fetch weight");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // we'll make 10 readings
            int max = 10;
            readings = new Reading[max];
            measuring.setMax(max);
            for (int i = 0; i < max; i++) {
                Log.d(TAG, "" + (i + 1));
                try {
                    readings[i] = restTemplate.getForObject(url, Reading.class);
                    publishProgress(new Integer[]{i+1});
                    Thread.sleep((5 * 1000 / max));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (RestClientException e) {
                    e.printStackTrace();
                }
            }
            Log.d(TAG, "end simulation to fetch measurement");
            return readings[max-1];
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        Log.d(TAG, "activity initializing...");
        url = getString(R.string.measurement_url);
        name = getIntent().getStringExtra("name");
        deviceId = getIntent().getStringExtra("deviceId");
        Log.v(TAG, "got name: " + name + ", and device ID: " + deviceId);
        measuring = (ProgressBar) findViewById(R.id.measuring);
        measuringMsg = (TextView) findViewById(R.id.measuring_msg);
        measurement = (TextView) findViewById(R.id.measurement);
        Log.d(TAG, "initialization complete");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "activity paused...");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "activity stopped...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "activity resumed...");
        new ReadMeasurements(name, deviceId).execute();
    }
}
