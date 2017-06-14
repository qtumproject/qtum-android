package com.pixelplex.qtum.ui.fragment.RestoreContractsFragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.pixelplex.qtum.ui.activity.MainActivity.MainActivity;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.utils.FileUtils;

import java.io.File;

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
        getView().getFragmentActivity().addActivityResultListener(new MainActivity.ActivityResultListener() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                if(requestCode == FILE_SELECT_CODE)
                    if(resultCode == RESULT_OK) {
                        String path = FileUtils.getPath(getView().getContext(), data.getData());
                        File file = new File(path);
                        String name = file.getName();
                        long time = file.lastModified();
                    }
            }
        });

        getView().getFragmentActivity().addPermissionResultListener(new MainActivity.PermissionsResultListener() {
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

    public void checkPermissionAndShow(){
        boolean isPermissionGranted = getView().getFragmentActivity().loadPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE_CODE);
        if(isPermissionGranted){
            showFileChooser();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getView().showBottomNavView(false);
        getView().getFragmentActivity().removeResultListener();
        getView().getFragmentActivity().removePermissionResultListener();
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
