package org.qtum.wallet;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.datastorage.listeners.LanguageChangeListener;
import org.qtum.wallet.model.gson.DGPInfo;
import org.qtum.wallet.model.gson.FeePerKb;
import org.qtum.wallet.ui.activity.main_activity.MainActivityInteractor;
import org.qtum.wallet.ui.activity.main_activity.MainActivityPresenterImpl;
import org.qtum.wallet.ui.activity.main_activity.MainActivityView;
import org.qtum.wallet.ui.fragment.pin_fragment.PinAction;

import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.qtum.wallet.ui.fragment.pin_fragment.PinAction.CHECK_AUTHENTICATION;

public class MainActivityPresenterTest {

    @Mock
    MainActivityView view;
    @Mock
    MainActivityInteractor interactor;
    MainActivityPresenterImpl presenter;

    @Before
    public void setup() {
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

        presenter = new MainActivityPresenterImpl(view, interactor);
    }

    @Test
    public void initializeView_test() {
        presenter.initializeViews();

        verify(interactor, times(1)).addLanguageChangeListener((LanguageChangeListener) any());
    }

    @Test
    public void openStartFragment_keyNotGenerated_test() {
        when(interactor.getKeyGeneratedInstance()).thenReturn(false);

        presenter.initializeViews();

        verify(view, times(1)).openStartPageFragment(false);
    }

    @Test
    public void openStartFragment_keyGenerated_sendFromIntentTrue_test() {
        when(interactor.getKeyGeneratedInstance()).thenReturn(true);
        presenter.setSendFromIntent(true);
        presenter.initializeViews();

        verify(view, times(1)).openPinFragmentRoot(PinAction.AUTHENTICATION_AND_SEND);
    }

    @Test
    public void openStartFragment_keyGenerated_test() {
        when(interactor.getKeyGeneratedInstance()).thenReturn(true);
        presenter.initializeViews();

        verify(view, times(1)).openStartPageFragment(true);
    }

    @Test
    public void stop_test() {
        when(interactor.getKeyGeneratedInstance()).thenReturn(true);
        presenter.onLogin();
        presenter.onStop();
        presenter.onPostResume();

        verify(view, times(1)).openPinFragment(CHECK_AUTHENTICATION);
    }

    @Test
    public void updateNetworkStateTrue_DGPInfoLoaded_FeePerKbLoaded_test() {
        when(interactor.getKeyGeneratedInstance()).thenReturn(true);
        when(interactor.isDGPInfoLoaded()).thenReturn(true);
        when(interactor.isFeePerkbLoaded()).thenReturn(true);
        presenter.updateNetworkSate(true);

        verify(interactor, never()).getDGPInfo();
        verify(interactor, never()).getFeePerKb();
    }

    @Test
    public void updateNetworkStateTrue_DGPInfoNotLoaded_FeePerKbNotLoaded_test() {
        FeePerKb feePerKb = mock(FeePerKb.class);
        DGPInfo dgpInfo = mock(DGPInfo.class);
        when(interactor.getKeyGeneratedInstance()).thenReturn(true);
        when(interactor.isDGPInfoLoaded()).thenReturn(false);
        when(interactor.isFeePerkbLoaded()).thenReturn(false);
        when(interactor.getFeePerKb()).thenReturn(Observable.just(feePerKb));
        when(interactor.getDGPInfo()).thenReturn(Observable.just(dgpInfo));
        presenter.updateNetworkSate(true);

        verify(interactor, times(1)).getDGPInfo();
        verify(interactor, times(1)).getFeePerKb();

        verify(interactor, times(1)).setDGPInfo(dgpInfo);
        verify(interactor, times(1)).setFeePerKb(feePerKb);
    }

    @Test
    public void onDestroy_Test() {
        presenter.onDestroy();

        verify(interactor, times(1)).clearStatic();
        verify(view, times(1)).clearService();
        verify(interactor, times(1)).removeLanguageChangeListener((LanguageChangeListener) any());
    }

    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().reset();
    }
}
