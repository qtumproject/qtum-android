package org.qtum.wallet.utils;

import android.os.Build;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by kirillvolkov on 31.07.17.
 */

public class CryptoUtilsCompat {

    public static String generateSHA256String(String inputString){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
           return CryptoUtils.generateSHA256String(inputString);
        }else {
            try {
                return SHA256(inputString);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String SHA256(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes());
        byte[] digest = md.digest();
        return Base64.encodeToString(digest, Base64.DEFAULT);
    }
}
