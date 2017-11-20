package org.qtum.wallet;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.model.Version;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.about_fragment.AboutInteractor;
import org.qtum.wallet.ui.fragment.about_fragment.AboutPresenterImpl;
import org.qtum.wallet.ui.fragment.about_fragment.AboutView;


import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class AboutPresenterTest {

    private final static int TEST_CODE_VERSION = 0;
    private final static String TEST_VERSION = "TEST_VERSION";

    @Mock
    private AboutView view;
    @Mock
    private AboutInteractor interactor;
    private AboutPresenterImpl presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        presenter = new AboutPresenterImpl(view, interactor);
    }
//TODO: in process
//    @Test
//    public void updateVersion_Success() throws Exception {
//        when(interactor.getCodeVersion()).thenReturn(TEST_CODE_VERSION);
//        when(interactor.getVersion()).thenReturn(TEST_VERSION);
//        presenter.initializeViews();
//        verify(view, times(1)).updateVersion(new Version(TEST_VERSION, TEST_CODE_VERSION));
//    }

    @Test
    public void updateVersion_versionError() throws Exception {
        when(interactor.getCodeVersion()).thenReturn(TEST_CODE_VERSION);
        when(interactor.getVersion()).thenThrow(new Exception());
        presenter.initializeViews();
        verify(view, times(1)).setAlertDialog(anyInt(),anyInt(),(BaseFragment.PopUpType) any());
    }

    @Test
    public void updateVersion_codeVersionError() throws Exception {
        when(interactor.getCodeVersion()).thenThrow(new Exception());
        when(interactor.getVersion()).thenReturn(TEST_VERSION);
        presenter.initializeViews();
        verify(view, times(1)).setAlertDialog(anyInt(),anyInt(),(BaseFragment.PopUpType) any());
    }


}