package org.qtum.wallet;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.ui.fragment.backup_wallet_fragment.BackUpWalletInteractor;
import org.qtum.wallet.ui.fragment.backup_wallet_fragment.BackUpWalletPresenterImpl;
import org.qtum.wallet.ui.fragment.backup_wallet_fragment.BackUpWalletView;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BackUpWalletPresenterTests {

    @Mock
    BackUpWalletView view;
    @Mock
    BackUpWalletInteractor interactor;
    BackUpWalletPresenterImpl presenter;

    private static final String pin = "4444";
    private static final String passphrase = "TestPassPhrase";

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        presenter = new BackUpWalletPresenterImpl(view, interactor);
    }

    @Test
    public void initializeView_test() {
        when(view.getPin()).thenReturn(pin);
        when(interactor.getPassphrase(pin)).thenReturn(passphrase);
        presenter.initializeViews();
        verify(view, times(1)).setBrainCode(passphrase);
    }

    @Test
    public void onCopyBrainCodeClick_test() {
        when(interactor.getPassphrase(anyString())).thenReturn(passphrase);
        presenter.initializeViews();
        presenter.onCopyBrainCodeClick();
        verify(view, times(1)).copyToClipboard(passphrase);
        verify(view, times(1)).showToast();
    }

    @Test
    public void onShareClick_test() {
        when(interactor.getPassphrase(anyString())).thenReturn(passphrase);
        presenter.initializeViews();
        presenter.onShareClick();
        verify(view, times(1)).chooseShareMethod(passphrase);
    }

}