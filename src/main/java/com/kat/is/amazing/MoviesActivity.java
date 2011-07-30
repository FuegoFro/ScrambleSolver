package com.kat.is.amazing;

import android.os.Bundle;

public class MoviesActivity extends GenericReasonActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView(
                "You have excellent taste in movies",
                "I",
                "I remember at one point before we were dating but after we had started flirting talking about movies and realizing that we had similar taste for good movies like V for Vendetta, Mr. Nobody, and The Godfather.",
                NerdyActivity.class,
                R.drawable.movies
        );
    }
}
