package org.qtum.wallet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.ui.fragment.receive_fragment.ReceiveInteractor;
import org.qtum.wallet.ui.fragment.receive_fragment.ReceivePresenterImpl;
import org.qtum.wallet.ui.fragment.receive_fragment.ReceiveView;

import java.math.BigDecimal;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by drevnitskaya on 05.10.17.
 */

public class ReceivePresenterTest {
    @Mock
    private ReceiveView view;
    @Mock
    private ReceiveInteractor interactor;
    private ReceivePresenterImpl presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        presenter = new ReceivePresenterImpl(view, interactor);
    }

    private static final BigDecimal TEST_UNCONFIRMED_BALANCE = new BigDecimal("10");
    private static final BigDecimal TEST_BALANCE = new BigDecimal("15");

    @Test
    public void onBalanceChanged_InvalidUnconfirmedBalance() {
        when(view.isUnconfirmedBalanceValid(anyString()))
                .thenReturn(false);
        when(interactor.formatBalance(anyString()))
                .thenReturn("Formatted Balance");

        presenter.onBalanceChanged(TEST_UNCONFIRMED_BALANCE, TEST_BALANCE);

        verify(view, times(1)).updateBalance(anyString());
        verify(view, never()).updateBalance(anyString(), anyString());
    }

    @Test
    public void onBalanceChanged_ValidUnconfirmedBalance() {
        when(view.isUnconfirmedBalanceValid(anyString()))
                .thenReturn(true);
        when(interactor.formatBalance(anyString()))
                .thenReturn("Formatted Balance");

        presenter.onBalanceChanged(TEST_UNCONFIRMED_BALANCE, TEST_BALANCE);

        verify(view, times(1)).updateBalance(anyString(), anyString());
        verify(view, never()).updateBalance(anyString());
    }

    @Test
    public void changeAmount() {
        presenter.changeAmount("Amount");

        verify(view, times(1)).showSpinner();
        verify(view, times(1)).imageEncodeObservable(anyString());
    }

    @Test
    public void setTokenAddress() {
        presenter.setTokenAddress("Token Address");

        verify(view, times(1)).showSpinner();
        verify(view, times(1)).imageEncodeObservable(anyString());
    }

    @Test
    public void changeAddress() {
        presenter.changeAddress();

        verify(view, times(1)).showSpinner();
        verify(view, times(1)).imageEncodeObservable(anyString());
        verify(view, times(1)).setUpAddress(anyString());
    }
}
