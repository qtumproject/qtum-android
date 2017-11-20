package org.qtum.wallet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.ui.fragment.subscribe_tokens_fragment.SubscribeTokensInteractor;
import org.qtum.wallet.ui.fragment.subscribe_tokens_fragment.SubscribeTokensPresenterImpl;
import org.qtum.wallet.ui.fragment.subscribe_tokens_fragment.SubscribeTokensView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class SubscribeTokensPresenterTest {

    @Mock
    private SubscribeTokensView view;
    @Mock
    private SubscribeTokensInteractor interactor;
    private SubscribeTokensPresenterImpl presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        presenter = new SubscribeTokensPresenterImpl(view, interactor);
    }

    private final List<Token> TEST_EMPTY_TOKENS = Collections.emptyList();

    @Test
    public void initialize_EmptyTokens() {
        when(interactor.getTokenList())
                .thenReturn(TEST_EMPTY_TOKENS);

        presenter.initializeViews();

        verify(view, times(1)).setPlaceHolder();
        verify(view, never()).setTokenList(anyList());
    }

    private final List<Token> TEST_TOKENS = Arrays.asList(new Token(true, true), new Token(true, true));

    @Test
    public void initialize_Success() {
        when(interactor.getTokenList())
                .thenReturn(TEST_TOKENS);

        presenter.initializeViews();

        verify(view, times(1)).setTokenList(anyList());
        verify(view, never()).setPlaceHolder();
    }

    @Test
    public void saveTokens_Success() {
        presenter.saveTokens(TEST_TOKENS);

        verify(interactor, times(1)).saveTokenList(anyList());
    }

    @Test
    public void saveTokens_NoTokens() {
        presenter.saveTokens(null);

        verify(interactor, never()).saveTokenList(anyList());
    }
}
