package org.qtum.wallet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.ui.fragment.wallet_fragment.WalletInteractor;
import org.qtum.wallet.ui.fragment.wallet_fragment.WalletPresenterImpl;
import org.qtum.wallet.ui.fragment.wallet_fragment.WalletView;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by drevnitskaya on 04.10.17.
 */

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
    public void notifyHeader() {
        presenter.notifyHeader();

        verify(interactor, times(1)).getAddress();
        verify(view, times(1)).updatePubKey(anyString());
    }

}
