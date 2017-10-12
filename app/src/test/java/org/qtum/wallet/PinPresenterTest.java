package org.qtum.wallet;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.ui.fragment.pin_fragment.PinFragment;
import org.qtum.wallet.ui.fragment.pin_fragment.PinInteractor;
import org.qtum.wallet.ui.fragment.pin_fragment.PinPresenterImpl;
import org.qtum.wallet.ui.fragment.pin_fragment.PinView;
import org.qtum.wallet.utils.FingerprintUtils;

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PinPresenterTest {

    @Mock
    PinView view;
    @Mock
    PinInteractor interactor;
    PinPresenterImpl presenter;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });
        RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
            @Override
            public Scheduler getIOScheduler() {
                return Schedulers.immediate();
            }
        });

        presenter = new PinPresenterImpl(view,interactor);
    }

    @Test
    public void initialize_view(){
        when(view.checkTouchIdEnable()).thenReturn(true);
        when(view.isSensorStateAt((FingerprintUtils.mSensorState)any())).thenReturn(true);
        presenter.setAction(PinFragment.AUTHENTICATION);
        presenter.initializeViews();
        verify(view,times(1)).setToolBarTitle(anyInt());
    }



    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().reset();
    }

}