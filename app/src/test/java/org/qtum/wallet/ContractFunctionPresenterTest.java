package org.qtum.wallet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.model.contract.ContractMethod;
import org.qtum.wallet.model.contract.ContractMethodParameter;
import org.qtum.wallet.model.gson.SendRawTransactionResponse;
import org.qtum.wallet.model.gson.UnspentOutput;
import org.qtum.wallet.model.gson.call_smart_contract_response.CallSmartContractResponse;
import org.qtum.wallet.model.gson.call_smart_contract_response.Item;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.contract_function_fragment.ContractFunctionInteractor;
import org.qtum.wallet.ui.fragment.contract_function_fragment.ContractFunctionInteractorImpl;
import org.qtum.wallet.ui.fragment.contract_function_fragment.ContractFunctionPresenterImpl;
import org.qtum.wallet.ui.fragment.contract_function_fragment.ContractFunctionView;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by drevnitskaya on 09.10.17.
 */

public class ContractFunctionPresenterTest {

    @Mock
    private ContractFunctionView view;
    @Mock
    private ContractFunctionInteractor interactor;
    private ContractFunctionPresenterImpl presenter;

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

        presenter = new ContractFunctionPresenterImpl(view, interactor);
    }

    private final String TEST_UUID = "uuid";
    private final BigDecimal TEST_FEE_DOUBLE = new BigDecimal("2.0");
    private final int TEST_MIN_GAS_PRISE = 2;

    @Test
    public void initialize_EmptyContracts() {
        when(view.getContractTemplateUiid())
                .thenReturn(TEST_UUID);
        when(interactor.getContractMethod(anyString()))
                .thenReturn(Collections.<ContractMethod>emptyList());

        when(interactor.getFeePerKb())
                .thenReturn(TEST_FEE_DOUBLE);
        when(interactor.getMinGasPrice())
                .thenReturn(TEST_MIN_GAS_PRISE);

        presenter.initializeViews();

        verify(view, never()).getMethodName();
        verify(view, never()).setUpParameterList(anyList());

        verify(view, times(1)).updateFee(anyDouble(), anyDouble());
        verify(view, times(1)).updateGasPrice(anyInt(), anyInt());
        verify(view, times(1)).updateGasLimit(anyInt(), anyInt());
    }

    private final String TEST_METHOD_NAME = "Method name";
    private final List<ContractMethod> TEST_METHODS = Arrays.asList(new ContractMethod("some name"), new ContractMethod(TEST_METHOD_NAME));

    @Test
    public void initialize_WithContractMethod() {
        when(view.getContractTemplateUiid())
                .thenReturn(TEST_UUID);
        when(view.getMethodName())
                .thenReturn(TEST_METHOD_NAME);
        when(interactor.getContractMethod(anyString()))
                .thenReturn(TEST_METHODS);

        when(interactor.getFeePerKb())
                .thenReturn(TEST_FEE_DOUBLE);
        when(interactor.getMinGasPrice())
                .thenReturn(TEST_MIN_GAS_PRISE);

        presenter.initializeViews();

        verify(view, times(1)).setUpParameterList(anyList());
        verify(view, times(1)).updateFee(anyDouble(), anyDouble());
        verify(view, times(1)).updateGasPrice(anyInt(), anyInt());
        verify(view, times(1)).updateGasLimit(anyInt(), anyInt());
    }

    private final List<ContractMethodParameter> TEST_PARAMETRS = Arrays.asList(new ContractMethodParameter("name", "type", "value"));

    @Test
    public void onCallClick_Error() {
        when(interactor.callSmartContractObservable(anyString(), anyList(), (Contract)any()))
                .thenReturn(Observable.error(new Throwable("Error")));

        presenter.onCallClick(TEST_PARAMETRS, "address", "fee", 2, 2, "method name");

        verify(view, never()).dismissProgressDialog();
    }

    private final String TEST_EXCEPTED_NOT_NONE = "excepted";
    private final String TEST_EXCEPTED_NONE = "None";
    private final List<Item> TEST_WRONG_ITEMS = Arrays.asList(new Item(TEST_EXCEPTED_NOT_NONE), new Item(TEST_EXCEPTED_NONE));
    private final ContractFunctionInteractorImpl.CallSmartContractRespWrapper TEST_WRONG_RESP_WRAPPER =
            new ContractFunctionInteractorImpl.CallSmartContractRespWrapper("code", new CallSmartContractResponse(TEST_WRONG_ITEMS));

    @Test
    public void onCallClick_ItemExceptedNotNoneError() {
        when(interactor.callSmartContractObservable(anyString(), anyList(), (Contract)any()))
                .thenReturn(Observable.just(TEST_WRONG_RESP_WRAPPER));

        presenter.onCallClick(TEST_PARAMETRS, "address", "fee", 2, 2, "method name");

        verify(view, times(1)).setAlertDialog(anyInt(), anyString(), anyString(), (BaseFragment.PopUpType) any());
        verify(view, never()).dismissProgressDialog();
    }

    private final int TEST_PARAM_GAS_LIMIT = 2;
    private final int TEST_ITEM_USED_GAS_LIMIT = 4;
    private final List<Item> TEST_WRONG_GAS_ITEMS = Arrays.asList(new Item(TEST_EXCEPTED_NONE, TEST_ITEM_USED_GAS_LIMIT),
            new Item(TEST_EXCEPTED_NOT_NONE, TEST_ITEM_USED_GAS_LIMIT));
    private final ContractFunctionInteractorImpl.CallSmartContractRespWrapper TEST_WRONG_GAS_RESP_WRAPPER =
            new ContractFunctionInteractorImpl.CallSmartContractRespWrapper("abi params", new CallSmartContractResponse(TEST_WRONG_GAS_ITEMS));

    @Test
    public void onCallClick_WrongGasUsedError() {
        when(interactor.callSmartContractObservable(anyString(), anyList(), (Contract)any()))
                .thenReturn(Observable.just(TEST_WRONG_GAS_RESP_WRAPPER));

        presenter.onCallClick(TEST_PARAMETRS, "address", "fee", TEST_PARAM_GAS_LIMIT, 2, "method name");

        verify(view, times(1)).setAlertDialog(anyInt(), anyString(), anyString(), (BaseFragment.PopUpType) any());
        verify(view, never()).dismissProgressDialog();
    }

    private final List<Item> TEST_RIGHT_ITEMS = Arrays.asList(new Item(TEST_EXCEPTED_NONE, 1),
            new Item(TEST_EXCEPTED_NOT_NONE, 1));
    private final ContractFunctionInteractorImpl.CallSmartContractRespWrapper TEST_RIGHT_RESP_WRAPPER =
            new ContractFunctionInteractorImpl.CallSmartContractRespWrapper("code", new CallSmartContractResponse(TEST_RIGHT_ITEMS));

    @Test
    public void onCallClick_ValidSmartContractResp_CreateTxError() {
        when(interactor.callSmartContractObservable(anyString(), anyList(), (Contract)any()))
                .thenReturn(Observable.just(TEST_RIGHT_RESP_WRAPPER));
        when(interactor.getFeePerKb())
                .thenReturn(new BigDecimal("12"));
        when(interactor.unspentOutputsForSeveralAddrObservable())
                .thenReturn(Observable.<List<UnspentOutput>>error(new Throwable("Unspent outputs error")));

        presenter.onCallClick(TEST_PARAMETRS, "address", "fee", TEST_PARAM_GAS_LIMIT, 2, "method name");

        verify(interactor, times(1)).unspentOutputsForSeveralAddrObservable();
        verify(view, times(1)).setAlertDialog(anyInt(), anyString(), anyString(), (BaseFragment.PopUpType) any());
        verify(view, never()).dismissProgressDialog();
    }

    private final List<UnspentOutput> TEST_OUTPUTS = Arrays.asList(new UnspentOutput(600, true, new BigDecimal("12.0")),
            new UnspentOutput(700, true, new BigDecimal("10.0")));

    @Test
    public void onCallClick_CreateTxSuccess_SendTxError() {
        when(interactor.callSmartContractObservable(anyString(), anyList(), (Contract)any()))
                .thenReturn(Observable.just(TEST_RIGHT_RESP_WRAPPER));
        when(interactor.getFeePerKb())
                .thenReturn(new BigDecimal("12"));

        when(interactor.unspentOutputsForSeveralAddrObservable())
                .thenReturn(Observable.just(TEST_OUTPUTS));

        when(interactor.createTransactionHash(anyString(), anyList(), anyInt(), anyInt(), (BigDecimal) any(), anyString(), anyString()))
                .thenReturn("hash");
        when(interactor.sendRawTransactionObservable(anyString()))
                .thenReturn(Observable.<SendRawTransactionResponse>error(new Throwable("Send raw transaction error")));

        presenter.onCallClick(TEST_PARAMETRS, "address", "fee", TEST_PARAM_GAS_LIMIT, 2, "method name");

        verify(interactor, times(1)).unspentOutputsForSeveralAddrObservable();
        verify(view, times(1)).setAlertDialog(anyInt(), anyString(), anyString(), (BaseFragment.PopUpType) any());

        verify(view, never()).dismissProgressDialog();
    }

    @Test
    public void onCallClick_CreateTxSuccess_SendTxSuccess() {
        when(interactor.callSmartContractObservable(anyString(), anyList(), (Contract)any()))
                .thenReturn(Observable.just(TEST_RIGHT_RESP_WRAPPER));
        when(interactor.getFeePerKb())
                .thenReturn(new BigDecimal("12"));

        when(interactor.unspentOutputsForSeveralAddrObservable())
                .thenReturn(Observable.just(TEST_OUTPUTS));

        when(interactor.createTransactionHash(anyString(), anyList(), anyInt(), anyInt(), (BigDecimal) any(), anyString(), anyString()))
                .thenReturn("hash");
        when(interactor.sendRawTransactionObservable(anyString()))
                .thenReturn(Observable.just(new SendRawTransactionResponse()));

        presenter.onCallClick(TEST_PARAMETRS, "address", "fee", TEST_PARAM_GAS_LIMIT, 2, "method name");

        verify(view, times(1)).dismissProgressDialog();
        verify(view, never()).setAlertDialog(anyInt(), anyString(), anyString(), (BaseFragment.PopUpType) any());
    }

    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().reset();
    }

}
