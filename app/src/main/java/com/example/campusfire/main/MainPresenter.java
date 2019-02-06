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
        if (isSuccess) {
            String resultatAuth = jsonObject.getString("AuthStatus");
            mView.toaster(resultatAuth);
            if (resultatAuth.equals("AuthFailed")){
                mView.toaster("Authentication failed, try again");
                mView.retryBarcodeCheck();
            }
            else {
                String Player = jsonObject.getString("Player");
                mView.toaster("Welcome to paradise TOTEM");
                mView.enterParadiseTotem(Player);
            }
        }
        else
        {
            mView.toaster("Something went wrong");
        }
    }

    @Override
    public void handleSignInButtonClick() {
        mView.doBarcodeVerification();
    }
}
