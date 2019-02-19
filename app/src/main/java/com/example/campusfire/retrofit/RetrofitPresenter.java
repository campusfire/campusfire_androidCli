package com.example.campusfire.retrofit;

import java.util.HashMap;

import static com.example.campusfire.network.UrlConstants.POST_URL_LOGOUT_CODE;
import static com.example.campusfire.network.UrlConstants.POST_URL_TEXT_REQUEST_CODE;

/**
 * Handles actions from the View and updates the UI as needed.
 */

public class RetrofitPresenter implements RetrofitContract.Presenter{

    private RetrofitContract.View rView;

    // tags
    private String TAGDL = "DownloadImage";
    private String TAGUPDTXT = "UploadText";
    private static final String DEBUG_TAG = "Gestures";

    RetrofitPresenter(RetrofitContract.View view) {
        rView = view;
    }

    @Override
    public void getAccelerometer(float[] values, float gravity, long actualTime) {
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z)
                / (gravity * gravity);
        if (accelationSquareRoot >= 6) //
        {
            rView.toaster("Acceleration detected");
        }
    }

    @Override
    public void uploadText(String sentText){
        rView.logger("v",TAGUPDTXT,"UPDTXT button clicked");
        if (!sentText.equals("")){
            rView.logger("v",TAGUPDTXT,"entered in If");
            HashMap<String, String> stringParams = new HashMap<>();
            stringParams.put("sentText", sentText);
            rView.logger("v",TAGUPDTXT,"text to send : " + sentText);
            rView.network_connect(POST_URL_TEXT_REQUEST_CODE,stringParams);
        }
    }

    @Override
    public void logout(String Player) {
        HashMap<String, String> stringParamsLogout = new HashMap<>();
        stringParamsLogout.put("player", Player);
        rView.network_connect(POST_URL_LOGOUT_CODE, stringParamsLogout);
    }
}
