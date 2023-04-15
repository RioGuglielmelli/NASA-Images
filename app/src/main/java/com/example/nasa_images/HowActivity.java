package com.example.nasa_images;

import android.os.Bundle;
import android.widget.TextView;

public class HowActivity extends BaseActivity {
    TextView headerTxt,mainTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how);
        headerTxt = findViewById(R.id.header);
        mainTxt = findViewById(R.id.mainText);
    }
}