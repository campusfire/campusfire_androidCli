package com.example.campusfire.main;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
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

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mPresenter = new MainPresenter(mView);
    }

    @Test
    public void handleSignInButtonClick() {
        mPresenter.handleSignInButtonClick();
        verify(mView).showSignInScreen();
    }
}