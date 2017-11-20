package org.qtum.wallet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.model.gson.qstore.QSearchItem;
import org.qtum.wallet.ui.fragment.qstore_by_type.QStoreByTypeInteractor;
import org.qtum.wallet.ui.fragment.qstore_by_type.QStoreByTypePresenterImpl;
import org.qtum.wallet.ui.fragment.qstore_by_type.QStoreByTypeView;

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
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class QStoreByTypePresenterTest {

    @Mock
    private QStoreByTypeView view;
    @Mock
    private QStoreByTypeInteractor interactor;
    private QStoreByTypePresenterImpl presenter;

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

        presenter = new QStoreByTypePresenterImpl(view, interactor);
    }

    @Test
    public void searchItems_Error() {
        when(interactor.searchContractsObservable(anyInt(), anyString(), anyString(), anyBoolean()))
                .thenReturn(Observable.<List<QSearchItem>>error(new Throwable("Search Contracts Exception")));

        presenter.searchItems("tag", true);

        verify(view, never()).setSearchResult(anyList());
    }

    @Test
    public void searchItems_Success() {
        when(interactor.searchContractsObservable(anyInt(), anyString(), anyString(), anyBoolean()))
                .thenReturn(Observable.just(Arrays.asList(new QSearchItem(), new QSearchItem())));

        presenter.searchItems("tag", true);

        verify(view, times(1)).setSearchResult(anyList());
    }

    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().reset();
    }

}
