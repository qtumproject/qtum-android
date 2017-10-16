package org.qtum.wallet.utils.fingerprint_utils;

import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;


public final class FingerprintUtils {

    private FingerprintUtils() {
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static SensorState checkSensorState(@NonNull Context context) {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if (!keyguardManager.isKeyguardSecure()) {
            return SensorState.NOT_BLOCKED;
        }

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (!FingerprintManagerCompat.from(context).hasEnrolledFingerprints()) {
                return SensorState.NO_FINGERPRINTS;
            }
        } else {
            FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
            if (!fingerprintManager.hasEnrolledFingerprints()) {
                return SensorState.NO_FINGERPRINTS;
            }
        }

        return SensorState.READY;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isSensorStateAt(@NonNull SensorState state, @NonNull Context context) {
        return checkSensorState(context) == state;
    }
}
