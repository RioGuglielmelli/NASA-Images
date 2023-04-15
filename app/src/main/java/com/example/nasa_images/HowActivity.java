package com.example.nasa_images;

import android.os.Bundle;
import android.widget.TextView;
/**

 The HowActivity displays information on how to use the application.
 It sets up the layout and initializes the header and main text fields.
 */
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