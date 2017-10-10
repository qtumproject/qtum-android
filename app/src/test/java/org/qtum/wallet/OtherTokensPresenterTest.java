package org.qtum.wallet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.ui.fragment.other_tokens.OtherTokensInteractor;
import org.qtum.wallet.ui.fragment.other_tokens.OtherTokensPresenterImpl;
import org.qtum.wallet.ui.fragment.other_tokens.OtherTokensView;

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

import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by drevnitskaya on 10.10.17.
 */

public class OtherTokensPresenterTest {
    @Mock
    private OtherTokensView view;
    @Mock
    private OtherTokensInteractor interactor;
    private OtherTokensPresenterImpl presenter;

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

        presenter = new OtherTokensPresenterImpl(view, interactor);
    }

    @Test
    public void notifyNewToken_Error() {
        when(interactor.getTokenObservable())
                .thenReturn(Observable.<List<Token>>error(new Throwable("Getting Token error")));

        presenter.notifyNewToken();

        verify(view, never()).setTokensData(anyList());
    }

    @Test
    public void notifyNewToken_EmptyTokens() {
        when(interactor.getTokenObservable())
                .thenReturn(Observable.just(Collections.<Token>emptyList()));

        presenter.notifyNewToken();

        verify(view, never()).setTokensData(anyList());
    }

    @Test
    public void notifyNewToken_Success() {
        when(interactor.getTokenObservable())
                .thenReturn(Observable.just(Arrays.asList(new Token(), new Token())));

        presenter.notifyNewToken();

        verify(view, times(1)).setTokensData(anyList());
    }

    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().reset();
    }

}
