package com.pixelplex.qtum.ui.fragment.RestoreContractsFragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.Contract;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.Token;
import com.pixelplex.qtum.datastorage.FileStorageManager;
import com.pixelplex.qtum.datastorage.TinyDB;
import com.pixelplex.qtum.datastorage.backupmodel.Backup;
import com.pixelplex.qtum.datastorage.backupmodel.ContractJSON;
import com.pixelplex.qtum.datastorage.backupmodel.TemplateJSON;
import com.pixelplex.qtum.ui.activity.MainActivity.MainActivity;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.utils.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * Created by max-v on 6/14/2017.
 */

public class RestoreContractsFragmentPresenter extends BaseFragmentPresenterImpl{

    private RestoreContractsFragmentView mRestoreContractsFragmentView;

    private File mRestoreFile;
    private Context mContext;

    RestoreContractsFragmentPresenter(RestoreContractsFragmentView restoreContractsFragmentView){
        mRestoreContractsFragmentView = restoreContractsFragmentView;
        mContext = getView().getContext();
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
                if(requestCode == FILE_SELECT_CODE) {
                    if (resultCode == RESULT_OK) {
                        String path = FileUtils.getPath(getView().getContext(), data.getData());
                        if (path != null) {
                            File responseFile = new File(path);
                            mRestoreFile = new File(getView().getContext().getFilesDir(), responseFile.getName());
                            try {
                                copyFileUsingStream(responseFile,mRestoreFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                ParcelFileDescriptor mInputPFD = null;
                                mInputPFD = getView().getMainActivity().getContentResolver().openFileDescriptor(data.getData(), "r");
                                if (mInputPFD != null) {
                                    FileDescriptor fd = mInputPFD.getFileDescriptor();
                                    FileInputStream fis = new FileInputStream(fd);

                                    Uri uri = data.getData();

                                    String fileName="";
                                    Cursor cursor = null;
                                    try {
                                        cursor = getView().getMainActivity().getContentResolver().query(uri, new String[]{
                                                MediaStore.Images.ImageColumns.DISPLAY_NAME
                                        }, null, null, null);
                                        if (cursor != null && cursor.moveToFirst()) {
                                            fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME));
                                        }
                                    } finally {
                                        if (cursor != null) {
                                            cursor.close();
                                        }
                                    }

                                    mRestoreFile = new File(getView().getContext().getFilesDir(), fileName);

                                    byte[] buffer;
                                    buffer = new byte[fis.available()];
                                    fis.read(buffer);

                                    OutputStream outStream = new FileOutputStream(mRestoreFile);
                                    outStream.write(buffer);
                                    outStream.flush();
                                    outStream.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                return;
                            }

                        }
                        String name = mRestoreFile.getName();
                        String fileSize = String.valueOf((int) Math.ceil(mRestoreFile.length() / 1024.0)) + " Kb";
                        getView().setFile(name, fileSize);
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
        if(getView().getMainActivity().checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            showFileChooser();
        }else{
            getView().getMainActivity().loadPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE_CODE);
        }
    }

    public void onDeleteFileClick(){
        getView().deleteFile();
        if(mRestoreFile!=null){
            mRestoreFile.delete();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mRestoreFile!=null){
            mRestoreFile.delete();
        }
        getView().showBottomNavView(false);
        getView().getMainActivity().removeResultListener();
        getView().getMainActivity().removePermissionResultListener();
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
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

    public void onRestoreClick(boolean restoreTemplates, boolean restoreContracts, boolean restoreTokens) {
        if (restoreTemplates || restoreContracts || restoreTokens) {
            if (mRestoreFile != null) {
                Gson gson = new Gson();
                TinyDB tinyDB = new TinyDB(mContext);
                Backup backup = gson.fromJson(readFile(mRestoreFile), Backup.class);
                if (!restoreContracts && !restoreTokens) {
                    for (TemplateJSON templateJSON : backup.getTemplates()) {
                        FileStorageManager.getInstance().importTemplate(mContext, templateJSON.getSource(), templateJSON.getBitecode(), templateJSON.getAbi(), templateJSON.getType(), templateJSON.getName(), templateJSON.getCreationDate());
                    }

                } else if (!restoreTemplates && !restoreTokens) {
                    List<Contract> contractList = new ArrayList<>();
                    for (ContractJSON contractJSON : backup.getContracts()) {
                        if (!contractJSON.getType().equals("token")) {
                            for (TemplateJSON templateJSON : backup.getTemplates()) {
                                if (contractJSON.getTemplate() == templateJSON.getUiid()) {
                                    long uiid = FileStorageManager.getInstance().importTemplate(mContext, templateJSON.getSource(), templateJSON.getBitecode(), templateJSON.getAbi(), templateJSON.getType(), templateJSON.getName(), templateJSON.getCreationDate());
                                    Contract contract = new Contract(contractJSON.getContractAddress(), uiid, true, contractJSON.getPublishDate(), contractJSON.getContractCreationAddres(), contractJSON.getName());
                                    contractList.add(contract);
                                }
                            }
                        }
                        List<Contract> tmpContractList = tinyDB.getContractListWithoutToken();
                        tmpContractList.addAll(contractList);
                        tinyDB.putContractListWithoutToken(tmpContractList);
                    }

                } else if (!restoreTemplates && !restoreContracts) {
                    List<Token> tokenList = new ArrayList<>();
                    for (ContractJSON contractJSON : backup.getContracts()) {
                        if (contractJSON.getType().equals("token")) {
                            for (TemplateJSON templateJSON : backup.getTemplates()) {
                                if (contractJSON.getTemplate() == templateJSON.getUiid()) {
                                    long uiid = FileStorageManager.getInstance().importTemplate(mContext, templateJSON.getSource(), templateJSON.getBitecode(), templateJSON.getAbi(), templateJSON.getType(), templateJSON.getName(), templateJSON.getCreationDate());
                                    Token token = new Token(contractJSON.getContractAddress(), uiid, true, contractJSON.getPublishDate(), contractJSON.getContractCreationAddres(), contractJSON.getName());
                                    token.setSubscribe(contractJSON.getIsActive());
                                    tokenList.add(token);
                                }
                            }
                        }
                        List<Token> tmpTokenList = tinyDB.getTokenList();
                        tmpTokenList.addAll(tokenList);
                        tinyDB.putTokenList(tokenList);
                    }

                } else if (!restoreTemplates) {
                    List<Token> tokenList = new ArrayList<>();
                    List<Contract> contractList = new ArrayList<>();
                    for (ContractJSON contractJSON : backup.getContracts()) {
                        if (contractJSON.getType().equals("token")) {
                            for (TemplateJSON templateJSON : backup.getTemplates()) {
                                if (contractJSON.getTemplate() == templateJSON.getUiid()) {
                                    long uiid = FileStorageManager.getInstance().importTemplate(mContext, templateJSON.getSource(), templateJSON.getBitecode(), templateJSON.getAbi(), templateJSON.getType(), templateJSON.getName(), templateJSON.getCreationDate());
                                    Token token = new Token(contractJSON.getContractAddress(), uiid, true, contractJSON.getPublishDate(), contractJSON.getContractCreationAddres(), contractJSON.getName());
                                    token.setSubscribe(contractJSON.getIsActive());
                                    tokenList.add(token);
                                }
                            }
                        } else {
                            for (TemplateJSON templateJSON : backup.getTemplates()) {
                                if (contractJSON.getTemplate() == templateJSON.getUiid()) {
                                    long uiid = FileStorageManager.getInstance().importTemplate(mContext, templateJSON.getSource(), templateJSON.getBitecode(), templateJSON.getAbi(), templateJSON.getType(), templateJSON.getName(), templateJSON.getCreationDate());
                                    Contract contract = new Contract(contractJSON.getContractAddress(), uiid, true, contractJSON.getPublishDate(), contractJSON.getContractCreationAddres(), contractJSON.getName());
                                    contractList.add(contract);
                                }
                            }
                        }
                    }
                    List<Token> tmpTokenList = tinyDB.getTokenList();
                    tmpTokenList.addAll(tokenList);
                    tinyDB.putTokenList(tokenList);
                    List<Contract> tmpContractList = tinyDB.getContractListWithoutToken();
                    tmpContractList.addAll(contractList);
                    tinyDB.putContractListWithoutToken(tmpContractList);

                } else if(restoreContracts && restoreTokens){
                    List<Token> tokenList = new ArrayList<>();
                    List<Contract> contractList = new ArrayList<>();
                    for (TemplateJSON templateJSON : backup.getTemplates()) {
                        long uiid = FileStorageManager.getInstance().importTemplate(mContext, templateJSON.getSource(), templateJSON.getBitecode(), templateJSON.getAbi(), templateJSON.getType(), templateJSON.getName(), templateJSON.getCreationDate());
                        for(ContractJSON contractJSON : backup.getContracts()){
                            if(contractJSON.getTemplate() == uiid){
                                if(!contractJSON.getType().equals("token")){
                                    Contract contract = new Contract(contractJSON.getContractAddress(), uiid, true, contractJSON.getPublishDate(), contractJSON.getContractCreationAddres(), contractJSON.getName());
                                    contractList.add(contract);
                                } else if(contractJSON.getType().equals("token")){
                                    Token token = new Token(contractJSON.getContractAddress(), uiid, true, contractJSON.getPublishDate(), contractJSON.getContractCreationAddres(), contractJSON.getName());
                                    token.setSubscribe(contractJSON.getIsActive());
                                    tokenList.add(token);
                                }
                            }
                        }
                    }
                    List<Token> tmpTokenList = tinyDB.getTokenList();
                    tmpTokenList.addAll(tokenList);
                    tinyDB.putTokenList(tokenList);
                    List<Contract> tmpContractList = tinyDB.getContractListWithoutToken();
                    tmpContractList.addAll(contractList);
                    tinyDB.putContractListWithoutToken(tmpContractList);

                }else if(restoreContracts){
                    List<Contract> contractList = new ArrayList<>();
                    for (TemplateJSON templateJSON : backup.getTemplates()) {
                        long uiid = FileStorageManager.getInstance().importTemplate(mContext, templateJSON.getSource(), templateJSON.getBitecode(), templateJSON.getAbi(), templateJSON.getType(), templateJSON.getName(), templateJSON.getCreationDate());
                        for(ContractJSON contractJSON : backup.getContracts()){
                            if(contractJSON.getTemplate() == uiid){
                                if(!contractJSON.getType().equals("token")){
                                    Contract contract = new Contract(contractJSON.getContractAddress(), uiid, true, contractJSON.getPublishDate(), contractJSON.getContractCreationAddres(), contractJSON.getName());
                                    contractList.add(contract);
                                }
                            }
                        }
                    }
                    List<Contract> tmpContractList = tinyDB.getContractListWithoutToken();
                    tmpContractList.addAll(contractList);
                    tinyDB.putContractListWithoutToken(tmpContractList);

                }else{
                    List<Token> tokenList = new ArrayList<>();
                    for (TemplateJSON templateJSON : backup.getTemplates()) {
                        long uiid = FileStorageManager.getInstance().importTemplate(mContext, templateJSON.getSource(), templateJSON.getBitecode(), templateJSON.getAbi(), templateJSON.getType(), templateJSON.getName(), templateJSON.getCreationDate());
                        for(ContractJSON contractJSON : backup.getContracts()){
                            if(contractJSON.getTemplate() == uiid){
                                if(contractJSON.getType().equals("token")){
                                    Token token = new Token(contractJSON.getContractAddress(), uiid, true, contractJSON.getPublishDate(), contractJSON.getContractCreationAddres(), contractJSON.getName());
                                    token.setSubscribe(contractJSON.getIsActive());
                                    tokenList.add(token);
                                }
                            }
                        }
                    }
                    List<Token> tmpTokenList = tinyDB.getTokenList();
                    tmpTokenList.addAll(tokenList);
                    tinyDB.putTokenList(tokenList);
                }
            }
        }
    }

    private String readFile(File file){

        FileReader inputFile = null;
        String data="";
        try {
            inputFile = new FileReader(file);
            BufferedReader bufferReader = new BufferedReader(inputFile);

            String line;
            while ((line = bufferReader.readLine()) != null)   {
                data+=line;
            }
            bufferReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;

    }
}