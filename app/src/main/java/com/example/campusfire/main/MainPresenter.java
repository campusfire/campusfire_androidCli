package com.example.campusfire.main;

/**
 * Handles actions from the View and updates the UI as needed.
 */
public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;

    MainPresenter(MainContract.View view){
        mView = view;
    }

    @Override
    public void handleSignInButtonClick() {
        mView.doBarcodeVerification();
    }
}
