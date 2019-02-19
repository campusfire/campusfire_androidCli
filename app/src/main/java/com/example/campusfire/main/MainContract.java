package com.example.campusfire.main;

import android.app.ProgressDialog;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Defines the contract between the View {@link MainActivity} and the Presenter
 * {@link MainPresenter}
 */

public interface MainContract {
    interface View {
        void toaster(String txtToast);
        void retryBarcodeCheck();
        void enterParadiseTotem(String Player);
    }
    interface Presenter {
        void handleOnResult(int requestCode, boolean isSuccess, String resultatAuth, String Player, VolleyError volleyError, ProgressDialog progressDialog) throws JSONException;
    }
}
