package com.example.bhadoria.engagechallenge;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Measurement extends AppCompatActivity {
    private ProgressBar measuring;
    private TextView measuringMsg;
    private TextView measurement;
    private static final String TAG = "MeasurementActivity";
    private String name;
    private String deviceId;
    private class ReadMeasurements extends AsyncTask<Void, Integer, Integer> {
        private String name;
        private String deviceId;
        public ReadMeasurements(String name, String deviceId) {
            this.name = name;
            this.deviceId = deviceId;
        }
        @Override
        protected void onPreExecute() {
            measuringMsg.setText("Reading measurements...");
            measuringMsg.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate (Integer... values) {
            measuring.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute (Integer reading) {
            measuringMsg.setVisibility(View.GONE);
            measuring.setVisibility(View.GONE);
            measurement.setText("Weight: " + reading + " Lbs!");
            measurement.setVisibility(View.VISIBLE);

        }
        @Override
        protected Integer doInBackground(Void... params) {
            // simulate reading by waiting for 5 seconds
            Log.d(TAG, "starting simulation to fetch weight");
            int max = 100;
            measuring.setMax(max);
            for (int i = 0; i < max; i++) {
                Log.d(TAG, "" + (i + 1));
                try {
                    publishProgress(new Integer[]{i+1});
                    Thread.sleep((5 * 1000 / max));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.d(TAG, "end simulation to fetch measurement");
            return 160;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d(TAG, "activity initializing...");
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
