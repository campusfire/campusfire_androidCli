package com.example.campusfire.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.campusfire.R;
import com.example.campusfire.barcode.BarcodeCaptureActivity;
import com.example.campusfire.network.NetworkController;
import com.example.campusfire.retrofit.RetrofitActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.example.campusfire.network.UrlConstants.GET_URL_REQUEST_CODE;
import static com.example.campusfire.network.UrlConstants.POST_URL_AUTHPLAYER1_REQUEST_CODE;
import static com.example.campusfire.network.UrlConstants.POST_URL_REQUEST_CODE;

/**
 * Displays the Main screen
 */

public class MainActivity extends AppCompatActivity implements MainContract.View, View.OnClickListener, NetworkController.ResultListener {

    private MainPresenter mPresenter;

    private Button barcodeButton;
    private TextView responseTv;
    private String Player;
    private String TAGPLAYER="TAGPLAYER";

    //For Gesture detection
    private static final String DEBUG_TAG = "Gestures";

    //For Barcode scanning
    private int REQUEST_CODE=100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        barcodeButton = (Button) findViewById(R.id.barcodeButton);

        barcodeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.barcodeButton:
                mPresenter.handleSignInButtonClick();
                break;
        }
    }

    @Override
    public void onResult(int requestCode, boolean isSuccess, JSONObject jsonObject, VolleyError volleyError, ProgressDialog progressDialog) throws JSONException {

        if (requestCode == POST_URL_REQUEST_CODE)
        {
            if (isSuccess)
            {
                Log.e("MainActivity", "onResult() called 2");
                responseTv.setText(jsonObject.toString());
            }
            else
            {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == GET_URL_REQUEST_CODE)
        {
            if (isSuccess)
            {
                Log.e("MainActivity", "onResult() called 1");
                responseTv.setText(jsonObject.toString());
            }
        }
        else if (requestCode == POST_URL_AUTHPLAYER1_REQUEST_CODE)
        {
            if (isSuccess)
            {
                Log.e("MainActivity", "onResult() called 3");

                String resultatAuth = jsonObject.getString("AuthStatus");

                if (resultatAuth.equals("AuthFailed")) {
                    Toast.makeText(this, "Authentication failed, try again", Toast.LENGTH_SHORT).show();
                    Intent intentBarcodeRetry = new Intent(this,BarcodeCaptureActivity.class);
                    startActivityForResult(intentBarcodeRetry,REQUEST_CODE);
                }
                else {
                    Player = jsonObject.getString("Player");
                    Log.d(Player,TAGPLAYER);
                    Toast.makeText(this, "Welcome to paradise TOTEM", Toast.LENGTH_SHORT).show();
                    Intent openSecondAct = new Intent(this, RetrofitActivity.class);
                    openSecondAct.putExtra("player", Player);
                    startActivity(openSecondAct);
                }
            }

        }

        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void doBarcodeVerification() {
        Intent intentBarcode = new Intent(this, BarcodeCaptureActivity.class);
        startActivityForResult(intentBarcode,REQUEST_CODE);
    }
}
