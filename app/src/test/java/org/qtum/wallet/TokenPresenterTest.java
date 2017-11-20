package org.qtum.wallet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.ui.fragment.token_fragment.TokenInteractor;
import org.qtum.wallet.ui.fragment.token_fragment.TokenPresenterImpl;
import org.qtum.wallet.ui.fragment.token_fragment.TokenView;
import org.qtum.wallet.utils.ContractManagementHelper;

import java.math.BigDecimal;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class TokenPresenterTest {

    @Mock
    private TokenView view;
    @Mock
    private TokenInteractor interactor;
    private TokenPresenterImpl presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        presenter = new TokenPresenterImpl(view, interactor);
    }

    private static final Token TEST_TOKEN_WITH_DECIMALS = new Token(10, new BigDecimal("15"));
    private static final Token TEST_TOKEN_WITHOUT_DECIMALS = new Token(new BigDecimal("15"));

    @Test
    public void initialize_TokenWithDecimals() {

        presenter.setToken(TEST_TOKEN_WITH_DECIMALS);
        presenter.initializeViews();

        verify(view, times(1)).onContractPropertyUpdated(anyString(), anyString());
        verify(view, times(1)).setBalance(anyString());

        verify(interactor, never()).setupPropertyDecimalsValue((Token) any(), (ContractManagementHelper.GetPropertyValueCallBack) any());
    }

    @Test
    public void initialize_TokenWithoutDecimals() {
        presenter.setToken(TEST_TOKEN_WITHOUT_DECIMALS);
        presenter.initializeViews();

        verify(interactor, times(1)).setupPropertyDecimalsValue((Token) any(), (ContractManagementHelper.GetPropertyValueCallBack) any());

        verify(view, never()).onContractPropertyUpdated(anyString(), anyString());
        verify(view, never()).setBalance(anyString());
    }

    private static final String TEST_ABI = "abi";

    @Test
    public void getAbi_NotEmptyValue() {
        presenter.setAbi(TEST_ABI);

        when(view.isAbiEmpty(anyString()))
                .thenReturn(false);

        presenter.getAbi();

        verify(interactor, never()).readAbiContract(anyString());
        assertThat(presenter.getAbi())
                .isEqualTo(TEST_ABI);

    }

    @Test
    public void getAbi_EmptyValue() {
        Token testToken = new Token();
        testToken.setUiid("uuid");
        presenter.setToken(testToken);

        when(view.isAbiEmpty(anyString()))
                .thenReturn(true);
        when(interactor.readAbiContract(anyString()))
                .thenReturn(TEST_ABI);

        presenter.getAbi();

        verify(interactor, times(1)).readAbiContract(anyString());
        assertThat(presenter.getAbi())
                .isEqualTo(TEST_ABI);

    }


}
