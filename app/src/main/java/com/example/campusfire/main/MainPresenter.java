package com.example.campusfire.main;

import android.app.ProgressDialog;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Handles actions from the View and updates the UI as needed.
 */
public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;

    MainPresenter(MainContract.View view){
        mView = view;
    }

    @Override
    public void handleOnResult(int requestCode, boolean isSuccess, JSONObject jsonObject, VolleyError volleyError, ProgressDialog progressDialog) throws JSONException {
        mView.actionOnResult(requestCode, isSuccess, jsonObject, volleyError, progressDialog);
    }

    @Override
    public void handleSignInButtonClick() {
        mView.doBarcodeVerification();
    }
}
