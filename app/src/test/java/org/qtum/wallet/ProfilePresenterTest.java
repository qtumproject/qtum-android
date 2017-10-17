package org.qtum.wallet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.datastorage.listeners.LanguageChangeListener;
import org.qtum.wallet.ui.fragment.profile_fragment.ProfileInteractor;
import org.qtum.wallet.ui.fragment.profile_fragment.ProfilePresenterImpl;
import org.qtum.wallet.ui.fragment.profile_fragment.ProfileView;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by drevnitskaya on 10.10.17.
 */

public class ProfilePresenterTest {

    @Mock
    private ProfileView view;
    @Mock
    private ProfileInteractor interactor;
    private ProfilePresenterImpl presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void initialize() {
        when(view.checkAvailabilityTouchId())
                .thenReturn(false);

        presenter = new ProfilePresenterImpl(view, interactor);

        verify(interactor, never()).isTouchIdEnable();
        assertThat(presenter.getSettingsData())
                .isNotEmpty();
    }

    @Test
    public void clearWallet() {
        when(view.checkAvailabilityTouchId())
                .thenReturn(true);
        when(interactor.isTouchIdEnable())
                .thenReturn(true);

        presenter = new ProfilePresenterImpl(view, interactor);

        presenter.clearWallet();

        verify(interactor, times(1)).clearWallet();
    }

    @Test
    public void onTouchIdSwitched() {
        when(view.checkAvailabilityTouchId())
                .thenReturn(true);
        when(interactor.isTouchIdEnable())
                .thenReturn(true);

        presenter = new ProfilePresenterImpl(view, interactor);

        presenter.onTouchIdSwitched(true);

        verify(interactor, times(1)).saveTouchIdEnable(anyBoolean());
    }

    @Test
    public void setupLanguageChangeListener() {
        when(view.checkAvailabilityTouchId())
                .thenReturn(true);
        when(interactor.isTouchIdEnable())
                .thenReturn(true);

        presenter = new ProfilePresenterImpl(view, interactor);

        presenter.setupLanguageChangeListener(new LanguageChangeListener() {
            @Override
            public void onLanguageChange() {

            }
        });

        verify(interactor, times(1)).setupLanguageChangeListener((LanguageChangeListener) any());
    }

    @Test
    public void removeLanguageListener() {
        when(view.checkAvailabilityTouchId())
                .thenReturn(true);
        when(interactor.isTouchIdEnable())
                .thenReturn(true);

        presenter = new ProfilePresenterImpl(view, interactor);

        presenter.removeLanguageListener(new LanguageChangeListener() {
            @Override
            public void onLanguageChange() {

            }
        });

        verify(interactor, times(1)).removeLanguageListener((LanguageChangeListener) any());
    }
}
