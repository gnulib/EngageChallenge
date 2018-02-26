package com.example.bhadoria.engagechallenge;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class Measurement extends AppCompatActivity {
    private ProgressBar measuring;
    private TextView measuringMsg;
    private TextView measurement;
    private static final String TAG = "MeasurementActivity";
    private String name;
    private String deviceId;
    private String url;
    private class ReadMeasurements extends AsyncTask<Void, Integer, ReadingResp> {
        private String name;
        private String deviceId;
        private RestTemplate restTemplate;
        private ReadingResp[] readingResps;
        public ReadMeasurements(String name, String deviceId) {
            this.name = name;
            this.deviceId = deviceId;
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        }
        @Override
        protected void onPreExecute() {
            measuringMsg.setText("ReadingResp measurements...");
            measuringMsg.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate (Integer... values) {
            measuringMsg.setText(readingResps[values[0]-1].getWeight().toString());
            measuring.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute (ReadingResp readingResp) {
            measuringMsg.setVisibility(View.GONE);
            measuring.setVisibility(View.GONE);
            measurement.setText("Weight: " + readingResp.getWeight() + " Lbs!");
            measurement.setVisibility(View.VISIBLE);

        }
        @Override
        protected ReadingResp doInBackground(Void... params) {
            // simulate reading by waiting for 5 seconds
            Log.d(TAG, "starting simulation to fetch weight");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // we'll make 10 readingResps
            int max = 10;
            readingResps = new ReadingResp[max];
            measuring.setMax(max);
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            ReadingReq req = new ReadingReq();
            req.setDeviceId(deviceId);
            req.setUsername(name);
            HttpEntity<ReadingReq> entity = new HttpEntity<ReadingReq>(req, headers);
            for (int i = 0; i < max; i++) {
                Log.d(TAG, "" + (i + 1));
                try {

                    readingResps[i] = restTemplate.exchange(url, HttpMethod.PUT, entity, ReadingResp.class).getBody();
                    publishProgress(new Integer[]{i+1});
                    Thread.sleep((5 * 1000 / max));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (RestClientException e) {
                    e.printStackTrace();
                }
            }
            Log.d(TAG, "end simulation to fetch measurement");
            return readingResps[max-1];
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
