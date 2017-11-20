package org.qtum.wallet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.ui.fragment.wallet_main_fragment.WalletMainInteractor;
import org.qtum.wallet.ui.fragment.wallet_main_fragment.WalletMainPresenterImpl;
import org.qtum.wallet.ui.fragment.wallet_main_fragment.WalletMainView;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class WalletMainPresenterTest {

    @Mock
    private WalletMainView view;
    @Mock
    private WalletMainInteractor interactor;
    private WalletMainPresenterImpl presenter;

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

        presenter = new WalletMainPresenterImpl(view, interactor);
    }

    @Test
    public void checkOtherTokens_Success() {
        when(interactor.getTokensObservable())
                .thenReturn(Observable.just(Arrays.asList(new Token(), new Token())));

        presenter.checkOtherTokens();

        verify(view, times(1)).showOtherTokens(anyBoolean());
    }

    @Test
    public void checkOtherTokens_Error() {
        when(interactor.getTokensObservable())
                .thenReturn(Observable.<List<Token>>error(new Throwable("Tokens error")));

        presenter.checkOtherTokens();

        verify(view, never()).showOtherTokens(anyBoolean());
    }


    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().reset();
    }
}
