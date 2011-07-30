package com.kat.is.amazing;

import android.os.Bundle;
import android.widget.ImageButton;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class HomeActivity extends RoboActivity {
    @Inject StartActivityClickListener listener;

    @InjectView(R.id.next_button) ImageButton nextButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_layout);

        listener.setActivityClass(MoviesActivity.class);
        nextButton.setOnClickListener(listener);
    }
}
