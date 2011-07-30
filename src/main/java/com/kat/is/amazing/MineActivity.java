package com.kat.is.amazing;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class MineActivity extends RoboActivity {
    @Inject FinalShowPopupClickListener finalShowPopupClickListener;
    @Inject StartActivityClickListener startActivityClickListener;

    @InjectView(R.id.root) View root;
    @InjectView(R.id.home_button) Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.end_layout);

        finalShowPopupClickListener.setInfo(
                "Know",
                "HAPPY SIX MONTHS!!!"
        );
        finalShowPopupClickListener.setButton(homeButton);
        root.setOnClickListener(finalShowPopupClickListener);

        startActivityClickListener.setActivityClass(HomeActivity.class);
        homeButton.setOnClickListener(startActivityClickListener);
    }
}
