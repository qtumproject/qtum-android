package org.qtum.wallet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.ui.fragment.touch_id_preference_fragment.TouchIDInterractor;
import org.qtum.wallet.ui.fragment.touch_id_preference_fragment.TouchIDPreferencePresenterImpl;
import org.qtum.wallet.ui.fragment.touch_id_preference_fragment.TouchIDPreferenceView;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class TouchIDPreferencesPresenterTest {

    @Mock
    private TouchIDPreferenceView view;
    @Mock
    private TouchIDInterractor interractor;
    private TouchIDPreferencePresenterImpl presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        presenter = new TouchIDPreferencePresenterImpl(view, interractor);
    }

    @Test
    public void onEnableTouchIdClick() {
        presenter.onEnableTouchIdClick();

        verify(interractor, times(1)).saveTouchIDEnabled();
    }


}
