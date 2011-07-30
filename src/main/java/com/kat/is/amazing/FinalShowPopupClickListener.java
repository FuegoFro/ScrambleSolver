package com.kat.is.amazing;

import android.view.View;

public class FinalShowPopupClickListener extends ShowPopupClickListener {
    private View button;

    @Override
    public void onClick(View view) {
        super.onClick(view);
        button.setVisibility(View.VISIBLE);
    }

    public void setButton(View button) {
        this.button = button;
    }
}
