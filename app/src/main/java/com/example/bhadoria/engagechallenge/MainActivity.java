package com.example.bhadoria.engagechallenge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnMeasure;
    private EditText name, deviceId;
    private TextView errorMsg;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.btnMeasure = (Button) findViewById(R.id.measure);
        this.btnMeasure.setOnClickListener(this);
        this.name = (EditText) findViewById(R.id.name);
        this.deviceId = (EditText) findViewById(R.id.device_id);
        this.errorMsg = (TextView) findViewById(R.id.error_msg);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d(TAG, "parsing scan result...");
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        String device = getString(R.string.device_id);
        if (scanResult != null) {
            // handle scan result
            device = scanResult.getContents();
            Log.d(TAG, "got valid scan result: '" + device + "'");
        } else {
            Log.d(TAG, "did not get valid scan result");
        }
        Intent newIntent = new Intent(this, Measurement.class);
        newIntent.putExtra("name", name.getText().toString());
        newIntent.putExtra("deviceId", device);
        startActivity(newIntent);
    }
    @Override
    public void onClick(View v) {
        if (v == btnMeasure)
        {
            if (name.getText() != null && name.getText().length() != 0)
            {
                Log.v(TAG, "got name: " + name.getText());
                Log.d(TAG, "requesting scan intent...");
                errorMsg.setVisibility(View.GONE);
                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.initiateScan();
            }
            else
            {
                errorMsg.setText("Please provide name!!!");
                errorMsg.setVisibility(View.VISIBLE);
            }
        }
        return;

    }
}
