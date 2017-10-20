package org.qtum.wallet;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.ui.activity.splash_activity.SplashActivityInteractor;
import org.qtum.wallet.ui.activity.splash_activity.SplashActivityPresenter;
import org.qtum.wallet.ui.activity.splash_activity.SplashActivityPresenterImpl;
import org.qtum.wallet.ui.activity.splash_activity.SplashActivityView;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class SplashActivityPresenterTest {

    @Mock
    SplashActivityView view;
    @Mock
    SplashActivityInteractor interactor;
    SplashActivityPresenter presenter;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        presenter = new SplashActivityPresenterImpl(view,interactor);
    }

    @Test
    public void initializeViews_test(){
        presenter.initializeViews();

        verify(interactor, times(1)).migrateDefaultContracts();
        verify(view, times(1)).startApp();
    }

}
