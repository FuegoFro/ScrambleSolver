package com.kat.is.amazing;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import com.google.inject.Inject;

public class ShowPopupClickListener implements View.OnClickListener {
    @Inject Context context;
    private String title;
    private String popupMessage;

    @Override
    public void onClick(View view) {
        showDialog(title, popupMessage, context);
    }

    public void setInfo(String title, String popupMessage) {
        this.title = title;
        this.popupMessage = popupMessage;
    }

    public static void showDialog(String title, String message, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton("Yar :3", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }
}
