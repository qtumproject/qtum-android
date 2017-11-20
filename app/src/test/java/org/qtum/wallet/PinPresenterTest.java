package org.qtum.wallet;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.ui.fragment.pin_fragment.PinInteractor;
import org.qtum.wallet.ui.fragment.pin_fragment.PinPresenterImpl;
import org.qtum.wallet.ui.fragment.pin_fragment.PinView;

import javax.crypto.Cipher;

import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.qtum.wallet.ui.fragment.pin_fragment.PinAction.AUTHENTICATION;
import static org.qtum.wallet.ui.fragment.pin_fragment.PinAction.AUTHENTICATION_AND_SEND;
import static org.qtum.wallet.ui.fragment.pin_fragment.PinAction.AUTHENTICATION_FOR_PASSPHRASE;
import static org.qtum.wallet.ui.fragment.pin_fragment.PinAction.CHANGING;
import static org.qtum.wallet.ui.fragment.pin_fragment.PinAction.CHECK_AUTHENTICATION;
import static org.qtum.wallet.ui.fragment.pin_fragment.PinAction.CREATING;
import static org.qtum.wallet.ui.fragment.pin_fragment.PinAction.IMPORTING;

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
        when(interactor.getSixDigitPassword()).thenReturn("");
        presenter.setAction(AUTHENTICATION);
        presenter.initializeViews();
        verify(view,times(1)).setToolBarTitle(anyInt());
    }

    private String TOUCH_ID_PASSWORD = "touch_id_password";
    private String PIN = "4444";

    @Test
    public void onAuthenticationSucceeded_test(){
        Cipher cipher = mock(Cipher.class);
        when(interactor.getTouchIdPassword()).thenReturn(TOUCH_ID_PASSWORD);
        when(interactor.decode(TOUCH_ID_PASSWORD,cipher)).thenReturn(PIN);

        presenter.onAuthenticationSucceeded(cipher);

        verify(view, times(1)).setPin(PIN);
    }

    @Test
    public void cancel_authentication(){
        presenter.setAction(AUTHENTICATION);
        presenter.cancel();

        verify(view, times(1)).onCancelClick();
    }

    @Test
    public void cancel_authenticationAndSend(){
        presenter.setAction(AUTHENTICATION_AND_SEND);
        presenter.cancel();

        verify(view, times(1)).onCancelClick();
    }

    @Test
    public void cancel_creating(){
        presenter.setAction(CREATING);
        presenter.cancel();

        verify(view, times(1)).onCancelClick();
    }

    @Test
    public void cancel_importing(){
        presenter.setAction(IMPORTING);
        presenter.cancel();

        verify(view, times(1)).onCancelClick();
    }

    @Test
    public void cancel_authenticationForPassphrase(){
        presenter.setAction(AUTHENTICATION_FOR_PASSPHRASE);
        presenter.cancel();

        verify(view, times(1)).onCancelClick();
    }

    @Test
    public void cancel_checkAuthentication(){
        presenter.setAction(CHECK_AUTHENTICATION);
        presenter.cancel();

        verify(view, times(1)).onBackPressed();
    }

    @Test
    public void cancel_changing(){
        presenter.setAction(CHANGING);
        presenter.cancel();

        verify(view, times(1)).onBackPressed();
    }

    private String PASSPHRASE = "passphrase";
    private String INCORRECT_REPEATED_PIN = "3333";
    private String PIN_HASH = "pin_hash";

    @Test
    public void confirm_creating_withoutTouchId_test(){
        when(interactor.createWallet()).thenReturn(Observable.just(PASSPHRASE));
        when(view.checkAvailabilityTouchId()).thenReturn(false);
        when(interactor.generateSHA256String(PIN)).thenReturn(PIN_HASH);

        presenter.setAction(CREATING);
        presenter.confirm(PIN);
        verify(view,times(1)).clearError();
        presenter.confirm(PIN);
        verify(view,times(2)).clearError();
        verify(view,times(1)).setProgressDialog();
        verify(view,times(1)).hideKeyBoard();
        verify(interactor, times(1)).setKeyGeneratedInstance(true);

        verify(interactor,times(1)).savePassword(anyString());
        verify(view,times(1)).onLogin();
        verify(view,times(1)).dismiss();
        verify(view,times(1)).openBackUpWalletFragment(true,PIN);
        verify(view,times(1)).dismissProgressDialog();

    }

    @Test
    public void confirm_creating_withTouchId_test(){
        when(interactor.createWallet()).thenReturn(Observable.just(PASSPHRASE));
        when(view.checkAvailabilityTouchId()).thenReturn(true);
        when(interactor.encodeInBackground(PIN)).thenReturn(Observable.just(TOUCH_ID_PASSWORD));
        when(interactor.generateSHA256String(PIN)).thenReturn(PIN_HASH);

        presenter.setAction(CREATING);
        presenter.confirm(PIN);
        verify(view,times(1)).clearError();
        presenter.confirm(PIN);
        verify(view,times(2)).clearError();
        verify(view,times(1)).setProgressDialog();
        verify(view,times(1)).hideKeyBoard();
        verify(interactor, times(1)).setKeyGeneratedInstance(true);

        verify(interactor,times(1)).saveTouchIdPassword(TOUCH_ID_PASSWORD);
        verify(interactor,times(1)).savePassword(anyString());
        verify(view,times(1)).onLogin();
        verify(view,times(1)).openTouchIDPreferenceFragment(false,PIN);
        verify(view,times(1)).dismissProgressDialog();

    }

    @Test
    public void confirm_creating_incorrectRepeatedPin_test(){
        when(interactor.createWallet()).thenReturn(Observable.just(PASSPHRASE));

        presenter.setAction(CREATING);
        presenter.confirm(PIN);
        verify(view,times(1)).clearError();
        presenter.confirm(INCORRECT_REPEATED_PIN);
        verify(view,times(1)).confirmError(anyInt());

    }

    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().reset();
    }

}