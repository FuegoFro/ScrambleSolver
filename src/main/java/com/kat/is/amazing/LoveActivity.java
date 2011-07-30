package com.kat.is.amazing;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class LoveActivity extends RoboActivity {
    @Inject ShowPopupClickListener popupClickListener;
    @Inject StartActivityClickListener startActivityClickListener;

    @InjectView(R.id.message) TextView message;
    @InjectView(R.id.next_button) ImageButton nextButton;
    @InjectView(R.id.how_much_button) Button howMuchButton;

    private String initialMessage = "You love me. ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.love_layout);

        message.setText(initialMessage);

        popupClickListener.setInfo(
                "Ever",
                "Only that much?"
        );

        startActivityClickListener.setActivityClass(MineActivity.class);
        nextButton.setOnClickListener(startActivityClickListener);

        howMuchButton.setOnClickListener(new SoMuchClickListener());
    }

    private class SoMuchClickListener implements View.OnClickListener {
        private String so = "So ";
        private int timesMore = 0;

        @Override
        public void onClick(View view) {
            message.setText(initialMessage + " " + so + "much!!!");
            so += "so ";
            timesMore++;
            if (timesMore == 20) {
                ShowPopupClickListener.showDialog(
                        "<3 ^ ∞",
                        "\"Insert nonsense infinity argument here :)\"",
                        LoveActivity.this
                );
            }
        }
    }
}
