package com.kat.is.amazing;

import android.os.Bundle;

public class SillyActivity extends GenericReasonActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView(
                "You are amazingly silly",
                "Than",
                "Your laugh, your cat-like attributes, your - well everything about you is so amazing I (clearly) can't even describe it. You make me feel alive and loose (comfortable?) like no one and nothing else doesM.",
                ThereActivity.class,
                R.drawable.silly
        );
    }
}
