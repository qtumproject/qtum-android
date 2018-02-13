package org.qtum.wallet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.model.gson.token_history.TokenHistory;
import org.qtum.wallet.model.gson.token_history.TokenHistoryResponse;
import org.qtum.wallet.ui.fragment.token_fragment.TokenInteractor;
import org.qtum.wallet.ui.fragment.token_fragment.TokenPresenterImpl;
import org.qtum.wallet.ui.fragment.token_fragment.TokenView;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
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
        presenter = new TokenPresenterImpl(view, interactor);
    }

    private static final Token TEST_TOKEN_WITH_DECIMALS = new Token(10, new BigDecimal("15"));
    private static final Token TEST_TOKEN_WITHOUT_DECIMALS = new Token(new BigDecimal("15"));
    private static final TokenHistory TEST_TOKEN_HISTORY = new TokenHistory("test", "test", "test", "test", "test", 20);
    private static final List<TokenHistory> TEST_TOKEN_HISTORY_LIST = Arrays.asList(TEST_TOKEN_HISTORY);
    private static final TokenHistoryResponse TEST_HISTORY_RESPONSE = new TokenHistoryResponse(10, 10, 10, Arrays.asList(TEST_TOKEN_HISTORY));

    @Test
    public void initialize_TokenWithDecimals() {
        when(interactor.getHistoryList(anyString(), anyInt(), anyInt())).thenReturn(Observable.just(TEST_HISTORY_RESPONSE));
        presenter.setToken(TEST_TOKEN_WITH_DECIMALS);
        presenter.initializeViews();

        verify(view, times(1)).setQtumAddress(interactor.getCurrentAddress());

    }

//    @Test
//    public void initialize_TokenWithoutDecimals() {
//        when(interactor.getHistoryList(anyString(), anyInt(), anyInt())).thenReturn(Observable.just(TEST_HISTORY_RESPONSE));
//        presenter.setToken(TEST_TOKEN_WITHOUT_DECIMALS);
//        presenter.initializeViews();
//
//        verify(interactor, times(1)).setupPropertyDecimalsValue((Token) any(), (Subscriber<String>) any());
//
//        verify(view, never()).onContractPropertyUpdated(anyString(), anyString());
//        verify(view, never()).setBalance(anyString());
//    }

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

    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().reset();
    }

}
