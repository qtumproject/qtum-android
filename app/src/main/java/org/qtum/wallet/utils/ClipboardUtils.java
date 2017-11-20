package org.qtum.wallet.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import static android.content.Context.CLIPBOARD_SERVICE;

public class ClipboardUtils {

    public static void copyToClipBoard(Context context, String text, CopyCallback copyCallback) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", text);
        clipboard.setPrimaryClip(clip);
        if (copyCallback != null) {
            copyCallback.onCopyToClipBoard();
        }
    }

    public interface CopyCallback {
        void onCopyToClipBoard();
    }
}
