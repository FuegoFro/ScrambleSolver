package com.kat.is.amazing;

import android.os.Bundle;

public class NerdyActivity extends GenericReasonActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView(
                "You are wonderfully nerdy",
                "Love",
                "Nerdy Story",
                WeirdActivity.class,
                R.drawable.nerdy
        );
    }
}
