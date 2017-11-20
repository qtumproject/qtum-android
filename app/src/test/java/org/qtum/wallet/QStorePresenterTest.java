package org.qtum.wallet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.model.gson.qstore.QstoreItem;
import org.qtum.wallet.ui.fragment.qstore.QStoreInteractor;
import org.qtum.wallet.ui.fragment.qstore.QStorePresenterImpl;
import org.qtum.wallet.ui.fragment.qstore.QStoreView;

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

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class QStorePresenterTest {

    @Mock
    private QStoreView view;
    @Mock
    private QStoreInteractor interactor;
    private QStorePresenterImpl presenter;

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

        presenter = new QStorePresenterImpl(view, interactor);
    }

    @Test
    public void initialize_TrendingError() {
        when(interactor.getTrendingNowObservable())
                .thenReturn(Observable.<List<QstoreItem>>error(new Throwable("Getting Trending now error")));

        presenter.onViewCreated();

        verify(view, never()).setCategories(anyList());
        assertThat(presenter.getCategories())
                .isEmpty();
    }

    private final List<QstoreItem> TEST_TRENDING_CATEGORIES = Arrays.asList(new QstoreItem(), new QstoreItem());
    private final List<QstoreItem> TEST_WHATS_NEW_CATEGORIES = Arrays.asList(new QstoreItem(), new QstoreItem());

    @Test
    public void initialize_TrendingSuccess_WhatsNewError() {
        when(interactor.getTrendingNowObservable())
                .thenReturn(Observable.just(TEST_TRENDING_CATEGORIES));
        when(interactor.getTrendingString())
                .thenReturn("Trending");
        when(interactor.getWhatsNewObservable())
                .thenReturn(Observable.<List<QstoreItem>>error(new Throwable("Getting Whats New error")));

        presenter.onViewCreated();

        verify(view, times(1)).setCategories(anyList());
        assertThat(presenter.getCategories())
                .hasSize(1);
    }

    @Test
    public void initialize_TrendingSuccess_WhatsNewSuccess() {
        when(interactor.getTrendingNowObservable())
                .thenReturn(Observable.just(TEST_TRENDING_CATEGORIES));
        when(interactor.getTrendingString())
                .thenReturn("Trending");
        when(interactor.getWhatsNewObservable())
                .thenReturn(Observable.just(TEST_WHATS_NEW_CATEGORIES));
        when(interactor.getWhatsNewString())
                .thenReturn("Whats New");

        presenter.onViewCreated();

        verify(view, times(2)).setCategories(anyList());
        assertThat(presenter.getCategories())
                .hasSize(2);
    }


    @Test
    public void initialize_TrendingEmpty_WhatsNewSuccess() {
        when(interactor.getTrendingNowObservable())
                .thenReturn(Observable.just(Collections.<QstoreItem>emptyList()));
        when(interactor.getWhatsNewObservable())
                .thenReturn(Observable.just(TEST_WHATS_NEW_CATEGORIES));
        when(interactor.getWhatsNewString())
                .thenReturn("Whats New");

        presenter.onViewCreated();

        verify(view, times(1)).setCategories(anyList());
        verify(interactor, never()).getTrendingString();
        assertThat(presenter.getCategories())
                .hasSize(1);
    }

    @Test
    public void initialize_TrendingSuccess_WhatsNewEmpty() {
        when(interactor.getTrendingNowObservable())
                .thenReturn(Observable.just(TEST_TRENDING_CATEGORIES));
        when(interactor.getTrendingString())
                .thenReturn("Trending");
        when(interactor.getWhatsNewObservable())
                .thenReturn(Observable.just(Collections.<QstoreItem>emptyList()));

        presenter.onViewCreated();

        verify(view, times(1)).setCategories(anyList());
        verify(interactor, never()).getWhatsNewString();
        assertThat(presenter.getCategories())
                .hasSize(1);
    }

    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().reset();
    }

}
