package com.kat.is.amazing;

import android.os.Bundle;

public class ThereActivity extends GenericReasonActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView(
                "You are always there for me when I need you",
                "You'll",
                "Through the months of February and March, even when we had just started dating, you listened to a lot of shit from me. And you still continue to be there for me, the only person can confide in and the only person I've ever truly trusted.",
                LoveActivity.class,
                R.drawable.there
        );
    }
}
