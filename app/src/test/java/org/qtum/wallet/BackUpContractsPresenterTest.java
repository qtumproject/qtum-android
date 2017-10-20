package org.qtum.wallet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.backup_contracts_fragment.BackupContractsInteractor;
import org.qtum.wallet.ui.fragment.backup_contracts_fragment.BackupContractsPresenterImpl;
import org.qtum.wallet.ui.fragment.backup_contracts_fragment.BackupContractsView;

import java.io.File;

import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class BackUpContractsPresenterTest {

    @Mock
    private BackupContractsView view;
    @Mock
    private BackupContractsInteractor interactor;
    private BackupContractsPresenterImpl presenter;

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

        presenter = new BackupContractsPresenterImpl(view, interactor);
    }

    @Test
    public void permissionGrantedForCreateBackUpFile_Success(){
        File file = mock(File.class);
        when(interactor.createBackUpFile()).thenReturn(Observable.just((file)));
        presenter.permissionGrantedForCreateBackUpFile();
        verify(view,times(1)).setProgressDialog();
        verify(view,times(1)).dismissProgressDialog();
        verify(view,times(1)).setUpFile(anyString());
    }

    @Test
    public void permissionGrantedForCreateBackUpFile_Error(){
        when(interactor.createBackUpFile()).thenReturn(Observable.<File>error(new Throwable()));
        presenter.permissionGrantedForCreateBackUpFile();
        verify(view,times(1)).setProgressDialog();
        verify(view,times(1)).setAlertDialog(anyInt(),anyInt(),(BaseFragment.PopUpType)any());
    }

    @Test
    public void permissionGrantedForCreateAndBackUpFile_Success(){
        File file = mock(File.class);
        when(interactor.createBackUpFile()).thenReturn(Observable.just((file)));
        presenter.permissionGrantedForCreateAndBackUpFile();
        verify(view,times(1)).setProgressDialog();
        verify(view,times(1)).dismissProgressDialog();
        verify(view,times(1)).setUpFile(anyString());
        verify(view,times(1)).checkPermissionForBackupFile();
    }

    @Test
    public void permissionGrantedForCreateAndBackUpFile_Error(){
        when(interactor.createBackUpFile()).thenReturn(Observable.<File>error(new Throwable()));
        presenter.permissionGrantedForCreateAndBackUpFile();
        verify(view,times(1)).setProgressDialog();
        verify(view,times(1)).setAlertDialog(anyInt(),anyInt(),(BaseFragment.PopUpType)any());
    }

    @Test
    public void permissionGrantedForChooseShareMethod_Success(){
        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        presenter.setBackUpFile(file);
        presenter.permissionGrantedForChooseShareMethod();
        verify(view,times(1)).chooseShareMethod(anyString());
    }

    @Test
    public void permissionGrantedForChooseShareMethod_Error(){
        File file = mock(File.class);
        when(file.exists()).thenReturn(false);
        presenter.setBackUpFile(file);
        presenter.permissionGrantedForChooseShareMethod();
        verify(view,times(1)).setAlertDialog(anyInt(),anyInt(),(BaseFragment.PopUpType)any());
        verify(view,never()).chooseShareMethod(anyString());
    }

    @Test
    public void onBackUpClick_test(){
        presenter.onBackUpClick();
        verify(view,times(1)).checkPermissionForBackupFile();
    }

    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
        RxJavaPlugins.getInstance().reset();
    }

}
