package com.pixelplex.qtum.ui.fragment.restore_contracts_fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pixelplex.qtum.R;
import com.pixelplex.qtum.model.SharedTemplate;
import com.pixelplex.qtum.model.ContractTemplate;
import com.pixelplex.qtum.model.contract.Contract;
import com.pixelplex.qtum.model.contract.Token;
import com.pixelplex.qtum.datastorage.FileStorageManager;
import com.pixelplex.qtum.datastorage.TinyDB;
import com.pixelplex.qtum.model.backup.Backup;
import com.pixelplex.qtum.model.backup.ContractJSON;
import com.pixelplex.qtum.model.backup.TemplateJSON;
import com.pixelplex.qtum.ui.activity.main_activity.MainActivity;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragment;
import com.pixelplex.qtum.ui.base.base_fragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.utils.FileUtils;
import com.pixelplex.qtum.utils.FontTextView;
import com.pixelplex.qtum.utils.ThemeUtils;

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
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;


public class RestoreContractsFragmentPresenter extends BaseFragmentPresenterImpl{

    private RestoreContractsFragmentView mRestoreContractsFragmentView;

    private File mRestoreFile;
    private Context mContext;
    private Backup mBackup;
    private AlertDialog mRestoreDialog;

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
                                ParcelFileDescriptor mInputPFD;
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
                        if(getFileExtension(name)!=null){
                            if(!getFileExtension(name).equals(".json")) {
                                getView().setAlertDialog(mContext.getString(R.string.something_went_wrong), "", "OK", BaseFragment.PopUpType.error, new BaseFragment.AlertDialogCallBack() {
                                    @Override
                                    public void onOkClick() {
                                        mRestoreFile.delete();
                                        mRestoreFile = null;
                                    }
                                });
                                return;
                            }
                        }
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

    private String getFileExtension(String mystr) {
        int index = mystr.indexOf('.');
        return index == -1? null : mystr.substring(index);
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
            mRestoreFile = null;
        }
        if(mBackup!=null){
            mBackup=null;
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
            if(is!=null) {
                is.close();
            }
            if(os!=null) {
                os.close();
            }
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

    public void onRestoreClick(final boolean restoreTemplates, final boolean restoreContracts, final boolean restoreTokens) {
        if (restoreTemplates || restoreContracts || restoreTokens) {
            if (mRestoreFile != null) {
                try {
                    Gson gson = new Gson();
                    mBackup = gson.fromJson(readFile(mRestoreFile), Backup.class);
                    int templatesCountInt = 0;
                    int contractsCountInt = 0;
                    int tokensCountInt = 0;

                    if (restoreTemplates) {
                        templatesCountInt = mBackup.getTemplates().size();
                    }
                    if (restoreContracts) {
                        for (ContractJSON contractJSON : mBackup.getContracts()) {
                            if (!contractJSON.getType().equals("token")) {
                                contractsCountInt++;
                            }
                        }
                    }
                    if (restoreTokens) {
                        for (ContractJSON contractJSON : mBackup.getContracts()) {
                            if (contractJSON.getType().equals("token")) {
                                tokensCountInt++;
                            }
                        }
                    }

                    String templatesCount = String.valueOf(templatesCountInt);
                    String contractsCount = String.valueOf(contractsCountInt);
                    String tokensCount = String.valueOf(tokensCountInt);

                    showRestoreDialogFragment(mBackup.getDateCreate(), mBackup.getFileVersion(), templatesCount, contractsCount, tokensCount, new RestoreDialogCallBack() {
                        @Override
                        public void onRestoreClick() {
                            getView().setProgressDialog();
                            createBackupData(restoreTemplates, restoreContracts, restoreTokens).subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<Boolean>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(Boolean aBoolean) {
                                            mRestoreDialog.dismiss();
                                            if (aBoolean) {
                                                getView().setAlertDialog(mContext.getString(R.string.restored_successfully), "", "OK", BaseFragment.PopUpType.confirm, new BaseFragment.AlertDialogCallBack() {
                                                    @Override
                                                    public void onOkClick() {
                                                        FragmentManager fm = getView().getFragment().getFragmentManager();
                                                        int count = fm.getBackStackEntryCount() - 2;
                                                        for (int i = 0; i < count; ++i) {
                                                            fm.popBackStack();
                                                        }
                                                    }
                                                });
                                            } else {
                                                getView().setAlertDialog(mContext.getString(R.string.something_went_wrong), "", "OK", BaseFragment.PopUpType.error, new BaseFragment.AlertDialogCallBack() {
                                                    @Override
                                                    public void onOkClick() {
                                                        onDeleteFileClick();
                                                    }
                                                });
                                            }
                                        }
                                    });
                        }
                    });
                } catch(Exception e) {
                    getView().setAlertDialog(mContext.getString(R.string.something_went_wrong), "", "OK", BaseFragment.PopUpType.error, new BaseFragment.AlertDialogCallBack() {
                        @Override
                        public void onOkClick() {
                            onDeleteFileClick();
                        }
                    });
                }
            }
        }
    }

    private Observable<Boolean> createBackupData(final boolean restoreTemplates, final boolean restoreContracts, final boolean restoreTokens){
        return rx.Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {

                if(!validateBackup()){
                    return false;
                }

                TinyDB tinyDB = new TinyDB(mContext);
                List<SharedTemplate> templates = tinyDB.getTemplates();

                if (!restoreContracts && !restoreTokens) {
                            for (TemplateJSON templateJSON : mBackup.getTemplates()) {
                                FileStorageManager.getInstance().importTemplate(mContext, templateJSON, templates);
                            }

                        } else if (!restoreTemplates && !restoreTokens) {
                            List<Contract> contractList = new ArrayList<>();
                            for (ContractJSON contractJSON : mBackup.getContracts()) {
                                if (!contractJSON.getType().equals("token")) {
                                    for (TemplateJSON templateJSON : mBackup.getTemplates()) {
                                        if (contractJSON.getTemplate().equals(templateJSON.getUuid())) {
                                            ContractTemplate contractTemplate = FileStorageManager.getInstance().importTemplate(mContext, templateJSON, templates);
                                            Contract contract = new Contract(contractJSON.getContractAddress(), contractTemplate.getUuid(), true, contractJSON.getPublishDate(), contractJSON.getContractCreationAddres(), contractJSON.getName());
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
                            for (ContractJSON contractJSON : mBackup.getContracts()) {
                                if (contractJSON.getType().equals("token")) {
                                    for (TemplateJSON templateJSON : mBackup.getTemplates()) {
                                        if (contractJSON.getTemplate().equals(templateJSON.getUuid())) {
                                            ContractTemplate contractTemplate = FileStorageManager.getInstance().importTemplate(mContext, templateJSON, templates);
                                            Token token = new Token(contractJSON.getContractAddress(), contractTemplate.getUuid(), true, contractJSON.getPublishDate(), contractJSON.getContractCreationAddres(), contractJSON.getName());
                                            token.setSubscribe(contractJSON.getIsActive());
                                            tokenList.add(token);
                                        }
                                    }
                                }
                                List<Token> tmpTokenList = tinyDB.getTokenList();
                                tmpTokenList.addAll(tokenList);
                                tinyDB.putTokenList(tmpTokenList);
                            }

                        } else if (!restoreTemplates) {
                            List<Token> tokenList = new ArrayList<>();
                            List<Contract> contractList = new ArrayList<>();
                            for (ContractJSON contractJSON : mBackup.getContracts()) {
                                if (contractJSON.getType().equals("token")) {
                                    for (TemplateJSON templateJSON : mBackup.getTemplates()) {
                                        if (contractJSON.getTemplate().equals(templateJSON.getUuid())) {
                                            ContractTemplate contractTemplate = FileStorageManager.getInstance().importTemplate(mContext, templateJSON, templates);
                                            Token token = new Token(contractJSON.getContractAddress(), contractTemplate.getUuid(), true, contractJSON.getPublishDate(), contractJSON.getContractCreationAddres(), contractJSON.getName());
                                            token.setSubscribe(contractJSON.getIsActive());
                                            tokenList.add(token);
                                        }
                                    }
                                } else {
                                    for (TemplateJSON templateJSON : mBackup.getTemplates()) {
                                        if (contractJSON.getTemplate().equals(templateJSON.getUuid())) {
                                            ContractTemplate contractTemplate = FileStorageManager.getInstance().importTemplate(mContext, templateJSON, templates);
                                            Contract contract = new Contract(contractJSON.getContractAddress(), contractTemplate.getUuid(), true, contractJSON.getPublishDate(), contractJSON.getContractCreationAddres(), contractJSON.getName());
                                            contractList.add(contract);
                                        }
                                    }
                                }
                            }
                            List<Token> tmpTokenList = tinyDB.getTokenList();
                            tmpTokenList.addAll(tokenList);
                            tinyDB.putTokenList(tmpTokenList);
                            List<Contract> tmpContractList = tinyDB.getContractListWithoutToken();
                            tmpContractList.addAll(contractList);
                            tinyDB.putContractListWithoutToken(tmpContractList);

                        } else if(restoreContracts && restoreTokens){
                            List<Token> tokenList = new ArrayList<>();
                            List<Contract> contractList = new ArrayList<>();
                            for (TemplateJSON templateJSON : mBackup.getTemplates()) {
                                ContractTemplate contractTemplate = FileStorageManager.getInstance().importTemplate(mContext, templateJSON, templates);
                                for(ContractJSON contractJSON : mBackup.getContracts()){
                                    if(contractJSON.getTemplate().equals(templateJSON.getUuid())){
                                        if(!contractJSON.getType().equals("token")){
                                            Contract contract = new Contract(contractJSON.getContractAddress(), contractTemplate.getUuid(), true, contractJSON.getPublishDate(), contractJSON.getContractCreationAddres(), contractJSON.getName());
                                            contractList.add(contract);
                                        } else if(contractJSON.getType().equals("token")){
                                            Token token = new Token(contractJSON.getContractAddress(), contractTemplate.getUuid(), true, contractJSON.getPublishDate(), contractJSON.getContractCreationAddres(), contractJSON.getName());
                                            token.setSubscribe(contractJSON.getIsActive());
                                            tokenList.add(token);
                                        }
                                    }
                                }
                            }
                            List<Token> tmpTokenList = tinyDB.getTokenList();
                            tmpTokenList.addAll(tokenList);
                            tinyDB.putTokenList(tmpTokenList);
                            List<Contract> tmpContractList = tinyDB.getContractListWithoutToken();
                            tmpContractList.addAll(contractList);
                            tinyDB.putContractListWithoutToken(tmpContractList);

                        }else if(restoreContracts){
                            List<Contract> contractList = new ArrayList<>();
                            for (TemplateJSON templateJSON : mBackup.getTemplates()) {
                                ContractTemplate contractTemplate = FileStorageManager.getInstance().importTemplate(mContext, templateJSON, templates);
                                for(ContractJSON contractJSON : mBackup.getContracts()){
                                    if(contractJSON.getTemplate().equals(templateJSON.getUuid())){
                                        if(!contractJSON.getType().equals("token")){
                                            Contract contract = new Contract(contractJSON.getContractAddress(), contractTemplate.getUuid(), true, contractJSON.getPublishDate(), contractJSON.getContractCreationAddres(), contractJSON.getName());
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
                            for (TemplateJSON templateJSON : mBackup.getTemplates()) {
                                ContractTemplate contractTemplate = FileStorageManager.getInstance().importTemplate(mContext, templateJSON, templates);
                                for(ContractJSON contractJSON : mBackup.getContracts()){
                                    if(contractJSON.getTemplate().equals(templateJSON.getUuid())){
                                        if(contractJSON.getType().equals("token")){
                                            Token token = new Token(contractJSON.getContractAddress(), contractTemplate.getUuid(), true, contractJSON.getPublishDate(), contractJSON.getContractCreationAddres(), contractJSON.getName());
                                            token.setSubscribe(contractJSON.getIsActive());
                                            tokenList.add(token);
                                        }
                                    }
                                }
                            }
                            List<Token> tmpTokenList = tinyDB.getTokenList();
                            tmpTokenList.addAll(tokenList);
                            tinyDB.putTokenList(tmpTokenList);
                        }


                return true;
            }
        });
    }

    private boolean validateBackup(){

        if(mBackup == null)
            return false;

        if(mBackup.getContracts() != null) {
            for (ContractJSON contractJSON : mBackup.getContracts()) {
                if (!contractJSON.getValidity()) {
                    return false;
                }
            }
        }

        if(mBackup.getTemplates() != null) {
            for (TemplateJSON templateJSON : mBackup.getTemplates()) {
                if (!templateJSON.getValidity()) {
                    return false;
                }
            }
        }

        return true;
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

    private void showRestoreDialogFragment(String backupDate, String backupAppVersion, String templatesSum, String contractsSum, String tokensSum, final RestoreDialogCallBack callBack){
        View view = LayoutInflater.from(getView().getMainActivity()).inflate(ThemeUtils.getCurrentTheme(getView().getContext()).equals(ThemeUtils.THEME_DARK)? R.layout.dialog_restore_contracts_fragment : R.layout.dialog_restore_contracts_fragment_light,null);
        ((FontTextView)view.findViewById(R.id.tv_back_up_date)).setText(backupDate);
        ((FontTextView)view.findViewById(R.id.tv_back_up_app_version)).setText(backupAppVersion);
        ((FontTextView)view.findViewById(R.id.tv_templates)).setText(templatesSum);
        ((FontTextView)view.findViewById(R.id.tv_contracts)).setText(contractsSum);
        ((FontTextView)view.findViewById(R.id.tv_tokens)).setText(tokensSum);
        view.findViewById(R.id.bt_restore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.onRestoreClick();
            }
        });
        view.findViewById(R.id.bt_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRestoreDialog.dismiss();
            }
        });
        mRestoreDialog = new AlertDialog
                .Builder(mContext)
                .setView(view)
                .create();

        if(mRestoreDialog.getWindow() != null) {
            mRestoreDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        mRestoreDialog.setCanceledOnTouchOutside(false);
        mRestoreDialog.show();
    }

    interface RestoreDialogCallBack{
        void onRestoreClick();
    }
}