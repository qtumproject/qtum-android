package org.qtum.wallet;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.import_wallet_fragment.ImportWalletInteractor;
import org.qtum.wallet.ui.fragment.import_wallet_fragment.ImportWalletPresenterImpl;
import org.qtum.wallet.ui.fragment.import_wallet_fragment.ImportWalletView;
import org.qtum.wallet.ui.fragment.pin_fragment.PinAction;

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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ImportWalletInteractorTest {

    @Mock
    ImportWalletView view;
    @Mock
    ImportWalletInteractor interactor;
    ImportWalletPresenterImpl presenter;

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

        presenter = new ImportWalletPresenterImpl(view, interactor);
    }

    private String VALID_PASSPHRASE1 = "a b c d e f g h i j k l";
    private String VALID_PASSPHRASE2 = "   a b c d e f g    h i j k l   ";
    private String INVALID_PASSPHRASE_WORD_COUNT_11 = "a b c de f g h i j k l";
    private String INVALID_PASSPHRASE_WORD_COUNT_13 = "a b c d e f g h i j k l m";
    private String INVALID_NUMBER_IN_PASSPHRASE = "a b c d e 9 g h i j k l m";

    @Test
    public void onPassphraseChange_validString1_test() {
        presenter.onPassphraseChange(VALID_PASSPHRASE1);
        verify(view, never()).disableImportButton();
        verify(view, times(1)).enableImportButton();
    }

    @Test
    public void onPassphraseChange_validString2_test() {
        presenter.onPassphraseChange(VALID_PASSPHRASE2);
        verify(view, never()).disableImportButton();
        verify(view, times(1)).enableImportButton();
    }

    @Test
    public void onPassphraseChange_invalidWordCount11_test() {
        presenter.onPassphraseChange(INVALID_PASSPHRASE_WORD_COUNT_11);
        verify(view, times(1)).disableImportButton();
        verify(view, never()).enableImportButton();
    }

    @Test
    public void onPassphraseChange_invalidWordCount13_test() {
        presenter.onPassphraseChange(INVALID_PASSPHRASE_WORD_COUNT_13);
        verify(view, times(1)).disableImportButton();
        verify(view, never()).enableImportButton();
    }

    @Test
    public void onPassphraseChange_invalidNumberInString_test() {
        presenter.onPassphraseChange(INVALID_NUMBER_IN_PASSPHRASE);
        verify(view, times(1)).disableImportButton();
        verify(view, never()).enableImportButton();
    }

    @Test
    public void onResume_dataLoadedTrue_test() {
        presenter.setDataLoaded(true);
        presenter.onResume();
        verify(view, times(1)).dismissProgressDialog();
        verify(view, times(1)).openPinFragment(anyString(), (PinAction) any());
    }

    @Test
    public void onResume_dataLoadedFalse_test() {
        presenter.setDataLoaded(false);
        presenter.onResume();
        verify(view, never()).dismissProgressDialog();
        verify(view, never()).openPinFragment(anyString(), (PinAction) any());
    }

    @Test
    public void onImportClick_Success() {
        when(interactor.importWallet(VALID_PASSPHRASE1)).thenReturn(Observable.just(VALID_PASSPHRASE1));
        presenter.onImportClick(VALID_PASSPHRASE1);
        verify(view, times(1)).dismissProgressDialog();
        verify(view, times(1)).openPinFragment(eq(VALID_PASSPHRASE1), (PinAction) any());
    }

    @Test
    public void onImportClick_Error() {
        when(interactor.importWallet(VALID_PASSPHRASE1)).thenReturn(Observable.<String>error(new Throwable()));
        presenter.onImportClick(VALID_PASSPHRASE1);
        verify(view, never()).openPinFragment(eq(VALID_PASSPHRASE1), (PinAction) any());
        verify(view, times(1)).setAlertDialog(anyInt(), anyInt(), eq(BaseFragment.PopUpType.error));
    }

    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().reset();
    }

}
