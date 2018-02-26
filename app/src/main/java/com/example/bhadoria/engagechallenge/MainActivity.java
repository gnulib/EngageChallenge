package com.example.bhadoria.engagechallenge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    public void onClick(View v) {
        if (v == btnMeasure)
        {
            if (name.getText() != null && name.getText().length() != 0 && deviceId.getText() != null && deviceId.getText().length() != 0)
            {
                Log.v(TAG, "got name: " + name.getText() + ", and device ID: " + deviceId.getText());
                Log.d(TAG, "initiating Measurement activity...");
                errorMsg.setVisibility(View.GONE);
                Intent intent = new Intent(this, Measurement.class);
                intent.putExtra("name", name.getText().toString());
                intent.putExtra("deviceId", deviceId.getText().toString());
                startActivity(intent);
            }
            else
            {
                errorMsg.setText("Please complete both fields");
                errorMsg.setVisibility(View.VISIBLE);
            }
        }
        return;

    }
}
