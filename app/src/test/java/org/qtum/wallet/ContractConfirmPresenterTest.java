package org.qtum.wallet;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.model.contract.ContractMethod;
import org.qtum.wallet.model.contract.ContractMethodParameter;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.contract_confirm_fragment.ContractConfirmInteractor;
import org.qtum.wallet.ui.fragment.contract_confirm_fragment.ContractConfirmPresenterImpl;
import org.qtum.wallet.ui.fragment.contract_confirm_fragment.ContractConfirmView;

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
        when(interactor.createAbiConstructParams(TEST_PARAMETRS,uiid)).thenReturn(Observable.<String>error(new Throwable()));//thenReturn(Observable.just(anyString()));
        presenter.setContractMethodParameterList(TEST_PARAMETRS);
        presenter.onConfirmContract(uiid,gas_limit,gas_price,fee);
        verify(view, times(1)).setAlertDialog(anyInt(),anyString(),anyString(),(BaseFragment.PopUpType) any());

    }


}
