package com.kat.is.amazing;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class HomeActivity extends RoboActivity {
    @InjectView(R.id.message) TextView message;
    @InjectView(R.id.next_button) ImageButton nextButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_layout);

        message.setText("Reasons\nWhy\nKat\nIs\nAmazing");
        nextButton.setOnClickListener(new StartActivityClickListener());
    }
}
