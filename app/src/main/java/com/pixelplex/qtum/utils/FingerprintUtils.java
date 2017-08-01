package com.pixelplex.qtum.utils;

import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;


public final class FingerprintUtils {
    private FingerprintUtils() {
    }

    public enum mSensorState {
        NOT_BLOCKED,
        NO_FINGERPRINTS,
        READY
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static mSensorState checkSensorState(@NonNull Context context) {
            KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            if (!keyguardManager.isKeyguardSecure()) {
                return mSensorState.NOT_BLOCKED;
            }

            if (!FingerprintManagerCompat.from(context).hasEnrolledFingerprints()) {
                return mSensorState.NO_FINGERPRINTS;
            }

            return mSensorState.READY;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isSensorStateAt(@NonNull mSensorState state, @NonNull Context context) {
        return checkSensorState(context) == state;
    }
}
