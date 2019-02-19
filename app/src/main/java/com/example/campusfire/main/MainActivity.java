package com.example.campusfire.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.campusfire.R;
import com.example.campusfire.barcode.BarcodeCaptureActivity;
import com.example.campusfire.network.NetworkController;
import com.example.campusfire.retrofit.RetrofitActivity;
import com.google.android.gms.common.api.CommonStatusCodes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.example.campusfire.network.UrlConstants.POST_URL_AUTHPLAYER1_REQUEST_CODE;

/**
 * Displays the Main screen
 */

public class MainActivity extends AppCompatActivity implements MainContract.View, View.OnClickListener, NetworkController.ResultListener {

    private MainPresenter mPresenter;

    private Button barcodeButton;
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
        mPresenter = new MainPresenter(this);

        barcodeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.e("MainActivity","onClick() was called");
        switch (v.getId())
        {
            case R.id.barcodeButton:
                Log.e("MainActivity","case barcodeButton was triggered");
                Intent intentBarcode = new Intent(this,BarcodeCaptureActivity.class);
                startActivityForResult(intentBarcode,REQUEST_CODE);
                break;
        }
    }

    @Override
    public void onResult(int requestCode, boolean isSuccess, JSONObject jsonObject, VolleyError volleyError, ProgressDialog progressDialog) throws JSONException {
        Log.e("MainActivity", "onResult() called QrCode server check");
        String resultatAuth = null;
        String Player = null;
        if (jsonObject != null) {
            resultatAuth = jsonObject.getString("AuthStatus");
            Player = jsonObject.getString("Player");
        }
        mPresenter.handleOnResult(requestCode, isSuccess, resultatAuth, Player, volleyError, progressDialog);
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void retryBarcodeCheck(){
        Log.e("MainActivity","retryBarcodeCheck() was called");
        Intent intentBarcodeRetry = new Intent(this,BarcodeCaptureActivity.class);
        startActivityForResult(intentBarcodeRetry,REQUEST_CODE);
    }

    @Override
    public void enterParadiseTotem(String Player){
        Log.e("MainActivity","enterParadiseTotem() was called");
        Log.d(TAGPLAYER,Player);
        Intent openSecondAct = new Intent(this,RetrofitActivity.class);
        openSecondAct.putExtra("player", Player);
        startActivity(openSecondAct);
    }

    @Override
    public void toaster(String txtToast){
        Toast.makeText(this,txtToast,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == CommonStatusCodes.SUCCESS && requestCode == REQUEST_CODE){
            if(data!=null && data.hasExtra("barcode")){
                HashMap<String,String> stringParamsAuth = new HashMap<>();
                stringParamsAuth.put("barcodeSent",data.getStringExtra("barcode"));
                NetworkController.getInstance().connect(this,POST_URL_AUTHPLAYER1_REQUEST_CODE, Request.Method.POST, stringParamsAuth,this);
            }
        }
    }
}
