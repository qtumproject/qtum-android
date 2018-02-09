package org.qtum.wallet;

import org.bitcoinj.crypto.DeterministicKey;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.ui.fragment.addresses_fragment.AddressesInteractor;
import org.qtum.wallet.ui.fragment.addresses_fragment.AddressesPresenterImpl;
import org.qtum.wallet.ui.fragment.addresses_fragment.AddressesView;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class AddressesPresenterTest {

    @Mock
    AddressesView view;
    @Mock
    AddressesInteractor interactor;
    AddressesPresenterImpl presenter;

    @Mock
    private List<DeterministicKey> deterministicKeyList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        presenter = new AddressesPresenterImpl(view, interactor);
    }

    @Test
    public void updateAddressesList_Success() {
        when(interactor.getKeyList()).thenReturn(deterministicKeyList);
        presenter.onViewCreated();
        verify(view, times(1)).updateAddressList(deterministicKeyList);
    }

}
