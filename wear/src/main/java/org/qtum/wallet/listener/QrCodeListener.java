package org.qtum.wallet.listener;

import android.graphics.Bitmap;

/**
 * Created by kirillvolkov on 22.11.2017.
 */

public interface QrCodeListener {
    void onQrCodeReady(Bitmap bitmap);
}
