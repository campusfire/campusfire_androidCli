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

    private MainPresenter mPresenter;
    private int requestcode;
    private boolean isSuccess;
    private JSONObject jsonObject;
    private VolleyError volleyError;
    private ProgressDialog progressDialog;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mPresenter = new MainPresenter(mView);
        // Make presenter a mock while using mock view created above
        //mPresenter = Mockito.spy(new MainPresenter(mView));
    }

    @Test
    public void test_view_ok() {
        mPresenter.handleSignInButtonClick();
        verify(mView).doBarcodeVerification();
    }

    @Test
    public void qrCode_validation(){

    }

    @Test
    public void handleOnResult() throws JSONException {
    }
}