package org.qtum.wallet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.history.Vin;
import org.qtum.wallet.model.gson.history.Vout;
import org.qtum.wallet.ui.fragment.transaction_fragment.TransactionInteractor;
import org.qtum.wallet.ui.fragment.transaction_fragment.TransactionPresenterImpl;
import org.qtum.wallet.ui.fragment.transaction_fragment.TransactionView;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class TransactionPresenterTest {

    private static final List<Vout> vouts = Arrays.asList(new Vout("address"), new Vout("address"));
    private static final List<Vin> vins = Arrays.asList(new Vin("address"), new Vin("address"));
    private static final History TEST_HISTORY_WITH_BLOCK_TIME = new History(Long.valueOf("12"), vouts, vins, new BigDecimal("10"), 10);
    private static final History TEST_HISTORY_WITHOUT_BLOCK_TIME = new History(vouts, vins, new BigDecimal("10"), 10);

    @Mock
    private TransactionView view;
    @Mock
    private TransactionInteractor interactor;
    private TransactionPresenterImpl presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        presenter = new TransactionPresenterImpl(view, interactor);
    }

    @Test
    public void openTransactionView_FullDate() {
        when(interactor.getHistory(anyInt()))
                .thenReturn(TEST_HISTORY_WITH_BLOCK_TIME);
        when(interactor.getFullDate(anyLong()))
                .thenReturn("full date");

        presenter.openTransactionView(0);

        verify(interactor, times(1)).getFullDate(anyLong());
        verify(view, times(1)).setUpTransactionData(anyString(), anyString(), anyList(), anyList(), anyBoolean());

        verify(interactor, never()).getUnconfirmedDate();
    }

    @Test
    public void openTransactionView_UnconfirmedDate() {
        when(interactor.getHistory(anyInt()))
                .thenReturn(TEST_HISTORY_WITHOUT_BLOCK_TIME);
        when(interactor.getFullDate(anyLong()))
                .thenReturn("full date");

        presenter.openTransactionView(0);

        verify(interactor, times(1)).getUnconfirmedDate();
        verify(view, times(1)).setUpTransactionData(anyString(), anyString(), anyList(), anyList(), anyBoolean());

        verify(interactor, never()).getFullDate(anyLong());

    }


}
