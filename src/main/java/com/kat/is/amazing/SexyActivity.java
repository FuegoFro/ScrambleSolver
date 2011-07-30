package com.kat.is.amazing;

import android.os.Bundle;

public class SexyActivity extends GenericReasonActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView(
                "You are sexy as hell",
                "More",
                "(kekeke) I still vividly remember the first time I slept over, the night Quyen was gone. It was so exhilarating and we were exploring each other and it was wonderful. And without a doubt, it is still at least as wonderful now.",
                SillyActivity.class,
                R.drawable.sexy
        );
    }
}
