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

        presenter.onBalanceChanged(TEST_UNCONFIRMED_BALANCE, TEST_BALANCE);

        verify(view, times(1)).updateBalance(anyString(),anyString(),anyString());
        verify(view, never()).updateBalance(anyString(), anyString(),anyString());
    }

    @Test
    public void onBalanceChanged_ValidUnconfirmedBalance() {
        when(view.isUnconfirmedBalanceValid(anyString()))
                .thenReturn(true);

        presenter.onBalanceChanged(TEST_UNCONFIRMED_BALANCE, TEST_BALANCE);

        verify(view, times(1)).updateBalance(anyString(), anyString(), anyString());
        verify(view, never()).updateBalance(anyString(),anyString(), anyString());
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
