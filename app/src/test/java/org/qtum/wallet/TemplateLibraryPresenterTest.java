package org.qtum.wallet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.ui.fragment.template_library_fragment.TemplateLibraryInteractor;
import org.qtum.wallet.ui.fragment.template_library_fragment.TemplateLibraryPresenterImpl;
import org.qtum.wallet.ui.fragment.template_library_fragment.TemplateLibraryView;

import java.util.Arrays;

import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by drevnitskaya on 06.10.17.
 */

public class TemplateLibraryPresenterTest {

    @Mock
    private TemplateLibraryView view;
    @Mock
    private TemplateLibraryInteractor interactor;
    private TemplateLibraryPresenterImpl presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        presenter = new TemplateLibraryPresenterImpl(view, interactor);
    }

    @Test
    public void initialize() {
        when(interactor.getContactTemplates())
                .thenReturn(Arrays.asList(new ContractTemplate(), new ContractTemplate()));
        when(view.isTokenLibrary())
                .thenReturn(true);
        when(interactor.compareDates(anyString(), anyString()))
                .thenReturn(1);

        presenter.initializeViews();

        verify(view, times(1)).setUpTemplateList(anyList());
    }

}
