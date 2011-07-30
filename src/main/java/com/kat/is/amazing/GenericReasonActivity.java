package com.kat.is.amazing;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class GenericReasonActivity extends RoboActivity {
    @Inject ShowPopupClickListener popupClickListener;
    @Inject StartActivityClickListener startActivityClickListener;

    @InjectView(R.id.message) TextView message;
    @InjectView(R.id.next_button) ImageButton nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.generic_layout);
    }

    void setupView(String messageText, String popupTitle, String popupMessage, Class<?> nextActivity) {
        message.setText(messageText);
        popupClickListener.setInfo(popupTitle, popupMessage);
        message.setOnClickListener(popupClickListener);

        startActivityClickListener.setActivityClass(nextActivity);
        nextButton.setOnClickListener(startActivityClickListener);
    }
}
