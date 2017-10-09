//package org.qtum.wallet;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Matchers;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.qtum.wallet.model.Currency;
//import org.qtum.wallet.model.CurrencyToken;
//import org.qtum.wallet.model.contract.Token;
//import org.qtum.wallet.model.gson.FeePerKb;
//import org.qtum.wallet.model.gson.call_smart_contract_response.CallSmartContractResponse;
//import org.qtum.wallet.model.gson.call_smart_contract_response.Item;
//import org.qtum.wallet.model.gson.token_balance.Balance;
//import org.qtum.wallet.model.gson.token_balance.TokenBalance;
//import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
//import org.qtum.wallet.ui.fragment.send_fragment.SendInteractor;
//import org.qtum.wallet.ui.fragment.send_fragment.SendInteractorImpl;
//import org.qtum.wallet.ui.fragment.send_fragment.SendPresenterImpl;
//import org.qtum.wallet.ui.fragment.send_fragment.SendView;
//
//import java.math.BigDecimal;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import rx.Observable;
//import rx.Scheduler;
//import rx.android.plugins.RxAndroidPlugins;
//import rx.android.plugins.RxAndroidSchedulersHook;
//import rx.plugins.RxJavaPlugins;
//import rx.plugins.RxJavaSchedulersHook;
//import rx.schedulers.Schedulers;
//
//import static org.assertj.core.api.Java6Assertions.assertThat;
//import static org.mockito.Matchers.any;
//import static org.mockito.Matchers.anyDouble;
//import static org.mockito.Matchers.anyInt;
//import static org.mockito.Matchers.anyString;
//import static org.mockito.Mockito.never;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
///**
// * Created by drevnitskaya on 03.10.17.
// */
//
//public class SendPresenterTest {
//
//    private static final String TEST_CURRENCY_VALUE = "Qtum";
//    private static final List<Token> TEST_LIST_TOKENS = Arrays.asList(new Token(true, ""), new Token(true, TEST_CURRENCY_VALUE));
//    private static final List<Token> TEST_LIST_TOKENS_WITH_UNSUBSCRIBED_ITEMS = Arrays.asList(new Token(true), new Token(true), new Token(true), new Token(false));
//    private static final List<Token> TEST_EMPTY_LIST_TOKENS = Collections.emptyList();
//    private static final FeePerKb TEST_FEE_PER_KB = new FeePerKb(new BigDecimal("10.0"));
//    private static final double TEST_FEE_DEFAULT_VALUE = 0.0;
//
//    @Mock
//    private SendView view;
//    @Mock
//    private SendInteractor interactor;
//    private SendPresenterImpl presenter;
//
//    @Before
//    public void init() {
//        MockitoAnnotations.initMocks(this);
//        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
//            @Override
//            public Scheduler getMainThreadScheduler() {
//                return Schedulers.immediate();
//            }
//        });
//        RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
//            @Override
//            public Scheduler getIOScheduler() {
//                return Schedulers.immediate();
//            }
//        });
//
//        presenter = new SendPresenterImpl(view, interactor);
//    }
//
//    @Test
//    public void initialize_WithTokensFeeSuccess() {
//        when(interactor.getTokenList())
//                .thenReturn(TEST_LIST_TOKENS);
//        when(interactor.getFeePerKbObservable())
//                .thenReturn(Observable.just(TEST_FEE_PER_KB));
//
//        presenter.initializeViews();
//
//        verify(view, times(1)).setUpCurrencyField(anyInt());
//        verify(interactor, times(1)).handleFeePerKbValue(Matchers.<FeePerKb>any());
//        assertThat(presenter.getMinFee())
//                .isEqualTo(Double.valueOf(TEST_FEE_PER_KB.getFeePerKb().toString()));
//        verify(view, times(1)).updateFee(anyDouble(), anyDouble());
//
//        assertThat(presenter.getTokenList())
//                .hasSize(2);
//    }
//
//    @Test
//    public void initialize_WithTokensFeeError() {
//        when(interactor.getTokenList())
//                .thenReturn(TEST_LIST_TOKENS);
//        when(interactor.getFeePerKbObservable())
//                .thenReturn(Observable.<FeePerKb>error(new Throwable("Getting Fee error")));
//
//        presenter.initializeViews();
//
//        verify(view, times(1)).setUpCurrencyField(anyInt());
//        verify(interactor, never()).handleFeePerKbValue(Matchers.<FeePerKb>any());
//        assertThat(presenter.getMinFee())
//                .isEqualTo(TEST_FEE_DEFAULT_VALUE);
//        verify(view, never()).updateFee(anyDouble(), anyDouble());
//    }
//
//    @Test
//    public void initialize_EmptyTokensFeeError() {
//        when(interactor.getTokenList())
//                .thenReturn(TEST_EMPTY_LIST_TOKENS);
//        when(interactor.getFeePerKbObservable())
//                .thenReturn(Observable.<FeePerKb>error(new Throwable("Getting Fee error")));
//
//        presenter.initializeViews();
//
//        verify(view, never()).setUpCurrencyField(anyInt());
//        verify(view, times(1)).hideCurrencyField();
//
//
//        verify(interactor, never()).handleFeePerKbValue(Matchers.<FeePerKb>any());
//        assertThat(presenter.getMinFee())
//                .isEqualTo(TEST_FEE_DEFAULT_VALUE);
//        verify(view, never()).updateFee(anyDouble(), anyDouble());
//    }
//
//    @Test
//    public void initialize_TokensWithUnsubscribedItems() {
//        when(interactor.getTokenList())
//                .thenReturn(TEST_LIST_TOKENS_WITH_UNSUBSCRIBED_ITEMS);
//        when(interactor.getFeePerKbObservable())
//                .thenReturn(Observable.<FeePerKb>error(new Throwable("Getting Fee error")));
//
//        presenter.initializeViews();
//
//        verify(view, times(1)).setUpCurrencyField(anyInt());
//        verify(view, never()).hideCurrencyField();
//
//
//        verify(interactor, never()).handleFeePerKbValue(Matchers.<FeePerKb>any());
//        assertThat(presenter.getMinFee())
//                .isEqualTo(TEST_FEE_DEFAULT_VALUE);
//        verify(view, never()).updateFee(anyDouble(), anyDouble());
//
//        assertThat(presenter.getTokenList())
//                .hasSize(3);
//    }
//
//    @Test
//    public void handleBalanceChanges_BalanceNotNull() {
//        presenter.handleBalanceChanges(new BigDecimal("10.0"), new BigDecimal("10.0"));
//
//        verify(view, times(1)).handleBalanceUpdating(anyString(), (BigDecimal) any());
//    }
//
//    @Test
//    public void setupCurrency_Success() {
//        when(interactor.getTokenList())
//                .thenReturn(TEST_LIST_TOKENS);
//
//        presenter.searchAndSetUpCurrency(TEST_CURRENCY_VALUE);
//
//        verify(view, times(1)).setUpCurrencyField(Matchers.<CurrencyToken>any());
//    }
//
//    @Test
//    public void setupCurrency_EmptyTokens() {
//        when(interactor.getTokenList())
//                .thenReturn(TEST_EMPTY_LIST_TOKENS);
//
//        presenter.searchAndSetUpCurrency(TEST_CURRENCY_VALUE);
//
//        verify(view, never()).setUpCurrencyField(Matchers.<CurrencyToken>any());
//    }
//
//    @Test
//    public void currencyChoose() {
//        presenter.onCurrencyChoose(new Currency(TEST_CURRENCY_VALUE));
//
//        verify(view, times(1)).setUpCurrencyField(Matchers.<CurrencyToken>any());
//    }
//
//
//    private static final String TEST_ADDRESS = "address";
//    private static final String TEST_TOKEN_ADDRESS = "token address";
//    private static final String TEST_CONTRACT_NAME = "contract name";
//    private static final double TEST_AMOUNT = 10.0;
//
//    @Test
//    public void onResponse_Success() {
//        when(view.isTokenEmpty(anyString()))
//                .thenReturn(false);
//        when(interactor.validateTokenExistance(anyString()))
//                .thenReturn(TEST_CONTRACT_NAME);
//
//        presenter.onResponse(TEST_ADDRESS, TEST_AMOUNT, TEST_TOKEN_ADDRESS);
//
//        verify(view, times(1)).updateData(anyString(), anyDouble(), anyString());
//        verify(view, never()).setAlertDialog(anyInt(), anyString(), (BaseFragment.PopUpType) any());
//    }
//
//    @Test
//    public void onResponse_Error() {
//        when(view.isTokenEmpty(anyString()))
//                .thenReturn(false);
//        when(interactor.validateTokenExistance(anyString()))
//                .thenReturn(null);
//
//        presenter.onResponse(TEST_ADDRESS, TEST_AMOUNT, TEST_TOKEN_ADDRESS);
//
//        verify(view, times(1)).updateData(anyString(), anyDouble(), anyString());
//        verify(view, times(1)).setAlertDialog(anyInt(), anyString(), (BaseFragment.PopUpType) any());
//    }
//
//
//    private static final double TEST_MIN_FEE = 0.1;
//    private static final double TEST_MAX_FEE = 0.2;
//    private static final String TEST_INVALID_FEE_VALUE = "0.3";
//    private static final String TEST_VALID_FEE_VALUE = "0.16";
//
//    @Test
//    public void send_NetworkConnectedSuccess_InvalidFee() {
//        when(view.getFeeInput())
//                .thenReturn(TEST_INVALID_FEE_VALUE);
//
//        presenter.updateNetworkSate(true);
//        presenter.setMinFee(TEST_MIN_FEE);
//        presenter.setMaxFee(TEST_MAX_FEE);
//        presenter.send();
//
//        verify(view, times(1)).dismissProgressDialog();
//        verify(view, times(1)).setAlertDialog(anyInt(), anyInt(), anyString(), (BaseFragment.PopUpType) any());
//
//        verify(view, never()).showPinDialog();
//    }
//
//    @Test
//    public void send_NetworkConnectedSuccess_InvalidAmount() {
//        when(view.isValidAmount())
//                .thenReturn(false);
//        when(view.getFeeInput())
//                .thenReturn(TEST_VALID_FEE_VALUE);
//
//        presenter.updateNetworkSate(true);
//        presenter.setMinFee(TEST_MIN_FEE);
//        presenter.setMaxFee(TEST_MAX_FEE);
//        presenter.send();
//
//        verify(view, never()).showPinDialog();
//    }
//
//    @Test
//    public void send_NetworkConnectedSuccess_ValidParams() {
//        when(view.isValidAmount())
//                .thenReturn(true);
//        when(view.getFeeInput())
//                .thenReturn(TEST_VALID_FEE_VALUE);
//
//        presenter.updateNetworkSate(true);
//        presenter.setMinFee(TEST_MIN_FEE);
//        presenter.setMaxFee(TEST_MAX_FEE);
//        presenter.send();
//
//        verify(view, times(1)).showPinDialog();
//    }
//
//    @Test
//    public void send_NetworkNotConnected() {
//        presenter.updateNetworkSate(false);
//
//        verify(view, never()).showPinDialog();
//        verify(view, never()).setAlertDialog(anyInt(), anyString(), (BaseFragment.PopUpType) any());
//    }
//
//    private static final String TEST_FROM_ADDRESS = "from address";
//    private static final String TEST_ADDRESS_INPUT = "address input";
//    private static final String TEST_AMOUNT_INPUT = "0.2";
//    private static final String TEST_FEE_INPUT = "0.15";
//    private static final String TEST_CURRENCY_NAME_WITHOUT_PREF = "name";
//    private static final String TEST_CURRENCY_NAME_WITH_PREF = "Qtum name";
//
//    @Test
//    public void onPin_SendTx() {
//        when(view.getCurrency())
//                .thenReturn(new Currency(TEST_CURRENCY_NAME_WITH_PREF));
//        when(view.getFromAddress())
//                .thenReturn(TEST_FROM_ADDRESS);
//        when(view.getAddressInput())
//                .thenReturn(TEST_ADDRESS_INPUT);
//        when(view.getAmountInput())
//                .thenReturn(TEST_AMOUNT_INPUT);
//        when(view.getFeeInput())
//                .thenReturn(TEST_FEE_INPUT);
//
//        when(view.getStringValue(anyInt()))
//                .thenReturn(TEST_CURRENCY_NAME_WITHOUT_PREF);
//
//        presenter.onPinSuccess();
//
//        verify(interactor, times(1)).sendTx(anyString(), anyString(), anyString(), anyString(), (SendInteractorImpl.SendTxCallBack) any());
//    }
//
//    private static final String TEST_CONTACT_ADDRESS = "contract address";
//    private static final Token TEST_TOKEN = new Token(true, TEST_CONTACT_ADDRESS);
//    private static final BigDecimal TEST_CURRENT_BALANCE_VALUE = new BigDecimal("20.0");
//
//    @Test
//    public void onPin_NotEqualsCurrenciesNames_WithFromAddress_ItemWithExceptedVal() {
//        when(view.getCurrency())
//                .thenReturn(new CurrencyToken(TEST_CURRENCY_NAME_WITH_PREF, TEST_TOKEN));
//        when(view.getFromAddress())
//                .thenReturn(TEST_FROM_ADDRESS);
//        when(view.getAddressInput())
//                .thenReturn(TEST_ADDRESS_INPUT);
//        when(view.getAmountInput())
//                .thenReturn(TEST_AMOUNT_INPUT);
//        when(view.getFeeInput())
//                .thenReturn(TEST_FEE_INPUT);
//        when(view.getStringValue(anyInt()))
//                .thenReturn(TEST_CURRENCY_VALUE);
//        when(view.getTokenBalance(anyString()))
//                .thenReturn(new TokenBalance(Arrays.asList(new Balance(TEST_FROM_ADDRESS, TEST_CURRENT_BALANCE_VALUE),
//                        new Balance(TEST_FROM_ADDRESS, TEST_CURRENT_BALANCE_VALUE))));
//        when(view.isValidAvailableAddress(anyString()))
//                .thenReturn(true);
//
//        when(interactor.createAbiMethodParamsObservable(anyString(), anyString(), anyString()))
//                .thenReturn(Observable.just("test"));
//        when(interactor.callSmartContractObservable((Token) any(), anyString()))
//                .thenReturn(Observable.just(new CallSmartContractResponse(Arrays.asList(new Item("Test")))));
//
//        presenter.setTokenList(Arrays.asList(TEST_TOKEN));
//        presenter.onPinSuccess();
//
//        verify(interactor, never()).sendTx(anyString(), anyString(), anyString(), anyString(), (SendInteractorImpl.SendTxCallBack) any());
//        verify(view, times(1)).getTokenBalance(anyString());
//        assertThat(presenter.getAvailableAddress())
//                .isEqualTo(TEST_FROM_ADDRESS);
//        verify(view, times(1)).setAlertDialog(anyInt(), anyString(), anyString(), (BaseFragment.PopUpType) any());
//        verify(interactor, never()).getUnspentOutputs(anyString(), (SendInteractorImpl.GetUnspentListCallBack) any());
//
//    }
//
//    @Test
//    public void onPin_NotEqualsCurrenciesNames_WithFromAddress_ItemWithoutExcepted() {
//        when(view.getCurrency())
//                .thenReturn(new CurrencyToken(TEST_CURRENCY_NAME_WITH_PREF, TEST_TOKEN));
//        when(view.getFromAddress())
//                .thenReturn(TEST_FROM_ADDRESS);
//        when(view.getAddressInput())
//                .thenReturn(TEST_ADDRESS_INPUT);
//        when(view.getAmountInput())
//                .thenReturn(TEST_AMOUNT_INPUT);
//        when(view.getFeeInput())
//                .thenReturn(TEST_FEE_INPUT);
//        when(view.getStringValue(anyInt()))
//                .thenReturn(TEST_CURRENCY_VALUE);
//        when(view.getTokenBalance(anyString()))
//                .thenReturn(new TokenBalance(Arrays.asList(new Balance(TEST_FROM_ADDRESS, TEST_CURRENT_BALANCE_VALUE),
//                        new Balance(TEST_FROM_ADDRESS, TEST_CURRENT_BALANCE_VALUE))));
//        when(view.isValidAvailableAddress(anyString()))
//                .thenReturn(true);
//
//        when(interactor.createAbiMethodParamsObservable(anyString(), anyString(), anyString()))
//                .thenReturn(Observable.just("test"));
//        when(interactor.callSmartContractObservable((Token) any(), anyString()))
//                .thenReturn(Observable.just(new CallSmartContractResponse(Arrays.asList(new Item("None")))));
//
//        presenter.setTokenList(Arrays.asList(TEST_TOKEN));
//        presenter.onPinSuccess();
//
//        verify(interactor, never()).sendTx(anyString(), anyString(), anyString(), anyString(), (SendInteractorImpl.SendTxCallBack) any());
//        verify(view, times(1)).getTokenBalance(anyString());
//        assertThat(presenter.getAvailableAddress())
//                .isEqualTo(TEST_FROM_ADDRESS);
//        verify(view, never()).setAlertDialog(anyInt(), anyString(), anyString(), (BaseFragment.PopUpType) any());
//        verify(interactor, times(1)).getUnspentOutputs(anyString(), (SendInteractorImpl.GetUnspentListCallBack) any());
//
//    }
//
//    @Test
//    public void onPin_NotEqualsCurrenciesNames_WithoutFromAddress_ItemWithExceptedVal() {
//        when(view.getCurrency())
//                .thenReturn(new CurrencyToken(TEST_CURRENCY_NAME_WITH_PREF, TEST_TOKEN));
//        when(view.getFromAddress())
//                .thenReturn("");
//        when(view.getAddressInput())
//                .thenReturn(TEST_ADDRESS_INPUT);
//        when(view.getAmountInput())
//                .thenReturn(TEST_AMOUNT_INPUT);
//        when(view.getFeeInput())
//                .thenReturn(TEST_FEE_INPUT);
//
//        when(view.getStringValue(anyInt()))
//                .thenReturn(TEST_CURRENCY_VALUE);
//        when(view.isValidAvailableAddress(anyString()))
//                .thenReturn(true);
//
//        when(view.getTokenBalance(anyString()))
//                .thenReturn(new TokenBalance(Arrays.asList(new Balance(TEST_FROM_ADDRESS, TEST_CURRENT_BALANCE_VALUE),
//                        new Balance(TEST_FROM_ADDRESS, TEST_CURRENT_BALANCE_VALUE))));
//
//        when(interactor.createAbiMethodParamsObservable(anyString(), anyString(), anyString()))
//                .thenReturn(Observable.just("test"));
//        when(interactor.callSmartContractObservable((Token) any(), anyString()))
//                .thenReturn(Observable.just(new CallSmartContractResponse(Arrays.asList(new Item("Test")))));
//
//        presenter.setTokenList(Arrays.asList(TEST_TOKEN));
//        presenter.onPinSuccess();
//
//        verify(interactor, never()).sendTx(anyString(), anyString(), anyString(), anyString(), (SendInteractorImpl.SendTxCallBack) any());
//        verify(view, times(1)).getTokenBalance(anyString());
//        assertThat(presenter.getAvailableAddress())
//                .isEqualTo(TEST_FROM_ADDRESS);
//        verify(view, times(1)).setAlertDialog(anyInt(), anyString(), anyString(), (BaseFragment.PopUpType) any());
//        verify(interactor, never()).getUnspentOutputs(anyString(), (SendInteractorImpl.GetUnspentListCallBack) any());
//
//    }
//
//    @Test
//    public void onPin_NotEqualsCurrenciesNames_WithoutFromAddress_ItemWithoutExceptedVal() {
//        when(view.getCurrency())
//                .thenReturn(new CurrencyToken(TEST_CURRENCY_NAME_WITH_PREF, TEST_TOKEN));
//        when(view.getFromAddress())
//                .thenReturn("");
//        when(view.getAddressInput())
//                .thenReturn(TEST_ADDRESS_INPUT);
//        when(view.getAmountInput())
//                .thenReturn(TEST_AMOUNT_INPUT);
//        when(view.getFeeInput())
//                .thenReturn(TEST_FEE_INPUT);
//
//        when(view.getStringValue(anyInt()))
//                .thenReturn(TEST_CURRENCY_VALUE);
//        when(view.isValidAvailableAddress(anyString()))
//                .thenReturn(true);
//
//        when(view.getTokenBalance(anyString()))
//                .thenReturn(new TokenBalance(Arrays.asList(new Balance(TEST_FROM_ADDRESS, TEST_CURRENT_BALANCE_VALUE),
//                        new Balance(TEST_FROM_ADDRESS, TEST_CURRENT_BALANCE_VALUE))));
//
//        when(interactor.createAbiMethodParamsObservable(anyString(), anyString(), anyString()))
//                .thenReturn(Observable.just("test"));
//        when(interactor.callSmartContractObservable((Token) any(), anyString()))
//                .thenReturn(Observable.just(new CallSmartContractResponse(Arrays.asList(new Item("None")))));
//
//        presenter.setTokenList(Arrays.asList(TEST_TOKEN));
//        presenter.onPinSuccess();
//
//        verify(interactor, never()).sendTx(anyString(), anyString(), anyString(), anyString(), (SendInteractorImpl.SendTxCallBack) any());
//        verify(view, times(1)).getTokenBalance(anyString());
//        assertThat(presenter.getAvailableAddress())
//                .isEqualTo(TEST_FROM_ADDRESS);
//        verify(view, never()).setAlertDialog(anyInt(), anyString(), anyString(), (BaseFragment.PopUpType) any());
//        verify(interactor, times(1)).getUnspentOutputs(anyString(), (SendInteractorImpl.GetUnspentListCallBack) any());
//
//    }
//
//    @Test
//    public void onPin_NotEqualsCurrenciesNames_WithFromAddress_Error() {
//        when(view.getCurrency())
//                .thenReturn(new CurrencyToken(TEST_CURRENCY_NAME_WITH_PREF, TEST_TOKEN));
//        when(view.getFromAddress())
//                .thenReturn(TEST_FROM_ADDRESS);
//        when(view.getAddressInput())
//                .thenReturn(TEST_ADDRESS_INPUT);
//        when(view.getAmountInput())
//                .thenReturn(TEST_AMOUNT_INPUT);
//        when(view.getFeeInput())
//                .thenReturn(TEST_FEE_INPUT);
//        when(view.getStringValue(anyInt()))
//                .thenReturn(TEST_CURRENCY_VALUE);
//        when(view.getTokenBalance(anyString()))
//                .thenReturn(new TokenBalance(Arrays.asList(new Balance(TEST_FROM_ADDRESS, TEST_CURRENT_BALANCE_VALUE),
//                        new Balance(TEST_FROM_ADDRESS, TEST_CURRENT_BALANCE_VALUE))));
//        when(view.isValidAvailableAddress(anyString()))
//                .thenReturn(true);
//
//        when(interactor.createAbiMethodParamsObservable(anyString(), anyString(), anyString()))
//                .thenReturn(Observable.just("test"));
//        when(interactor.callSmartContractObservable((Token) any(), anyString()))
//                .thenReturn(Observable.<CallSmartContractResponse>error(new Throwable("Params creation error")));
//
//        presenter.setTokenList(Arrays.asList(TEST_TOKEN));
//        presenter.onPinSuccess();
//
//        verify(interactor, never()).sendTx(anyString(), anyString(), anyString(), anyString(), (SendInteractorImpl.SendTxCallBack) any());
//        verify(view, times(1)).getTokenBalance(anyString());
//        assertThat(presenter.getAvailableAddress())
//                .isEqualTo(TEST_FROM_ADDRESS);
//        verify(view, times(1)).dismissProgressDialog();
//        verify(view, times(1)).setAlertDialog(anyInt(), anyString(), anyString(), (BaseFragment.PopUpType) any());
//
//    }
//
//    @Test
//    public void onPin_NotEqualsCurrenciesNames_InvalidAvailableAddress() {
//        when(view.getCurrency())
//                .thenReturn(new CurrencyToken(TEST_CURRENCY_NAME_WITH_PREF, TEST_TOKEN));
//        when(view.getFromAddress())
//                .thenReturn(TEST_FROM_ADDRESS);
//        when(view.getAddressInput())
//                .thenReturn(TEST_ADDRESS_INPUT);
//        when(view.getAmountInput())
//                .thenReturn(TEST_AMOUNT_INPUT);
//        when(view.getFeeInput())
//                .thenReturn(TEST_FEE_INPUT);
//        when(view.getStringValue(anyInt()))
//                .thenReturn(TEST_CURRENCY_VALUE);
//        when(view.getTokenBalance(anyString()))
//                .thenReturn(new TokenBalance(Arrays.asList(new Balance(TEST_FROM_ADDRESS, TEST_CURRENT_BALANCE_VALUE),
//                        new Balance(TEST_FROM_ADDRESS, TEST_CURRENT_BALANCE_VALUE))));
//        when(view.isValidAvailableAddress(anyString()))
//                .thenReturn(false);
//
//
//        presenter.setTokenList(Arrays.asList(TEST_TOKEN));
//        presenter.onPinSuccess();
//
//        verify(interactor, never()).sendTx(anyString(), anyString(), anyString(), anyString(), (SendInteractorImpl.SendTxCallBack) any());
//        verify(view, times(1)).getTokenBalance(anyString());
//        verify(interactor, never()).getUnspentOutputs(anyString(), (SendInteractorImpl.GetUnspentListCallBack) any());
//    }
//
//
//    @After
//    public void tearDown() {
//        RxAndroidPlugins.getInstance().reset();
//        RxJavaPlugins.getInstance().reset();
//    }
//
//}
