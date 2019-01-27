package com.example.campusfire.main;

/**
 * Defines the contract between the View {@link MainActivity} and the Presenter
 * {@link MainPresenter}
 */

public interface MainContract {
    interface View {
        void doBarcodeVerification();
    }
    interface Presenter {
        void handleSignInButtonClick();
    }
}
