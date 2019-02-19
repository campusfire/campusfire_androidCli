package com.example.campusfire.retrofit;

import java.util.HashMap;

/**
 * Defines the contract between the View {@link RetrofitActivity} and the Presenter
 * {@link RetrofitPresenter}
 */

public interface RetrofitContract {
    interface View {
        void toaster(String txtToast);
        void logger(String type, String tag, String logMessage);
        void network_connect(int requestCode, HashMap<String, String> params);
    }

    interface Presenter{
        void getAccelerometer(float[] values, float gravity, long actualTime);
        void logout(String Player);
        void uploadText(String sentText);
    }
}
