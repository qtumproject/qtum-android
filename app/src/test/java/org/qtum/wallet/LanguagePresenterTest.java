package org.qtum.wallet;


import android.util.Pair;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.datastorage.listeners.LanguageChangeListener;
import org.qtum.wallet.ui.fragment.language_fragment.LanguageInteractor;
import org.qtum.wallet.ui.fragment.language_fragment.LanguagePresenterImpl;
import org.qtum.wallet.ui.fragment.language_fragment.LanguageView;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LanguagePresenterTest {

    @Mock
    LanguageView view;
    @Mock
    LanguageInteractor interactor;
    LanguagePresenterImpl presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        presenter = new LanguagePresenterImpl(view, interactor);
    }

    private List<Pair<String, String>> TEST_LANGUAGES = Arrays.asList(new Pair<String, String>("test1", "test1"), new Pair<String, String>("test2", "test2"));

    @Test
    public void initialize_test() {
        when(interactor.getLanguagesList()).thenReturn(TEST_LANGUAGES);
        presenter.initializeViews();
        verify(view, times(1)).setUpLanguagesList(TEST_LANGUAGES);
    }

    @Test
    public void removeLanguageListener_test() {
        presenter.onDestroyView();
        verify(interactor, times(1)).removeLanguageListener((LanguageChangeListener) any());
    }

}