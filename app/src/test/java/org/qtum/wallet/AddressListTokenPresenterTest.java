package org.qtum.wallet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.model.DeterministicKeyWithTokenBalance;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.model.gson.token_balance.TokenBalance;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.token_cash_management_fragment.AddressesListTokenInteractor;
import org.qtum.wallet.ui.fragment.token_cash_management_fragment.AddressesListTokenPresenterImpl;
import org.qtum.wallet.ui.fragment.token_cash_management_fragment.AddressesListTokenView;

import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class AddressListTokenPresenterTest {

    @Mock
    private AddressesListTokenView view;
    @Mock
    private AddressesListTokenInteractor interactor;
    private AddressesListTokenPresenterImpl presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        presenter = new AddressesListTokenPresenterImpl(view, interactor);
    }

    @Test
    public void run_BalanceChanged_Success() {
        presenter.setKeysWithTokenBalance(Collections.<DeterministicKeyWithTokenBalance>emptyList());
        when(interactor.isCurrencyValid(anyString()))
                .thenReturn(true);

        presenter.run();

        verify(view, times(1)).updateAddressList(anyList(), anyString());
    }

    @Test
    public void run_BalanceChanged_Error() {
        presenter.setKeysWithTokenBalance(Collections.<DeterministicKeyWithTokenBalance>emptyList());
        when(interactor.isCurrencyValid(anyString()))
                .thenReturn(false);

        presenter.run();

        verify(view, never()).updateAddressList(anyList(), anyString());
    }

    @Test
    public void transfer_InvalidAmount() {
        when(interactor.isAmountValid(anyString()))
                .thenReturn(false);

        presenter.transfer(null, null, "test amount");

        verify(view, times(1)).setAlertDialog(anyInt(), anyInt(), anyInt(), (BaseFragment.PopUpType) any());
        verify(view, never()).goToSendFragment((DeterministicKeyWithTokenBalance) any(),
                (DeterministicKeyWithTokenBalance) any(), anyString(), anyString());
    }

    private final String TEST_ZERO_AMOUNT = "0";
    private final String TEST_AMOUNT = "10";

    @Test
    public void transfer_AmountIsZeroError() {
        when(interactor.isAmountValid(anyString()))
                .thenReturn(true);


        presenter.transfer(null, null, TEST_ZERO_AMOUNT);

        verify(view, times(1)).setAlertDialog(anyInt(), anyInt(), anyInt(), (BaseFragment.PopUpType) any());
        verify(view, never()).goToSendFragment((DeterministicKeyWithTokenBalance) any(),
                (DeterministicKeyWithTokenBalance) any(), anyString(), anyString());
    }

    @Test
    public void transfer_InvalidTokenBalanceError() {
        when(interactor.isAmountValid(anyString()))
                .thenReturn(true);


        presenter.transfer(null, null, TEST_AMOUNT);

        verify(view, times(1)).hideTransferDialog();
        verify(view, times(1)).dismissProgressDialog();
        verify(view, times(1)).setAlertDialog(anyInt(), anyInt(), anyString(), (BaseFragment.PopUpType) any());
        verify(view, never()).goToSendFragment((DeterministicKeyWithTokenBalance) any(),
                (DeterministicKeyWithTokenBalance) any(), anyString(), anyString());
    }

    @Test
    public void transfer_Success() {
        when(interactor.isAmountValid(anyString()))
                .thenReturn(true);
        presenter.setTokenBalance(new TokenBalance());
        presenter.setToken(new Token(true, "contract address"));
        when(interactor.isValidForAddress((TokenBalance) any(), (DeterministicKeyWithTokenBalance) any()))
                .thenReturn(true);
        when(interactor.isValidBalance((TokenBalance) any(), (DeterministicKeyWithTokenBalance) any(), anyString(), anyInt()))
                .thenReturn(true);


        presenter.transfer(null, null, TEST_AMOUNT);

        verify(view, times(1)).hideTransferDialog();
        verify(view, times(1)).goToSendFragment((DeterministicKeyWithTokenBalance) any(),
                (DeterministicKeyWithTokenBalance) any(), anyString(), anyString());

        verify(view, never()).dismissProgressDialog();
        verify(view, never()).setAlertDialog(anyInt(), anyInt(), anyString(), (BaseFragment.PopUpType) any());
    }

}
