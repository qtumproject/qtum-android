package org.qtum.wallet;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.model.contract.ContractMethod;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.contract_management_fragment.ContractManagementInteractor;
import org.qtum.wallet.ui.fragment.contract_management_fragment.ContractManagementPresenterImpl;
import org.qtum.wallet.ui.fragment.contract_management_fragment.ContractManagementView;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ContractManagementPresenterTest {

    @Mock
    ContractManagementView view;
    @Mock
    ContractManagementInteractor interactor;
    ContractManagementPresenterImpl presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        presenter = new ContractManagementPresenterImpl(view, interactor);
    }

    private final String TEST_UIID = "test_uiid";
    private final String TEST_METHOD_NAME = "Method name";
    private final List<ContractMethod> TEST_METHODS = Arrays.asList(new ContractMethod("some name"), new ContractMethod(TEST_METHOD_NAME));
    private final String TEST_NOT_EMPTY_STRING = "test_not_empty_string";

    @Test
    public void initialize_contractAddressNotEmpty_contractListNotEmpty() {
        when(view.getContractAddress()).thenReturn(TEST_NOT_EMPTY_STRING);
        when(view.getContractTemplateUiid()).thenReturn(TEST_UIID);
        when(interactor.getContractListByUiid(TEST_UIID)).thenReturn(TEST_METHODS);
        presenter.initializeViews();
        verify(view, times(1)).setRecyclerView(TEST_METHODS, true);

    }

    @Test
    public void initialize_contractAddressNotEmpty_contractListNull() {
        when(view.getContractAddress()).thenReturn(TEST_NOT_EMPTY_STRING);
        when(view.getContractTemplateUiid()).thenReturn(TEST_UIID);
        when(interactor.getContractListByUiid(TEST_UIID)).thenReturn(null);
        presenter.initializeViews();
        verify(view, never()).setRecyclerView(anyList(), anyBoolean());
        verify(view, times(1)).setAlertDialog(anyInt(), anyInt(), eq(BaseFragment.PopUpType.error));

    }

    private final String TEST_EMPTY_STRING = "";
    private final String TEST_ABI = "test_abi";

    @Test
    public void initialize_contractAddressEmpty_contractListNotEmpty() {
        when(view.getContractAddress()).thenReturn(TEST_EMPTY_STRING);
        when(view.getContractABI()).thenReturn(TEST_ABI);
        when(interactor.getContractListByAbi(TEST_ABI)).thenReturn(TEST_METHODS);
        presenter.initializeViews();
        verify(view, times(1)).setRecyclerView(TEST_METHODS, false);

    }

    @Test
    public void initialize_contractAddressEmpty_contractListNull() {
        when(view.getContractAddress()).thenReturn(TEST_EMPTY_STRING);
        when(view.getContractABI()).thenReturn(TEST_ABI);
        when(interactor.getContractListByAbi(TEST_ABI)).thenReturn(null);
        presenter.initializeViews();
        verify(view, never()).setRecyclerView(anyList(), anyBoolean());
        verify(view, times(1)).setAlertDialog(anyInt(), anyInt(), eq(BaseFragment.PopUpType.error));

    }

    @Test
    public void initialize_contractAddressEmpty() {
        when(view.getContractAddress()).thenReturn("");

    }

}
