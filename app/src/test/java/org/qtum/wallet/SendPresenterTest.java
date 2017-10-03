package org.qtum.wallet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.ui.fragment.send_fragment.SendInteractor;
import org.qtum.wallet.ui.fragment.send_fragment.SendPresenterImpl;
import org.qtum.wallet.ui.fragment.send_fragment.SendView;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Created by drevnitskaya on 03.10.17.
 */

public class SendPresenterTest {

    private static final List<Token> TEST_LIST_TOKENS = Arrays.asList(new Token(), new Token());

    @Mock
    private SendView view;
    @Mock
    private SendInteractor interactor;
    private SendPresenterImpl presenter;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        presenter = new SendPresenterImpl(view, interactor);
    }

    @Test
    public void initialize() {
        when(interactor.getTokenList())
                .thenReturn(TEST_LIST_TOKENS);

        presenter.initializeViews();


    }

}
