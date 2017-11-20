package org.qtum.wallet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.ui.fragment.start_page_fragment.StartPageInteractor;
import org.qtum.wallet.ui.fragment.start_page_fragment.StartPagePresenterImpl;
import org.qtum.wallet.ui.fragment.start_page_fragment.StartPageView;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class StartPagePresenterTest {

    @Mock
    private StartPageView view;
    @Mock
    private StartPageInteractor interactor;
    private StartPagePresenterImpl presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        presenter = new StartPagePresenterImpl(view, interactor);
    }

    @Test
    public void initialize_KeyGenerated() {
        when(interactor.getGeneratedKey())
                .thenReturn(true);

        presenter.initializeViews();

        verify(view, never()).hideLoginButton();
    }

    @Test
    public void initialize_KeyIsNotGenerated() {
        when(interactor.getGeneratedKey())
                .thenReturn(false);

        presenter.initializeViews();

        verify(view, times(1)).hideLoginButton();
    }

    @Test
    public void clearWallet() {
        presenter.clearWallet();
        verify(interactor, times(1)).clearWallet();
    }
}
