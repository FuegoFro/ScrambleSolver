package com.kat.is.amazing;

import android.os.Bundle;

public class WeirdActivity extends GenericReasonActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView(
                "You find random things weird",
                "You",
                "Usually human body parts. The classic example is eyes. But, as always with you, weird is not bad. It could also be cool or strange or ugly, but most often weird a good thing.",
                SexyActivity.class,
                R.drawable.weird
        );
    }
}
