package com.example.campusfire.main;

import android.app.ProgressDialog;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Local unit tests for the Main Presenter
 */

public class MainPresenterTest {

    @Mock
    private MainContract.View mView;

    @Mock
    private JSONObject jsonObject;

    private MainPresenter mPresenter;
    private int requestcode;
    private boolean isSuccess;
    private VolleyError volleyError;
    private ProgressDialog progressDialog;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mPresenter = new MainPresenter(mView);
    }

    @Test
    public void failure_server_connect() throws JSONException {
        isSuccess = false;
        mPresenter.handleOnResult(requestcode, isSuccess, jsonObject, volleyError, progressDialog);
        verify(mView).toaster("Something went wrong");
    }

    @Test
    public void auth_failed() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        isSuccess = true;
        jsonObject.put("AuthStatus","AuthFailed");
        System.out.println(jsonObject.toString());
        mPresenter.handleOnResult(requestcode, isSuccess, jsonObject, volleyError, progressDialog);
        String resultatAuth = jsonObject.getString("AuthStatus");
        verify(mView).toaster(resultatAuth);
        verify(mView).toaster("Authentication failed, try agai");
        verify(mView).retryBarcodeCheck();
    }
}