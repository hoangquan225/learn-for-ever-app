package com.example.mylap.utils;
import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogUtils {
    public static ProgressDialog createProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        return progressDialog;
    }
}
