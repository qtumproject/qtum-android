package org.qtum.wallet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.wallet_fragment.WalletInteractor;
import org.qtum.wallet.ui.fragment.wallet_fragment.WalletInteractorImpl;
import org.qtum.wallet.ui.fragment.wallet_fragment.WalletPresenterImpl;
import org.qtum.wallet.ui.fragment.wallet_fragment.WalletView;

import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class WalletPresenterTest {

    @Mock
    private WalletView view;
    @Mock
    private WalletInteractor interactor;
    private WalletPresenterImpl presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        presenter = new WalletPresenterImpl(view, interactor);
    }


    @Test
    public void onRefresh_NetworkConnected() {
        presenter.setNetworkConnectedFlag(true);
        presenter.onRefresh();

        verify(view, times(1)).startRefreshAnimation();
        verify(interactor, times(1)).getHistoryList(anyInt(), anyInt());
        verify(view, never()).setAlertDialog(anyInt(), anyInt(), anyInt(), (BaseFragment.PopUpType) any());
    }

    @Test
    public void onRefresh_NoNetworkConnection() {
        presenter.setNetworkConnectedFlag(false);
        presenter.onRefresh();

        verify(view, times(1)).setAlertDialog(anyInt(), anyInt(), anyInt(), (BaseFragment.PopUpType) any());
        verify(view, times(1)).stopRefreshRecyclerAnimation();
        verify(view, never()).startRefreshAnimation();
        verify(interactor, never()).getHistoryList(anyInt(), anyInt());
    }

    @Test
    public void openTransactionFragment() {
        presenter.openTransactionFragment(0);

        verify(view, times(1)).openTransactionsFragment(anyInt());
    }

    @Test
    public void onLastItem() {
        when(interactor.getHistoryList())
                .thenReturn(Arrays.asList(new History(), new History()));
        when(interactor.getTotalHistoryItem())
                .thenReturn(1);

        presenter.onLastItem(0);

        verify(view, times(1)).loadNewHistory();
        verify(interactor, times(1)).getHistoryList(anyInt(), anyInt());
    }

    @Test
    public void networkStateChanged_Connected() {
        presenter.onNetworkStateChanged(true);

        verify(view, times(1)).startRefreshAnimation();
        verify(interactor, times(1)).getHistoryList(anyInt(), anyInt());
    }

    @Test
    public void networkStateChanged_Disconnected() {
        presenter.onNetworkStateChanged(false);

        verify(view, never()).startRefreshAnimation();
    }

    private static final History TEST_HISTORY = new History();
    private static final History TEST_HISTORY_WITH_BLOCK_TIME = new History(Long.valueOf("12"));

    @Test
    public void onNewHistory_BlockTime_NewHistory() {
        when(interactor.setHistory((History) any()))
                .thenReturn(null);

        presenter.onNewHistory(TEST_HISTORY_WITH_BLOCK_TIME);

        verify(view, times(1)).notifyNewHistory();
        verify(view, never()).notifyConfirmHistory(anyInt());
        verify(interactor, never()).addToHistoryList((History) any());

    }

    @Test
    public void onNewHistory_BlockTime_ConfirmHistory() {
        when(interactor.setHistory((History) any()))
                .thenReturn(1);

        presenter.onNewHistory(TEST_HISTORY_WITH_BLOCK_TIME);

        verify(view, times(1)).notifyConfirmHistory(anyInt());

        verify(view, never()).notifyNewHistory();
        verify(interactor, never()).addToHistoryList((History) any());
    }

    @Test
    public void onNewHistory_NoBlockTime() {
        presenter.onNewHistory(TEST_HISTORY);

        verify(view, times(1)).notifyNewHistory();
        verify(interactor, times(1)).addToHistoryList((History) any());

        verify(view, never()).notifyConfirmHistory(anyInt());
    }


}
