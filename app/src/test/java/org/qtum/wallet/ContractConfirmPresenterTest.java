package org.qtum.wallet;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.model.contract.ContractMethodParameter;
import org.qtum.wallet.model.gson.SendRawTransactionRequest;
import org.qtum.wallet.model.gson.SendRawTransactionResponse;
import org.qtum.wallet.model.gson.UnspentOutput;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.contract_confirm_fragment.ContractConfirmInteractor;
import org.qtum.wallet.ui.fragment.contract_confirm_fragment.ContractConfirmPresenterImpl;
import org.qtum.wallet.ui.fragment.contract_confirm_fragment.ContractConfirmView;

import java.math.BigDecimal;
import java.util.Arrays;
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
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ContractConfirmPresenterTest {


    @Mock
    ContractConfirmView view;
    @Mock
    ContractConfirmInteractor interactor;

    ContractConfirmPresenterImpl presenter;

    private final static double minFee = 0.2;
    private final static int minGasPrice = 2;

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

        presenter = new ContractConfirmPresenterImpl(view, interactor);
    }

    @Test
    public void initializeView_test(){
        when(interactor.getMinFee()).thenReturn(minFee);
        when(interactor.getMinGasPrice()).thenReturn(minGasPrice);
        presenter.initializeViews();
        verify(view,times(1)).updateGasPrice(eq(minGasPrice),anyInt());
        verify(view,times(1)).updateFee(eq(minFee), anyDouble());
        verify(view,times(1)).updateGasLimit(anyInt(),anyInt());
    }

    private final static String uiid = "uiid";
    private final String fee = "1";
    private final int gas_limit = 2;
    private final int gas_price = 2;
    private final List<ContractMethodParameter> TEST_PARAMETRS = Arrays.asList(new ContractMethodParameter("name", "type", "value"));

    @Test
    public void onConfirm_createAbiConstructParams_Error(){
        when(interactor.createAbiConstructParams(TEST_PARAMETRS,uiid)).thenReturn(Observable.<String>error(new Throwable()));
        presenter.setContractMethodParameterList(TEST_PARAMETRS);
        presenter.onConfirmContract(uiid,gas_limit,gas_price,fee);
        verify(view, times(1)).setAlertDialog(anyInt(),anyString(),anyString(),(BaseFragment.PopUpType) any());

    }

    @Test
    public void onConfirm_getUnspentOutputsForSeveralAddresses_Error(){
        when(interactor.createAbiConstructParams(TEST_PARAMETRS,uiid)).thenReturn(Observable.just(anyString()));
        when(interactor.getUnspentOutputsForSeveralAddresses()).thenReturn(Observable.<List<UnspentOutput>>error(new Throwable()));
        presenter.setContractMethodParameterList(TEST_PARAMETRS);
        presenter.onConfirmContract(uiid,gas_limit,gas_price,fee);
        verify(view, times(1)).setAlertDialog(anyInt(),anyString(),anyString(),(BaseFragment.PopUpType) any());

    }

    private final static List<UnspentOutput> TEST_OUTPUTS = Arrays.asList(new UnspentOutput(600, true, new BigDecimal("12.0")),
            new UnspentOutput(700, true, new BigDecimal("10.0")));
    private static final String ABI_PARAMS = "abi_params";
    private static final String TEST_HASH = "test_hash";

    @Test
    public void onConfirm_sendRawTransaction_Error(){
        when(interactor.createAbiConstructParams(TEST_PARAMETRS,uiid)).thenReturn(Observable.just(ABI_PARAMS));
        when(interactor.getUnspentOutputsForSeveralAddresses()).thenReturn(Observable.just(TEST_OUTPUTS));
        when(interactor.createTransactionHash(ABI_PARAMS, TEST_OUTPUTS, gas_limit, gas_price, fee)).thenReturn(TEST_HASH);
        when(interactor.sendRawTransaction((SendRawTransactionRequest)any())).thenReturn(Observable.<SendRawTransactionResponse>error(new Throwable()));
        presenter.setContractMethodParameterList(TEST_PARAMETRS);
        presenter.onConfirmContract(uiid,gas_limit,gas_price,fee);
        verify(view, times(1)).setAlertDialog(anyInt(),anyString(),anyString(),(BaseFragment.PopUpType) any());

    }

    private final static String TX_HASH_TEST = "test_hash";

    @Test
    public void onConfirm_Success(){
        SendRawTransactionResponse sendrawTransactionResponse = mock(SendRawTransactionResponse.class);
        when(sendrawTransactionResponse.getTxid()).thenReturn(TX_HASH_TEST);
        when(interactor.createAbiConstructParams(TEST_PARAMETRS,uiid)).thenReturn(Observable.just(ABI_PARAMS));
        when(interactor.getUnspentOutputsForSeveralAddresses()).thenReturn(Observable.just(TEST_OUTPUTS));
        when(interactor.createTransactionHash(ABI_PARAMS, TEST_OUTPUTS, gas_limit, gas_price, fee)).thenReturn(TEST_HASH);
        when(interactor.sendRawTransaction((SendRawTransactionRequest)any())).thenReturn(Observable.just(sendrawTransactionResponse));
        presenter.setContractMethodParameterList(TEST_PARAMETRS);
        presenter.onConfirmContract(uiid,gas_limit,gas_price,fee);
        verify(view, times(1)).setAlertDialog(anyInt(),anyString(),anyString(),(BaseFragment.PopUpType) any(), (BaseFragment.AlertDialogCallBack)any());

    }

    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().reset();
    }

}
