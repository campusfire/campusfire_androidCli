package com.example.campusfire.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.campusfire.R;
import com.example.campusfire.barcode.BarcodeCaptureActivity;

import java.util.HashMap;

/**
 * Displays the Main screen
 */

public class MainActivity extends AppCompatActivity implements MainContract.View{

    private MainPresenter mPresenter;

    private Button barcodeButton;

    String Player;
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
    public void showSignInScreen() {
        Toast.makeText(this, "Taking user to the sign in screen", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void doBarcodeVerification() {
        Intent intentBarcode = new Intent(this, BarcodeCaptureActivity.class);
        startActivityForResult(intentBarcode,REQUEST_CODE);
    }
}
