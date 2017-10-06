package org.qtum.wallet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.model.backup.Backup;
import org.qtum.wallet.model.backup.ContractJSON;
import org.qtum.wallet.model.backup.TemplateJSON;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.restore_contracts_fragment.RestoreContractsInteractor;
import org.qtum.wallet.ui.fragment.restore_contracts_fragment.RestoreContractsPresenterImpl;
import org.qtum.wallet.ui.fragment.restore_contracts_fragment.RestoreContractsView;

import java.io.File;
import java.util.Arrays;

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.observers.TestSubscriber;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by drevnitskaya on 06.10.17.
 */

public class RestoreContractsPresenterTest {

    @Mock
    private RestoreContractsView view;
    @Mock
    private RestoreContractsInteractor interactor;
    private RestoreContractsPresenterImpl presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });
        RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
            @Override
            public Scheduler getIOScheduler() {
                return Schedulers.immediate();
            }
        });

        presenter = new RestoreContractsPresenterImpl(view, interactor);
    }

    @Test
    public void onRestoreClick_RestoreNothing() {
        presenter.onRestoreClick(false, false, false);
        verify(view, never()).getRestoreFile();
    }

    @Test
    public void onRestoreClick_FileDoesNotExistError() {
        when(view.getRestoreFile())
                .thenReturn(null);
        presenter.onRestoreClick(false, false, true);

        verify(view, never()).showRestoreDialogFragment(anyString(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void onRestoreClick_BackupFileError() throws Exception {
        when(view.getRestoreFile())
                .thenReturn(new File(""));
        when(interactor.getBackupFromFile((File) any()))
                .thenThrow(new Exception("Backup error"));

        presenter.onRestoreClick(false, false, true);

        verify(view, times(1)).setAlertDialog(anyInt(), anyString(), anyString(), (BaseFragment.PopUpType) any(),
                (BaseFragment.AlertDialogCallBack) any());
        verify(view, never()).showRestoreDialogFragment(anyString(), anyString(), anyString(), anyString(), anyString());
    }

    private static final Backup TEST_BACKUP = new Backup("date", Arrays.asList(new TemplateJSON("", "", "", "", "", "", "")),
            "", "", Arrays.asList(new ContractJSON("", "", "", "", "", "", false)), "");

    @Test
    public void onRestoreClick_Success() throws Exception {
        when(view.getRestoreFile())
                .thenReturn(new File(""));
        when(interactor.getBackupFromFile((File) any()))
                .thenReturn(TEST_BACKUP);

        presenter.onRestoreClick(true, false, false);

        verify(view, times(1)).showRestoreDialogFragment(anyString(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void createBackupData_Error() {

        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();
        presenter.createBackupData()
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(false);

    }

    @Test
    public void createBackupData() {

        presenter.setBackup(TEST_BACKUP);
        when(interactor.getTemplateValidity((TemplateJSON) any()))
                .thenReturn(true);
        when(interactor.getContractValidity((ContractJSON) any()))
                .thenReturn(true);
        when(interactor.validateContractCreationAddress((ContractJSON) any(), anyList()))
                .thenReturn(true);


        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();
        presenter.createBackupData()
                .subscribe(testSubscriber);

        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(true);

    }

    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().reset();
    }


}
