package org.qtum.wallet;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.model.gson.ContractParams;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.watch_token_fragment.WatchTokenInteractor;
import org.qtum.wallet.ui.fragment.watch_token_fragment.WatchTokenPresenterImpl;
import org.qtum.wallet.ui.fragment.watch_token_fragment.WatchTokenView;

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
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WatchTokenPresenterTest {

    @Mock
    private WatchTokenView view;
    @Mock
    private WatchTokenInteractor interactor;
    private WatchTokenPresenterImpl presenter;

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
        presenter = new WatchTokenPresenterImpl(view, interactor);
    }

    private final static String TEST_ADDRESS = "test";
    private final static String TEST_CONTRACT_NAME = "test_name";
    private final static ContractParams TEST_CONTRACT_PARAMS = new ContractParams();

    @Test
    public void onContractAddressEntered_test(){
        TEST_CONTRACT_PARAMS.setName(TEST_CONTRACT_NAME);
        when(interactor.getContractParams(TEST_ADDRESS)).thenReturn(Observable.just(TEST_CONTRACT_PARAMS));
        presenter.onContractAddressEntered(TEST_ADDRESS);
        verify(view,times(1)).setContractName(TEST_CONTRACT_NAME);
    }

    private final static String TEST_NAME = "test_name";

    private static final List<Contract> TEST_VALID_CONTRACTS = Arrays.asList(new Contract("some address"),
            new Contract("other address"));

    @Test
    public void onOkClick_Success(){
        when(interactor.isValidContractAddress(TEST_ADDRESS)).thenReturn(true);
        when(interactor.getContracts()).thenReturn(TEST_VALID_CONTRACTS);
        presenter.onOkClick(TEST_NAME, TEST_ADDRESS);
        verify(view, times(1)).setAlertDialog(anyInt(), anyString(), anyInt(), eq(BaseFragment.PopUpType.confirm), (BaseFragment.AlertDialogCallBack) any());
    }

    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().reset();
    }

}
