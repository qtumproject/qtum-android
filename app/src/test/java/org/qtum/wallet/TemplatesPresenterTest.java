package org.qtum.wallet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.ui.fragment.templates_fragment.TemplatesInteractor;
import org.qtum.wallet.ui.fragment.templates_fragment.TemplatesPresenterImpl;
import org.qtum.wallet.ui.fragment.templates_fragment.TemplatesView;

import java.util.Arrays;

import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by drevnitskaya on 06.10.17.
 */

public class TemplatesPresenterTest {

    @Mock
    private TemplatesView view;
    @Mock
    private TemplatesInteractor interactor;
    private TemplatesPresenterImpl presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        presenter = new TemplatesPresenterImpl(view, interactor);
    }

    @Test
    public void initialize() {
        when(interactor.getContractTemplates())
                .thenReturn(Arrays.asList(new ContractTemplate(), new ContractTemplate()));
        when(interactor.compareDates(anyString(), anyString()))
                .thenReturn(1);

        presenter.initializeViews();

        verify(view, times(1)).setUpTemplateList(anyList());
    }


}
