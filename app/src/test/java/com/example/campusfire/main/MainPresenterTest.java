package com.example.campusfire.main;

import android.app.ProgressDialog;

import com.android.volley.VolleyError;

import org.json.JSONException;
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

    private MainPresenter mPresenter;
    private int requestcode;
    private boolean isSuccess;
    private String resultatAuth;
    private String Player;
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
        mPresenter.handleOnResult(requestcode, isSuccess, resultatAuth, Player, volleyError, progressDialog);
        verify(mView).toaster("Network Connection went wrong");
    }

    @Test
    public void auth_failed() throws JSONException {
        isSuccess = true;
        resultatAuth = "AuthFailed";
        mPresenter.handleOnResult(requestcode, isSuccess, resultatAuth, Player, volleyError, progressDialog);
        verify(mView).toaster(resultatAuth);
        verify(mView).toaster("Authentication failed, try again");
        verify(mView).retryBarcodeCheck();
    }

    @Test
    public void auth_ok() throws JSONException {
        isSuccess = true;
        resultatAuth = "AuthOk";
        Player = "Player 1";
        mPresenter.handleOnResult(requestcode, isSuccess, resultatAuth, Player, volleyError, progressDialog);
        verify(mView).toaster("Welcome to paradise TOTEM");
        verify(mView).enterParadiseTotem(Player);
    }
}