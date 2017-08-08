package com.pixelplex.qtum.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.Nullable;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.util.Base64;

import org.spongycastle.crypto.digests.SHA256Digest;
import org.spongycastle.util.encoders.Hex;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.concurrent.Callable;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

import rx.Observable;


@TargetApi(Build.VERSION_CODES.M)
public final class CryptoUtils {
    private static final String TAG = CryptoUtils.class.getSimpleName();

    private static final String KEY_ALIAS = "key_for_pin";
    private static final String KEY_STORE = "AndroidKeyStore";
    private static final String TRANSFORMATION = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";

    private static KeyStore sKeyStore;
    private static KeyPairGenerator sKeyPairGenerator;
    private static Cipher sCipher;

    private CryptoUtils() {
    }

    public static String encode(String inputString) {
        try {
            if (prepare() && initCipher(Cipher.ENCRYPT_MODE)) {
                byte[] bytes = sCipher.doFinal(inputString.getBytes());
                return Base64.encodeToString(bytes, Base64.NO_WRAP);
            }
        } catch (IllegalBlockSizeException | BadPaddingException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static Observable<String> encodeInBackground(final String inputString) {
        return rx.Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    if (prepare() && initCipher(Cipher.ENCRYPT_MODE)) {
                        byte[] bytes = sCipher.doFinal(inputString.getBytes());
                        return Base64.encodeToString(bytes, Base64.NO_WRAP);
                    }
                } catch (IllegalBlockSizeException | BadPaddingException exception) {
                    exception.printStackTrace();
                }
                return null;
            }
        });
    }

    public static String decode(String encodedString, Cipher cipher) {
        try {
            byte[] bytes = Base64.decode(encodedString, Base64.NO_WRAP);
            return new String(cipher.doFinal(bytes));
        } catch (IllegalBlockSizeException | BadPaddingException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    private static boolean prepare() {
        return getKeyStore() && getCipher() && getKey();
    }


    private static boolean getKeyStore() {
        try {
            sKeyStore = KeyStore.getInstance(KEY_STORE);
            sKeyStore.load(null);
            return true;
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            e.printStackTrace();
        }
        return false;
    }


    @TargetApi(Build.VERSION_CODES.M)
    private static boolean getKeyPairGenerator() {
        try {
            sKeyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, KEY_STORE);
            return true;
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return false;
    }


    @SuppressLint("GetInstance")
    private static boolean getCipher() {
        try {
            sCipher = Cipher.getInstance(TRANSFORMATION);
            return true;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean getKey() {
        try {
            return sKeyStore.containsAlias(KEY_ALIAS) || generateNewKey();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return false;

    }


    @TargetApi(Build.VERSION_CODES.M)
    private static boolean generateNewKey() {

        if (getKeyPairGenerator()) {

            try {
                sKeyPairGenerator.initialize(
                        new KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                                .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                                //.setUserAuthenticationRequired(true)
                                .build());
                sKeyPairGenerator.generateKeyPair();
                return true;
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    private static boolean initCipher(int mode) {
        try {
            sKeyStore.load(null);

            switch (mode) {
                case Cipher.ENCRYPT_MODE:
                    initEncodeCipher(mode);
                    break;

                case Cipher.DECRYPT_MODE:
                    initDecodeCipher(mode);
                    break;
                default:
                    return false; //this cipher is only for encode\decode
            }
            return true;

        } catch (KeyPermanentlyInvalidatedException exception) {
            deleteInvalidKey();

        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException |
                NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void initDecodeCipher(int mode) throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, InvalidKeyException {
        PrivateKey key = (PrivateKey) sKeyStore.getKey(KEY_ALIAS, null);
        sCipher.init(mode, key);
    }

    private static void initEncodeCipher(int mode) throws KeyStoreException, InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        PublicKey key = sKeyStore.getCertificate(KEY_ALIAS).getPublicKey();

        // workaround for using public key
        // from https://developer.android.com/reference/android/security/keystore/KeyGenParameterSpec.html
        PublicKey unrestricted = KeyFactory.getInstance(key.getAlgorithm()).generatePublic(new X509EncodedKeySpec(key.getEncoded()));
        // from https://code.google.com/p/android/issues/detail?id=197719
        OAEPParameterSpec spec = new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT);

        sCipher.init(mode, unrestricted, spec);
    }

    private static void deleteInvalidKey() {
        if (getKeyStore()) {
            try {
                sKeyStore.deleteEntry(KEY_ALIAS);
            } catch (KeyStoreException e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    public static FingerprintManagerCompat.CryptoObject getCryptoObject() {
        if (prepare() && initCipher(Cipher.DECRYPT_MODE)) {
            return new FingerprintManagerCompat.CryptoObject(sCipher);
        }
        return null;
    }

    @Nullable
    public static FingerprintManager.CryptoObject getCryptoObjectCompat23() {
        if (prepare() && initCipher(Cipher.DECRYPT_MODE)) {
            return new FingerprintManager.CryptoObject(sCipher);
        }
        return null;
    }

    public static String generateSHA256String(String inputString){
        byte[] input = Hex.decode(inputString);

        SHA256Digest sha256Digest = new SHA256Digest();
        sha256Digest.update(input, 0, input.length);
        byte[] out = new byte[sha256Digest.getDigestSize()];
        sha256Digest.doFinal(out, 0);

        return Hex.toHexString(out);
    }


}

