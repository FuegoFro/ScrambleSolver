package com.kat.is.amazing;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class HomeActivity extends RoboActivity {
    @Inject StartActivityClickListener listener;

    @InjectView(R.id.message) TextView message;
    @InjectView(R.id.next_button) ImageButton nextButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_layout);

        message.setText("Reasons\nWhy\nKat\nIs\nAmazing\n(Abridged)");
        listener.setActivityClass(MoviesActivity.class);
        nextButton.setOnClickListener(listener);
    }
}
