package com.pixelplex.qtum.ui.fragment.RestoreContractsFragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.common.io.Files;
import com.pixelplex.qtum.ui.activity.MainActivity.MainActivity;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.utils.FileUtils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * Created by max-v on 6/14/2017.
 */

public class RestoreContractsFragmentPresenter extends BaseFragmentPresenterImpl{

    RestoreContractsFragmentView mRestoreContractsFragmentView;

    RestoreContractsFragmentPresenter(RestoreContractsFragmentView restoreContractsFragmentView){
        mRestoreContractsFragmentView = restoreContractsFragmentView;
    }

    @Override
    public RestoreContractsFragmentView getView() {
        return mRestoreContractsFragmentView;
    }

    private final int FILE_SELECT_CODE = 0;
    private final int READ_EXTERNAL_STORAGE_CODE = 1;

    @Override
    public void onViewCreated() {
        super.onViewCreated();
        getView().hideBottomNavView(false);
        getView().getMainActivity().addActivityResultListener(new MainActivity.ActivityResultListener() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if(requestCode == FILE_SELECT_CODE)
                    if(resultCode == RESULT_OK) {
                        String path = FileUtils.getPath(getView().getContext(), data.getData());
                        if(path!=null) {

                            ParcelFileDescriptor mInputPFD = null ;
                            try {
                                mInputPFD = getView().getMainActivity().getContentResolver().openFileDescriptor(data.getData(), "r");
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                System.out.println("File not found...");
                                return;
                            }
                            if(mInputPFD!=null){
                                FileDescriptor fd = mInputPFD.getFileDescriptor();
                                FileInputStream fis = new FileInputStream(fd);
                                //process the input stream
                            }
                            FileInputStream fis = null ;
                            try {
                                fis = (FileInputStream) getView().getMainActivity().getContentResolver().openInputStream(data.getData());
                            }
                            catch (FileNotFoundException e1) {
                                e1.printStackTrace();
                                System.out.println("File not found...");
                                return ;
                            }


                            File file = new File(path);
                            String name = file.getName();
                            String  fileSize = String.valueOf(file.length()/1024) + " Kb";
                            getView().setFile(name,fileSize);
                        }else {
                            ParcelFileDescriptor mInputPFD = null ;
                            String result = "";
                            try {
                                mInputPFD = getView().getMainActivity().getContentResolver().openFileDescriptor(data.getData(), "r");
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                System.out.println("File not found...");
                                return;
                            }
                            if(mInputPFD!=null){
                                FileDescriptor fd = mInputPFD.getFileDescriptor();
                                FileInputStream fis = new FileInputStream(fd);
                                File targetFile = new File(getView().getContext().getFilesDir().getPath()+ "/tmp");
                                byte[] buffer = new byte[0];
                                try {
                                    buffer = new byte[fis.available()];
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    fis.read(buffer);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                                try {
                                    OutputStream outStream = new FileOutputStream(targetFile);
                                    outStream.write(buffer);
                                    outStream.flush();
                                    outStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                String content = String.valueOf(targetFile.length()/1024);
                                content+=2;
                                //process the input stream
                            }
                            FileInputStream fis = null ;
                            try {
                                fis = (FileInputStream) getView().getMainActivity().getContentResolver().openInputStream(data.getData());
                            }
                            catch (FileNotFoundException e1) {
                                e1.printStackTrace();
                                System.out.println("File not found...");
                                return ;
                            }
                            //TODO
                        }
                    }
            }
        });

        getView().getMainActivity().addPermissionResultListener(new MainActivity.PermissionsResultListener() {
            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                if(requestCode == READ_EXTERNAL_STORAGE_CODE) {
                    if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                        showFileChooser();
                    }
                }
            }
        });
    }

    public void checkPermissionAndOpenFileDialog(){
        boolean isPermissionGranted = getView().getMainActivity().loadPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE_CODE);
        if(isPermissionGranted){
            showFileChooser();
        }
    }

    public void onDeleteFileClick(){
        getView().deleteFile();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getView().showBottomNavView(false);
        getView().getMainActivity().removeResultListener();
        getView().getMainActivity().removePermissionResultListener();
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            getView().startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getView().getContext(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

}
