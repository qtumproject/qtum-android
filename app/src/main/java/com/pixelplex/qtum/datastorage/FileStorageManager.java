package com.pixelplex.qtum.datastorage;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.pixelplex.qtum.BuildConfig;
import com.pixelplex.qtum.datastorage.model.ContractTemplate;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.ContractMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by kirillvolkov on 25.05.17.
 */

public class FileStorageManager {

    private static FileStorageManager _instance;

    public final static String TAG = "STORAGE MANAGER: ";

    private final static String prefMigrationBuildVersion = "migration_buid_version";

    private final static String CrowdSale = "Crowdsale";
    private static String[] STANDART_CONTRACTS = {"Standart", "Version1", "Version2", "Crowdsale"};

    private static String abiContractName = "abi-contract";
    private static String byteCodeContractName = "bitecode-contract";
    private static String sourceContractName = "source-contract";

    private static String TYPE = "type";
    private static String CONSTRUCTOR_TYPE = "constructor";
    private static String FUNCTION_TYPE = "function";

    private static String CONTRACTS_PACKAGE = "contracts";

    public static FileStorageManager getInstance() {
        if(_instance == null){
            _instance = new FileStorageManager();
        }

        return _instance;
    }

    public boolean writeContract(Context context, String packageName, String fileName, String fileContent) {
           return writeFile(context, packageName, fileName, fileContent);
    }

    public String readContract(Context context,String packageName, String fileName) {
            return readFile(context, packageName, fileName);
    }

    private File getPackagePath(ContextWrapper cw, String targetPackage) {
        return cw.getDir(targetPackage, Context.MODE_PRIVATE);
    }

    private boolean writeFile(Context context, String packageName, String fileName, String fileContent) {

        ContextWrapper cw = new ContextWrapper(context);
        File contractDir = getPackagePath(cw,CONTRACTS_PACKAGE);
        File mFileDirectory = new File(String.format("%s/%s",contractDir.getAbsolutePath(),packageName));

        boolean v = mFileDirectory.mkdir();

        File fullPath = new File(mFileDirectory, fileName);

        if(!fullPath.exists()) {

            try {

                FileOutputStream fOut = new FileOutputStream(fullPath, true);
                OutputStreamWriter osw = new OutputStreamWriter(fOut);

                osw.write(fileContent);
                osw.flush();
                osw.close();
            } catch (IOException e) {

                e.printStackTrace();
                return false;
            }
            Log.d(TAG, "writeFile: Complete");
            return true;
        }else {
            Log.d(TAG, "writeFile: File Exists");
            return true;
        }
    }

    private String readFile(Context context, String packageName, String fileName) {

        int i;
        String data = "";

        ContextWrapper cw = new ContextWrapper(context);
        File contractDir = getPackagePath(cw,CONTRACTS_PACKAGE);

        try {

        FileInputStream fIn = new FileInputStream(new File(String.format("%s/%s/%s",contractDir,packageName,fileName)));

        InputStreamReader isr = new InputStreamReader(fIn);

        while ((i = isr.read())!=-1){
            data += (char)i;
        }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "readFile: "+data);
        return data;
    }

    public String
    readFromAsset(Context context, String packageName, String fileName) {

        String data = "";
        String mLine;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(String.format("%s/%s/%s",CONTRACTS_PACKAGE,packageName,fileName))));

            while ((mLine = reader.readLine()) != null) {
                data += mLine;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d(TAG, "readFromAsset: "+data);
        return data;
    }

    public List<ContractTemplate> getContractTemplateList(Context context) {
        ContextWrapper cw = new ContextWrapper(context);
        File contractDir = getPackagePath(cw,CONTRACTS_PACKAGE);
        List<ContractTemplate> list = new ArrayList<>();

        TinyDB tinyDB = new TinyDB(context);
        ArrayList<String> contractTemplateStringList = tinyDB.getListString(tinyDB.CONTRACT_TEMPLATE_LIST);
        File contractFolder;

        for (String contractTemplateString : contractTemplateStringList){
            contractFolder = new File(contractDir.getPath()+"/"+contractTemplateString);
            list.add(new ContractTemplate(contractFolder.getName(),contractFolder.lastModified(),(contractFolder.getName().equalsIgnoreCase(CrowdSale)? CrowdSale : "Token")));
        }

//        for(File file :  contractDir.listFiles()){
//            list.add(new ContractTemplate(file.getName(),file.lastModified(),(file.getName().equalsIgnoreCase(CrowdSale)? CrowdSale : "Token")));
//        }

        Collections.sort(list, new Comparator<ContractTemplate>() {
            @Override
            public int compare(ContractTemplate contractInfo, ContractTemplate t1) {
                return contractInfo.getDate() > t1.getDate() ? 1 : contractInfo.getDate() < t1.getDate() ? -1 : 0;
            }
        });

        return list;
    }

    public boolean writeAbiContract(Context context, String packageName,String content){
        return writeContract(context,packageName,abiContractName,content);
    }

    public boolean writeByteCodeContract(Context context, String packageName,String content){
        return writeContract(context,packageName,byteCodeContractName,content);
    }

    public boolean writeSourceContract(Context context, String packageName,String content){
        return writeContract(context,packageName,sourceContractName,content);
    }

    public String readAbiContract(Context context, String packageName) {
        return readContract(context,packageName,abiContractName);
    }

    public String readByteCodeContract(Context context, String packageName) {
        return readContract(context,packageName,byteCodeContractName);
    }

    public String readSourceContract(Context context, String packageName) {
        return readContract(context,packageName,sourceContractName);
    }

//    READ DEFAULT CONTRACTS
    private String readAbiContractAsset(Context context, String packageName) {
        return readFromAsset(context,packageName,abiContractName);
    }

    private String readByteCodeContractAsset(Context context, String packageName) {
        return readFromAsset(context,packageName,byteCodeContractName);
    }

    private String readSourceContractAsset(Context context, String packageName) {
        return readFromAsset(context,packageName,sourceContractName);
    }
//    READ DEFAULT CONTRACTS

    private boolean migrateContract(Context context, String packageName) {
        boolean result = true;

        String readData = readAbiContractAsset(context,packageName);
        if(TextUtils.isEmpty(readData)) {
            return false;
        }
       result =  writeAbiContract(context,packageName,readData);

        if(result) {
            readData = readByteCodeContractAsset(context, packageName);
            if (TextUtils.isEmpty(readData)) {
                return false;
            }
            result = writeByteCodeContract(context, packageName, readData);
        }else {
            return false;
        }

        if(result) {
            readData = readSourceContractAsset(context, packageName);
            if (TextUtils.isEmpty(readData)) {
                return false;
            }
            result = writeSourceContract(context, packageName, readData);
        }else {
            return false;
        }

        return result;
    }

    public ContractMethod getContractConstructor(Context context, String contractName) {
      String abiContent = readAbiContract(context,contractName);
        JSONArray array = null;
        try {
            array = new JSONArray(abiContent);

            for (int i = 0; i < array.length(); i++) {
                JSONObject jb = (JSONObject)array.getJSONObject(i);
                if(CONSTRUCTOR_TYPE.equals(jb.getString(TYPE))){
                    Gson gson = new Gson();
                    return gson.fromJson(jb.toString(), ContractMethod.class);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ContractMethod> getContractMethods(final Context context, String contractName) {
        String abiContent = readAbiContract(context,contractName);
        JSONArray array = null;
        List<ContractMethod> contractMethods = new ArrayList<>();
        try {
            array = new JSONArray(abiContent);

            for (int i = 0; i < array.length(); i++) {
                JSONObject jb = (JSONObject)array.getJSONObject(i);
                if(FUNCTION_TYPE.equals(jb.getString(TYPE))){
                    Gson gson = new Gson();
                    contractMethods.add(gson.fromJson(jb.toString(), ContractMethod.class));
                }
            }
            Collections.sort(contractMethods, new Comparator<ContractMethod>() {
                @Override
                public int compare(ContractMethod contractMethod, ContractMethod t1) {
                    if(contractMethod.constant && !t1.constant){
                        return -1;
                    } else if(!contractMethod.constant && t1.constant){
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
            return contractMethods;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean migrateDefaultContracts(Context context) {
        if(!isDefaultContractsMigrates(context)) {
            ArrayList<String> contractTemplateList = new ArrayList<>();
            for (String contractPackage : STANDART_CONTRACTS) {
                if (!migrateContract(context, contractPackage)) {
                    return false;
                }
                contractTemplateList.add(contractPackage);
            }
            TinyDB tinyDB = new TinyDB(context);
            tinyDB.putListString(tinyDB.CONTRACT_TEMPLATE_LIST, contractTemplateList);
            commitDefaultContractsMigration(context);
            Log.d(TAG, "migrateDefaultContracts: Migration Complete");
            return true;
        } else {
            Log.d(TAG, "migrateDefaultContracts: Migration already performed");
            return true;
        }
    }

    private boolean isDefaultContractsMigrates(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return BuildConfig.VERSION_CODE == prefs.getInt(prefMigrationBuildVersion,0);
    }

    private boolean commitDefaultContractsMigration(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.edit().putInt(prefMigrationBuildVersion, BuildConfig.VERSION_CODE).commit();
    }

}
